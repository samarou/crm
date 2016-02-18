'use strict';

angular.module('app').controller('UserListController', ["$location", "UserService", "GroupService", "RoleService",
    function ($location, UserService, GroupService, RoleService) {
        var vm = this;

        vm.filter = {
            from: 0,
            count: 5,
            text: null,
            groupId: null,
            roleId: null,
            active: true
        };

        GroupService.fetchAll().then(function (response) { vm.groups = response.data; });
        RoleService.fetchAll().then(function (response) { vm.roles = response.data; });
        UserService.find(vm.filter, function (response) { vm.userList = response.data; });
        UserService.count(function (response) {
            var quantity = response.data;
            var totalPages = Math.ceil(quantity / vm.filter.count);
            console.log("quantity: " + quantity + ", totalPages: " + totalPages);
            vm.paging = {
                totalPages: totalPages,
                visiblePages: totalPages > 5 ? 5 : totalPages,
                onPageClick: function (pageNumber) {
                    console.log("pageNumber: " + pageNumber);
                    vm.filter.from = (pageNumber - 1) * vm.filter.count;
                    UserService.find(vm.filter, function (response) { vm.userList = response.data; });
                }
            };
        });

        vm.find = function find(filter) {
            angular.forEach(filter, function (value, key) {
                if (typeof(value) === "boolean") return;
                filter[key] = !!value ? value : null;
            });
            UserService.find(filter, function (response) {
                vm.userList = response.data;
            });
        };
    }]);