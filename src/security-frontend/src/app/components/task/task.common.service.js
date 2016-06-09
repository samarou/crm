/**
 * Created by yauheni.putsykovich on 01.06.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .service('taskCommonService', taskCommonService);

    /** @ngInject */
    function taskCommonService($log, taskService, searchService, $state, $q, userService, dialogService, collections, util) {
        var openSearchContactDialog = createAddAction('app/components/task/tabs/contacts/search-contact-dialog.view.html', 'Add Contact for Task', searchService.contactMode());
        var openSearchCompanyDialog = createAddAction('app/components/task/tabs/contacts/search-company-dialog.view.html', 'Add Company for Task', searchService.companyMode());
        var datepickerOptions = {
        };

        return {
            initContext: initContext
        };

        function initContext(context) {
            function taskResolver() {
                return context.task;
            }

            function contactsResolver() {
                return context.task.contacts = context.task.contacts || [];
            }

            function companiesResolver() {
                return context.task.companies = context.task.companies || [];
            }

            context.datepickerOptions = datepickerOptions;

            context.submit = createSubmitAction(taskResolver);
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
                if (arguments.length > 0 && arguments[arguments.length - 1]) {
                    arguments[arguments.length - 1].stopPropagation();// to prevent collapsing of panel
                }
                remove();
            };
        }

        function goToTaskList() {
            $state.go('tasks.list');
        }

        function createSubmitAction(taskResolver) {
            return function () {
                var task = taskResolver();
                if (task.id) {
                    taskService.update(task).then(goToTaskList);
                } else {
                    taskService.create(task).then(goToTaskList);
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
