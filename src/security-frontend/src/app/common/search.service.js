(function () {
    'use strict';

    angular
        .module('crm.common')
        .factory('searchService', searchService);

    /** @ngInject */
    function searchService(userService, contactService, util, $filter) {
        return {
            userPublicMode: getPublicBundle,
            userSecurityMode: getSecurityBundle,
            contactMode: getContactBundle
        };

        function getPublicBundle() {
            var bundle = createCommonBundle();
            bundle.selectAll = createSelectAllAction(bundle, userSkipPredicate);
            bundle.performSeach = userService.findPublicUsers;
            bundle.sortProperties.userName = {name: 'userName', asc: true, enabled: false};
            return bundle;
        }

        function getSecurityBundle() {
            var bundle = createCommonBundle();
            bundle.selectAll = createSelectAllAction(bundle, userSkipPredicate);
            bundle.performSeach = userService.find;
            bundle.sortProperties.userName = {name: 'userName', asc: true, enabled: false};
            bundle.filter.groupId = null;
            bundle.filter.roleId = null;
            bundle.filter.active = true;
            return bundle;
        }

        function userSkipPredicate(user) {
            return !$filter('isCurrentUser')(user);
        }

        function getContactBundle() {
            var bundle = createCommonBundle();
            bundle.selectAll = createSelectAllAction(bundle);
            bundle.performSeach = contactService.find;
            bundle.sortProperties.address = {name: 'address', asc: true, enabled: false};
            return bundle;
        }

        function createSelectAllAction(bundle, skipPredicate) {
            return function (checked) {
                bundle.itemsList.forEach(function (item) {
                    if (!skipPredicate || skipPredicate(item)) {
                        item.checked = checked;
                    }
                });
            }
        }

        function createCommonBundle() {
            var bundle = {};

            var pageSize = 5;

            bundle.isSelectAll = false;
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
                // todo: fix angular.isString and !!
                /*eslint-disable */
                // nulling, to prevent empty parameters in url
                angular.forEach(bundle.filter, function (value, key) {
                    if (typeof(value) !== 'string') {
                        return;
                    }
                    bundle.filter[key] = !!value ? value : null;
                });
                /*eslint-enable */
                bundle.performSeach(bundle.filter).then(function (response) {
                    bundle.itemsList = response.data.data;
                    var totalCount = response.data.totalCount;
                    var totalPages = Math.ceil(totalCount / bundle.filter.count) || 1;
                    bundle.paging.totalCount = totalCount;
                    bundle.paging.visiblePages = totalPages;
                    bundle.isSelectedAll = false;
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
                $log.debug(' current user');
                bundle.isSelectedAll = bundle.itemsList.every(function (user) {
                    return user.checked;
                });
            };

            return bundle;
        }
    }
})();
