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
            active: true,
            sortProperty: null,
            sortAsc: true
        };

        vm.paging = {
            totalPages: 1,
            visiblePages: 5,
            onPageClick: function (pageNumber) {
                vm.filter.from = (pageNumber - 1) * vm.filter.count;
                vm.find(vm.filter);
            }
        };

        vm.sortProperties = {
            firstName: {name: "firstName", asc: true, isUsage: true},
            lastName: {name: "lastName", asc: true, isUsage: false},
            userName: {name: "userName", asc: true, isUsage: false},
            email: {name: "email", asc: true, isUsage: false}
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

        vm.sortBy = function (property) {
            angular.forEach(vm.sortProperties, function (p) { p.isUsage = false; });
            property.isUsage = true;
            property.asc = !property.asc;
            vm.filter.sortAsc = property.asc;
            vm.filter.sortProperty = property.name;
            vm.find(vm.filter);
        };

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

        GroupService.fetchAll().then(function (response) { vm.groups = response.data; });
        RoleService.fetchAll().then(function (response) { vm.roles = response.data; });
    }]);