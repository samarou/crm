'use strict';

angular.module('app').controller('UsersController', ["$location", "UserService", "GroupService", "RoleService",
    function ($location, UserService, GroupService, RoleService) {
        "use strict";

        var defaultPageSize = 5;

        var vm = this;
        vm.filter = {
            from: 0,
            count: defaultPageSize,//todo: extract to config
            text: null,
            groupId: null,
            roleId: null,
            active: true
        };

        vm.paging = {
            totalPages: 0,
            visiblePages: 0,
            onPageClick: function (pageNumber) {
                vm.filter.from = (pageNumber - 1) * vm.filter.count;
                vm.find(vm.filter);
            }
        };

        vm.find = function find(filter) {
            //nulling, to prevent empty parameters in url
            angular.forEach(filter, function (value, key) {
                if (typeof(value) !== "string") return;
                filter[key] = !!value ? value : null;
            });
            UserService.find(filter, function (response) {
                vm.userList = response.data.data;
                var quantity = response.data.totalCount;
                var totalPages = Math.ceil(quantity / vm.filter.count) || 1;
                vm.paging.totalPages = totalPages;
                vm.paging.visiblePages = totalPages;
            });
        };
        vm.find(vm.filter);

        var keyTimer;

        vm.keyDown = function () {
            clearTimeout(keyTimer);
        };
        vm.keyUp = function () {
            clearTimeout(keyTimer);
            keyTimer = setTimeout(function () {
                vm.find(vm.filter);
            }, 500);
        };

        GroupService.fetchAll().then(function (response) {
            vm.groups = response.data;
        });
        RoleService.fetchAll().then(function (response) {
            vm.roles = response.data;
        });
    }]);