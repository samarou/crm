'use strict';

angular.module('app').controller('UserListController', ["$location", "UserService", "GroupService", "RoleService",
    function ($location, UserService, GroupService, RoleService) {
        var vm = this;

        vm.any = null;

        vm.filter = {
            text: null,
            groupId: null,
            roleId: null,
            active: true
        };

        vm.paging = {
            onchange: function (page, pageSize) {
                console.log("page: " + page + ", pageSize: " + pageSize);
            }
        };

        vm.find = function find(filter) {
            angular.forEach(filter, function (value, key) {
                if(typeof(value) === "boolean") return;
                filter[key] = !!value ? value : null;
            });
            console.log(JSON.stringify(filter));
            UserService.find(filter, function (response) {
                vm.userList = response.data;
            });
        };

        UserService.getAll(function (response) { vm.userList = response.data; });
        GroupService.fetchAll().then(function (response) { vm.groups = response.data; });
        RoleService.fetchAll().then(function (response) { vm.roles = response.data; });
    }]);