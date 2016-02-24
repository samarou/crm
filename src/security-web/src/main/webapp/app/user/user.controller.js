angular.module("app").controller("UserController", ["$routeParams", "$location", "$q", "UserService", "GroupService", "RoleService", "Collections",
    function ($routeParams, $location, $q, UserService, GroupService, RoleService, Collections) {
        "use strict";

        var vm = this;
        var loadGroupsPromise = GroupService.fetchAll();
        var loadRolesPromise = RoleService.fetchAll();

        loadGroupsPromise.then(function (response) {
            vm.groups = response.data;
        });
        loadRolesPromise.then(function (response) {
            vm.roles = response.data;
        });

        vm.isCreation = $routeParams.param === "new";
        if (vm.isCreation) {
            vm.formTitle = "Creating a New User";
            vm.actionTitle = "Add";
            vm.action = function(user){
                initUserWithGroupsAndRoles(user);
                UserService.create(user);
                $location.path("users");
            };
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
            vm.action = function(user){
                initUserWithGroupsAndRoles(user);
                UserService.update(user);
                $location.path("users");
            };
            UserService.getById($routeParams.param, function (response) {
                vm.user = response.data;
                $q.all([loadGroupsPromise, loadRolesPromise]).then(function () {
                    vm.groups.forEach(function (group) {
                        group.checked = !!Collections.find(group, vm.user.groups);
                    });
                    vm.roles.forEach(function (role) {
                        role.checked = !!Collections.find(role, vm.user.roles);
                    });
                });
            });
        }

        function initUserWithGroupsAndRoles(user){
            user.groups = vm.groups.filter(function (group) {
                return group.checked;
            });
            user.roles = vm.roles.filter(function (role) {
                return role.checked;
            });
        }
    }]);