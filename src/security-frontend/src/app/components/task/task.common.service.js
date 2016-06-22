/**
 * Created by yauheni.putsykovich on 01.06.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .service('taskCommonService', taskCommonService);

    /** @ngInject */
    function taskCommonService(aclServiceBuilder, taskService, searchService, $state, $q, userService, dialogService, collections, util) {
        var openSearchContactDialog = createAddAction('app/components/task/tabs/contacts/search-contact-dialog.view.html', 'Add Contact for Task', searchService.contactMode());
        var openSearchCompanyDialog = createAddAction('app/components/task/tabs/contacts/search-company-dialog.view.html', 'Add Company for Task', searchService.companyMode());

        return {
            initContext: initContext,
            createAclHandler: createAclHandler
        };

        function initContext(context) {
            function taskResolver() {
                return context.task;
            }

            function aclHandlerResolver() {
                return context.aclHandler;
            }

            function contactsResolver() {
                return context.task.contacts = context.task.contacts || [];
            }

            function companiesResolver() {
                return context.task.companies = context.task.companies || [];
            }

            context.onStartDateTimeChange = createStartDateTimeChangeListener(taskResolver);
            context.onEndDateTimeChange = createEndDateTimeChangeListener(taskResolver);
            context.aclHandler = createAclHandler(taskResolver);
            context.submit = createSaveOrUpdateAction(taskResolver, aclHandlerResolver);
            context.cancel = goToTaskList;

            context.addCompany = createAddCompaniesForTaskAction(companiesResolver);
            context.selectAllCompanies = util.createSelectAction(companiesResolver);
            context.selectOneCompany = util.createSelectAction(companiesResolver, function (isEvery) {
                context.isSelectedAllCompanies = isEvery;
            });
            context.removeCompanies = createRemoveAction(companiesResolver, function (rest) {
                context.task.companies = rest;
                context.isSelectedAllCompanies = false;
            });

            context.addContact = createAddContactsForTaskAction(contactsResolver);
            context.selectAllContacts = util.createSelectAction(contactsResolver);
            context.selectOneContact = util.createSelectAction(contactsResolver, function (isEvery) {
                context.isSelectedAllContacts = isEvery;
            });
            context.removeContacts = createRemoveAction(contactsResolver, function (rest) {
                context.task.contacts = rest;
                context.isSelectedAllContacts = false;
            });

            return loadStaticData(context);
        }

        function createStartDateTimeChangeListener(taskResolver) {
            return function (newStartDateTime, isDate) {
                var task = taskResolver();
                if (isDate) {
                    task.endDate = new Date(newStartDateTime.getTime());
                } else {
                    if (task.endDate < newStartDateTime) {
                        task.endDate = new Date(newStartDateTime.getTime());
                    }
                }
            }
        }

        function createEndDateTimeChangeListener(taskResolver) {
            return function (newEndDateTime) {
                var task = taskResolver();
                if (newEndDateTime < task.startDate) {
                    var date = new Date(task.startDate.getTime());
                    date.setHours(task.startDate.getHours());
                    date.setMinutes(task.startDate.getMinutes());
                    task.endDate = date;
                }
            }
        }

        function createAclHandler(getid) {
            return {
                canEdit: true,
                acls: [],
                actions: aclServiceBuilder(getid, taskService)
            };
        }

        function loadStaticData(scope) {
            return $q.all([
                userService.getPublicUsers(),
                taskService.getPriorities(),
                taskService.getStatuses()
            ]).then(function (responses) {
                scope.assigns = responses[0].data;
                scope.priorities = responses[1].data;
                scope.statuses = responses[2].data;
            });
        }

        function createAddContactsForTaskAction(contactsResolver) {
            return function () {
                openSearchContactDialog(contactsResolver(), arguments[arguments.length - 1]);
            };
        }

        function createAddCompaniesForTaskAction(companiesResolver) {
            return function () {
                openSearchCompanyDialog(companiesResolver(), arguments[arguments.length - 1]);
            };
        }

        function createRemoveAction(collectionResolver, handler) {
            var remove = util.createRemoveAction(collectionResolver, handler);
            return function () {
                if (arguments.length > 0 && arguments[arguments.length - 1] && arguments[arguments.length - 1].stopPropagation) {
                    arguments[arguments.length - 1].stopPropagation();// to prevent collapsing of panel
                }
                remove();
            };
        }

        function goToTaskList() {
            $state.go('tasks.list');
        }

        function createSaveOrUpdateAction(taskResolver, aclHandlerResolver) {
            return function () {
                var task = taskResolver();
                var acl = aclHandlerResolver();
                if (task.id) {
                    taskService.update(task).then(function () {
                        if (acl.canEdit) {
                            taskService.updateAcls(task.id, acl.acls);
                        }
                    }).then(goToTaskList);
                } else {
                    taskService.create(task).then(function (response) {
                        var id = response.data;
                        taskService.updateAcls(id, acl.acls).then(goToTaskList);
                    });
                }
            };
        }

        function createAddAction(dialogViewUrl, title, bundle) {
            return function (targetCollection, clickedEvent) {
                clickedEvent.stopPropagation();// to prevent collapsing of panel
                bundle.find();
                dialogService.custom(dialogViewUrl, {
                    title: title,
                    bundle: bundle,
                    size: 'lg',
                    cancelTitle: 'Back',
                    okTitle: 'Ok'
                }).result.then(function (model) {
                    model.bundle.itemsList.forEach(function (item) {
                        if (item.checked && !collections.exists(item, targetCollection)) {
                            item.checked = false;
                            targetCollection.push(angular.copy(item));
                        }
                    });
                });
            };
        }
    }
})();
