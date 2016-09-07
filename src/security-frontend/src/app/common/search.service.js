(function () {
    'use strict';

    angular
        .module('crm.common')
        .factory('searchService', searchService);

    /** @ngInject */
    function searchService(userService, contactService, companyService, taskService, util, $filter) {
        return {
            userPublicMode: getPublicBundle,
            userSecurityMode: getSecurityBundle,
            contactMode: getContactBundle,
            companyMode: getCompanyBundle,
            getTaskBundle: getTaskBundle
        };

        function getPublicBundle() {
            var bundle = createCommonBundle();
            bundle.selectAll = createSelectAllAction(bundle, userPredicate);
            bundle.performSeach = userService.findPublicUsers;
            bundle.sortProperties.userName = {name: 'userName', asc: true, enabled: false};
            return bundle;
        }

        function getSecurityBundle() {
            var bundle = createCommonBundle();
            bundle.selectAll = createSelectAllAction(bundle, userPredicate);
            bundle.performSeach = userService.find;
            bundle.sortProperties.userName = {name: 'userName', asc: true, enabled: false};
            bundle.filter.groupId = null;
            bundle.filter.roleId = null;
            bundle.filter.active = true;
            return bundle;
        }

        function userPredicate(user) {
            return !$filter('isCurrentUser')(user);
        }

        function getContactBundle() {
            var bundle = createCommonBundle();
            bundle.selectAll = createSelectAllAction(bundle);
            bundle.performSeach = contactService.find;
            bundle.sortProperties.address = {name: 'address', asc: true, enabled: false};
            return bundle;
        }

        function getCompanyBundle() {
            var bundle = createCommonBundle();
            bundle.performSeach = companyService.find;
            bundle.selectAll = createSelectAllAction(bundle);
            bundle.sortProperties = {
                name: {name: 'name', asc: true, enabled: true},
                employeeNumber: {name: 'employeeNumberCategory.id', asc: true, enabled: true}
            };
            bundle.filter.sortProperty = bundle.sortProperties.name.name;
            bundle.filter.sortAsc = bundle.sortProperties.name.asc;
            return bundle;
        }

        function getTaskBundle() {
            var bundle = createCommonBundle();
            bundle.selectAll = createSelectAllAction(bundle);
            bundle.performSeach = taskService.find;
            bundle.sortProperties = {
                startDate: {name: 'startDate', asc: false, enabled: true},
                status: {name: 'status', asc: true, enabled: false},
                priority: {name: 'priority', asc: true, enabled: false}
            };
            bundle.filter.sortProperty = bundle.sortProperties.startDate.name;
            bundle.filter.sortAsc = bundle.sortProperties.startDate.asc;
            return bundle;
        }

        function createSelectAllAction(bundle, predicate) {
            return function (checked) {
                bundle.itemsList.forEach(function (item) {
                    if (!predicate || predicate(item)) {
                        item.checked = checked;
                    }
                });
                bundle.isSelectNone = !checkActivationStateTasks(bundle);

            };
        }

        function resetBundleState(bundle) {
            bundle.isSelectedAll = false;
            bundle.isSelectNone = true;
            bundle.itemsList.forEach(function (item) {
                item.checked = false;
            });
        }

        function checkActivationStateTasks(bundle) {
            var counter = 0;

            bundle.itemsList.forEach(function (item) {
                if (item.checked) {
                    counter++;
                }
            });

            return counter > 0;
        }

        function createCommonBundle() {
            var bundle = {};

            var pageSize = 5;

            bundle.isSelectAll = false;
            bundle.isSelectNone = true;

            bundle.itemsList = [];

            bundle.paging = {
                totalCount: 0,
                itemsPerPage: pageSize,
                currentPage: 1,
                visiblePages: 5
            };

            bundle.paging.onPageChanged = function () {
                bundle.filter.from = (bundle.paging.currentPage - 1) * pageSize;
                bundle.find();
            };

            bundle.sortProperties = {
                firstName: {name: 'firstName', asc: true, enabled: true},
                lastName: {name: 'lastName', asc: true, enabled: false},
                email: {name: 'email', asc: true, enabled: false}
            };

            bundle.filter = {
                from: 0,
                count: pageSize,// todo: extract to config
                text: null,
                sortProperty: null,
                sortAsc: true
            };
            bundle.filter.sortProperty = bundle.sortProperties.firstName.name;
            bundle.filter.sortAsc = bundle.sortProperties.firstName.asc;

            bundle.find = function find() {
                resetBundleState(bundle);
                angular.forEach(bundle.filter, function (value, key) {
                    if (angular.isString(value)) {
                        bundle.filter[key] = value ? value : null;
                    }
                });
                bundle.performSeach(bundle.filter).then(function (response) {
                    bundle.itemsList = response.data.data;
                    bundle.paging.totalCount = response.data.totalCount;
                });
            };
            bundle.typing = util.createDelayTypingListener(bundle.find, 500);

            bundle.sortBy = function (property) {
                angular.forEach(bundle.sortProperties, function (sortProperty) {
                    sortProperty.enabled = false;
                });
                property.enabled = true;
                property.asc = !property.asc;
                bundle.filter.sortAsc = property.asc;
                bundle.filter.sortProperty = property.name;
                bundle.find();
            };

            bundle.selectOne = function () {
                bundle.isSelectedAll = bundle.itemsList.every(function (user) {
                    bundle.isSelectNone = !checkActivationStateTasks(bundle);
                    return user.checked;
                });
            };

            return bundle;
        }
    }
})();
