'use strict';

angular.module('app').controller('UserListController', ["$location", "UserService", "GroupService", "RoleService",
    function ($location, UserService, GroupService, RoleService) {
        var vm = this;
        var defaultPageSize = 5;
        vm.filter = {
            from: 0,
            count: defaultPageSize,//todo: extract to config
            text: null,
            groupId: null,
            roleId: null,
            active: true
        };

        vm.find = function find(filter) {
            angular.forEach(filter, function (value, key) {
                if (typeof(value) !== "string") return;
                filter[key] = !!value ? value : null;
            });
            UserService.find(filter, function (response) { vm.userList = response.data; });
        };
        vm.find(vm.filter);

        GroupService.fetchAll().then(function (response) { vm.groups = response.data; });
        RoleService.fetchAll().then(function (response) { vm.roles = response.data; });
        UserService.count(function (response) {
            var quantity = response.data;
            var totalPages = Math.ceil(quantity / vm.filter.count);
            vm.paging = {
                totalPages: totalPages,
                visiblePages: totalPages > defaultPageSize ? defaultPageSize : totalPages,
                onPageClick: function (pageNumber) {
                    vm.filter.from = (pageNumber - 1) * vm.filter.count;
                    vm.find(vm.filter);
                }
            };
        });
    }]);