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
            function contactsResolver() {
                return context.task.contacts = context.task.contacts || [];
            }

            function companiesResolver() {
                return context.task.companies = context.task.companies || [];
            }

            context.onTimeless = createOnTimeless(context);
            context.onStartDateTimeChange = createStartDateTimeChangeListener(context);
            context.onEndDateTimeChange = createEndDateTimeChangeListener(context);
            context.aclHandler = createAclHandler(context);
            context.submit = createSaveOrUpdateAction(context);
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

        function createOnTimeless(context) {
            return function () {
                if (context.canEdit) {
                    context.canEditDateTime = !context.timeless;
                }
            }
        }

        function createStartDateTimeChangeListener(context) {
            return function (newStartDateTime, isDate) {
                var task = context.task;
                if (isDate) {
                    task.endDate = new Date(newStartDateTime.getTime());
                    task.endDate.setHours(task.endDate.getHours() + 1);
                } else {
                    if (task.endDate <= newStartDateTime) {
                        task.endDate = new Date(newStartDateTime.getTime());
                        task.endDate.setHours(task.endDate.getHours() + 1);
                    }
                }
            }
        }

        function createEndDateTimeChangeListener(context) {
            return function (newEndDateTime) {
                var task = context.task;
                if (newEndDateTime <= task.startDate) {
                    var date = new Date(task.startDate.getTime());
                    date.setHours(task.startDate.getHours() + 1);
                    date.setMinutes(task.startDate.getMinutes());
                    task.endDate = date;
                }
            }
        }

        function createAclHandler(context) {
            return {
                canEdit: true,
                acls: [],
                actions: aclServiceBuilder(function () {
                    return context.task.id;
                }, taskService)
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

        function createSaveOrUpdateAction(context) {
            return function () {
                var acl = context.aclHandler;
                var task = context.task;
                if (!context.canEditDateTime) {
                    task.startDate = null;
                    task.endDate = null;
                }
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
