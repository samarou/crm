"use strict";

angular.module("app").controller("UserController", ["UserService", "$routeParams", "$location",
    function (UserService, $routeParams, $location) {
        console.log("UserController: " + $routeParams.param);
        var vm = this;
        vm.isCreation = $routeParams.param === "new";
        if (vm.isCreation) {
            vm.formTitle = "Creating a New User";
            vm.actionTitle = "Add";
            vm.action = function (user) {
                UserService.CreateUser(user, successCallback);
            };
        } else {
            vm.formTitle = "Editing User";
            vm.actionTitle = "Update";
            vm.action = function (user) {
                UserService.UpdateUser(user, successCallback);
            };
            UserService.GetByUsername($routeParams.param, function (response) {
                vm.user = response.data;
                vm.user.groups = [
                    {
                        id: 1,
                        name: "viewers",
                        description: "viewers group"
                    }
                ];
                vm.user.roles = [
                    {
                        id: 1,
                        name: "ROOT",
                        description: "ROOT ROLE",
                        parent: null
                    },
                    {
                        id: 2,
                        name: "USER",
                        description: "USER ROLE",
                        parent: 1
                    }
                ];
            });
        }

        vm.unselectGroup = function (group) {
            var index = vm.user.groups.indexOf(group);
            vm.user.groups.splice(index, 1);
            vm.groups.push(group);
            vm.selectedGroup = group;
        };

        vm.selectGroup = function (group) {
            var index = vm.groups.indexOf(group);
            vm.groups.splice(index, 1);
            vm.user.groups.push(group);
            vm.selectedGroup = vm.groups[0];
        };

        vm.unselectRole = function (role) {
            var index = vm.user.roles.indexOf(role);
            vm.user.roles.splice(index, 1);
            vm.roles.push(role);
            vm.selectedRole = role;
        };

        vm.selectRole = function (role) {
            var index = vm.roles.indexOf(role);
            vm.roles.splice(index, 1);
            vm.user.roles.push(role);
            vm.selectedRole = vm.roles[0];
        };

        vm.groups = [
            {
                id: 2,
                name: "managers",
                description: "managers"
            }
        ];
        vm.selectedGroup = vm.groups[0];

        vm.roles = [
            {
                id: 3,
                name: "ADMIN",
                description: "ADMIN ROLE",
                parent: 2
            },
            {
                id: 4,
                name: "GUEST",
                description: "GUEST ROLE",
                parent: 1
            }
        ];
        vm.selectedRole = vm.roles[0];

        function successCallback() {
            $location.path("users");
        }
    }]);