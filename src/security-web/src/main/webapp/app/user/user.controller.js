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
            });
        }
        function successCallback() {
            $location.path("users");
        }
    }]);