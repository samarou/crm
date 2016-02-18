'use strict';

angular.module('app').controller('UserListController', ["$location", "UserService", "GroupService", "RoleService",
    function ($location, UserService, GroupService, RoleService) {
        var vm = this;

        GroupService.fetchAll().then(function (response) {
            vm.groups = response.data;
        });
        RoleService.fetchAll().then(function (response) {
            vm.roles = response.data;
        });
        UserService.getAll(function (response) {
            vm.userList = response.data;
        });
        UserService.count(function (response) {
            var defaultPageSize = 5;
            var quantity = response.data;
            var totalPages = Math.ceil(quantity / defaultPageSize);
            console.log("quantity: " + quantity + ", totalPages: " + totalPages);
            vm.paging = {
                totalPages: totalPages,
                visiblePages: totalPages > 5 ? 5 : totalPages,
                onPageClick: function (pageNumber) {
                    console.log("pageNumber: " + pageNumber);
                    UserService.find({from: (pageNumber - 1) * defaultPageSize, count: defaultPageSize}, function (response) {
                        vm.userList = response.data;
                    });
                }
            };
        });

        vm.any = null;

        vm.filter = {
            text: null,
            groupId: null,
            roleId: null,
            active: true
        };

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