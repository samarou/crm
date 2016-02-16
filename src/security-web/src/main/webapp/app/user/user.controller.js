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
                        name:"viewers",
                        description: "viewers group"
                    }
                ];
            });
        }

        vm.unselectGroup = function (group) {
            var index = vm.user.groups.indexOf(group);
            var b = vm.user.groups;
            vm.user.groups.splice(index, 1);
            vm.groups.push(group);
        };

        vm.selectGroup = function (group) {
            var index = vm.groups.indexOf(group);
            vm.groups.splice(index, 1);
            vm.user.groups.push(group);
        };

        vm.groups = [
            {
                id: 2,
                name: "managers",
                description: "managers"
            }
        ];
        vm.selectedGroup = vm.groups[0];

        function successCallback() {
            $location.path("users");
        }
    }]);