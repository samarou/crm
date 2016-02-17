"use strict";

angular.module("app").controller("UserController", ["$routeParams", "$location", "$q", "UserService", "GroupService", "RoleService", "Collections",
    function ($routeParams, $location, $q, UserService, GroupService, RoleService, Collections) {
        console.log("UserController: " + $routeParams.param);
        var vm = this;

        var loadGroupsPromise = GroupService.fetchAll();
        var loadRolesPromise = RoleService.fetchAll();
        loadGroupsPromise.then(function (response) {
            vm.groups = response.data;
            vm.selectedGroup = vm.groups[0];
        });
        loadRolesPromise.then(function (response) {
            vm.roles = response.data;
            vm.selectedRole = vm.roles[0];
        });

        vm.isCreation = $routeParams.param === "new";
        if (vm.isCreation) {
            vm.formTitle = "Creating a New User";
            vm.actionTitle = "Add";
            vm.action = function (user) { UserService.create(user, successCallback); };
            vm.user = {
                "userName": null,
                "email": null,
                "firstName": null,
                "lastName": null,
                "active": true,
                "roles": [],
                "groups": []
            };
        } else {
            vm.formTitle = "Editing User";
            vm.actionTitle = "Update";
            vm.action = function (user) { UserService.update(user, successCallback); };
            UserService.getById($routeParams.param, function (response) {
                vm.user = response.data;
                $q.all([loadGroupsPromise, loadRolesPromise]).then(function () {
                    vm.groups = Collections.difference(vm.groups, vm.user.groups, Collections.Comparators.BY_ID);
                    vm.roles = Collections.difference(vm.roles, vm.user.roles, Collections.Comparators.BY_ID);
                    vm.selectedGroup = vm.groups[0];
                    vm.selectedRole = vm.roles[0];
                });
            });
        }

        //todo: add directive to encapsulate this login
        vm.deselectGroup = function (group) {
            var index = vm.user.groups.indexOf(group);
            vm.user.groups.splice(index, 1);
            vm.groups.push(group);
            vm.selectedGroup = group;
        };

        vm.deselectRole = function (role) {
            var index = vm.user.roles.indexOf(role);
            vm.user.roles.splice(index, 1);
            vm.roles.push(role);
            vm.selectedRole = role;
        };

        vm.selectGroup = function (group) {
            var index = vm.groups.indexOf(group);
            vm.groups.splice(index, 1);
            vm.user.groups.push(group);
            vm.selectedGroup = vm.groups[0];
        };

        vm.selectRole = function (role) {
            var index = vm.roles.indexOf(role);
            vm.roles.splice(index, 1);
            vm.user.roles.push(role);
            vm.selectedRole = vm.roles[0];
        };

        function successCallback() {
            $location.path("users");
        }
    }]);