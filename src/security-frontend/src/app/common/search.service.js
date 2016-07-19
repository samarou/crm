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
            var params = {
                itemsPerPage: 24,
                currentPage: 3,
                text: 'p'
            };
            var bundle = createCommonBundle(params);
            bundle.selectAll = createSelectAllAction(bundle);
            bundle.performSeach = contactService.find;
            console.log('4', bundle.paging.currentPage);

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
                startDate: {name: 'startDate', asc: true, enabled: true},
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
            };
        }

        function resetBundleState(bundle) {
            bundle.isSelectedAll = false;
            bundle.itemsList.forEach(function (item) {
                item.checked = false;
            });
        }

        function createCommonBundle(params) {
            var bundle = {};
            console.log(params);
            var itemsPerPage = params && params.itemsPerPage || 10;
            var currentPage = params && params.currentPage || 1;
            var initialSearchText = params && params.text || null;

            bundle.isSelectAll = false;
            bundle.itemsList = [];

            bundle.paging = {
                totalCount: 0,
                itemsPerPage: itemsPerPage,
                currentPage: currentPage,
                visiblePages: 5
            };
            console.log('1', bundle.paging.currentPage);
            bundle.paging.onPageChanged = function () {
                console.log('somewere1_1', bundle.paging.currentPage);
                bundle.filter.from = (bundle.paging.currentPage - 1) * itemsPerPage;
                bundle.find();
                console.log('somewere1_2', bundle.paging.currentPage);
            };

            bundle.sortProperties = {
                firstName: {name: 'firstName', asc: true, enabled: true},
                lastName: {name: 'lastName', asc: true, enabled: false},
                email: {name: 'email', asc: true, enabled: false}
            };

            bundle.filter = {
                from: 0,
                count: itemsPerPage,
                text: initialSearchText,
                sortProperty: null,
                sortAsc: true
            };
            bundle.filter.sortProperty = bundle.sortProperties.firstName.name;
            bundle.filter.sortAsc = bundle.sortProperties.firstName.asc;
            console.log('2', bundle.paging.currentPage);

            bundle.find = function find() {
                console.log('somewere2_1', bundle.paging.currentPage);
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
                console.log('somewere2_2', bundle.paging.currentPage);
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
                    return user.checked;
                });
            };
            console.log('3', bundle.paging.currentPage);

            return bundle;
        }
    }
})();
