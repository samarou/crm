angular.module('app').controller('UsersController', ["$location", "$q", "$uibModal", "UserService", "UserBundle", "GroupService", "RoleService", "DialogService", "Collections",
    function ($location, $q, $uibModal, UserService, UserBundle, GroupService, RoleService, DialogService, Collections) {
        "use strict";
        var vm = this;

        GroupService.fetchAll().then(function (response) {
            vm.groups = response.data;
        });
        RoleService.fetchAll().then(function (response) {
            vm.roles = response.data;
        });

        vm.bundle = UserBundle.securedMode();
        vm.bundle.find();

        vm.activate = function (newState) {
            var tasks = [];
            vm.bundle.userList.forEach(function (user) {
                if (user.checked) {
                    if (newState) {
                        tasks.push(UserService.activate(user.id));
                    } else {
                        tasks.push(UserService.deactivate(user.id));
                    }
                }
            });
            $q.all(tasks).then(vm.bundle.find);
        };

        vm.edit = function (user) {
            checkGroupsAndRolesWhichUserHas(user);
            showDialog({
                title: "Editing User",
                okTitle: "Update",
                groups: vm.groups,
                roles: vm.roles,
                user: angular.copy(user)
            });
        };

        vm.add = function () {
            var user = {};
            user.active = true;
            checkGroupsAndRolesWhichUserHas(user);
            showDialog({
                title: "Create User",
                okTitle: "Add",
                groups: vm.groups,
                roles: vm.roles,
                user: user
            });
        };

        function update(user) {
            initUserWithCheckedGroupsAndRoles(user);
            if (user.id) {
                var originUser = vm.bundle.userList.find(function (u) {
                    return u.id === user.id
                });
                angular.copy(user, originUser);
                UserService.update(user);
            } else {
                UserService.create(user).then(function (response) {
                    user.id = response.data;
                    vm.bundle.userList.push(user);
                });
            }
        }

        function showDialog(model) {
            var dialog = DialogService.custom('app/user/user.modal.view.html', model);
            dialog.result.then(function (model) {
                update(model.user);
            });
        }

        function checkGroupsAndRolesWhichUserHas(user) {
            vm.groups.forEach(function (group) {
                group.checked = !!Collections.find(group, user.groups);
            });
            vm.roles.forEach(function (role) {
                role.checked = !!Collections.find(role, user.roles);
            });
        }

        function initUserWithCheckedGroupsAndRoles(user) {
            user.groups = vm.groups.filter(function (group) {
                return group.checked;
            });
            user.roles = vm.roles.filter(function (role) {
                return role.checked;
            });
        }
    }]);