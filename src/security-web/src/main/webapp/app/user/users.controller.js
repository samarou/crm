angular.module('app').controller('UsersController', ["$location", "$uibModal", "UserService", "GroupService", "RoleService", "Collections",
    function ($location, $uibModal, UserService, GroupService, RoleService, Collections) {
        "use strict";
        var vm = this;

        GroupService.fetchAll().then(function (response) {
            vm.groups = response.data;
        });
        RoleService.fetchAll().then(function (response) {
            vm.roles = response.data;
        });

        var defaultPageSize = 5;

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
            firstName: {name: "firstName", asc: true, enabled: true},
            lastName: {name: "lastName", asc: true, enabled: false},
            userName: {name: "userName", asc: true, enabled: false},
            email: {name: "email", asc: true, enabled: false}
        };

        vm.find = function find(filter) {
            //nulling, to prevent empty parameters in url
            angular.forEach(filter, function (value, key) {
                if (typeof(value) !== "string") return;
                filter[key] = !!value ? value : null;
            });
            UserService.find(filter, function (response) {
                vm.userList = response.data.data;
                var totalCount = response.data.totalCount;
                var totalPages = Math.ceil(totalCount / vm.filter.count) || 1;
                vm.paging.totalCount = totalCount;
                vm.paging.totalPages = totalPages;
                vm.paging.visiblePages = totalPages;
                vm.isSelectedAll = false;
                vm.selectAll(vm.isSelectedAll);
            });
        };

        vm.sortBy = function (property) {
            angular.forEach(vm.sortProperties, function (sortProperty) {
                sortProperty.enabled = false;
            });
            property.enabled = true;
            property.asc = !property.asc;
            vm.filter.sortAsc = property.asc;
            vm.filter.sortProperty = property.name;
            vm.find(vm.filter);
        };

        vm.isSelectedAll = false;
        vm.totalSelected = 0;

        vm.selectAll = function (checked) {
            vm.userList.forEach(function (user) {
                user.checked = checked;
            });
        };

        vm.selectOneByClick = function (user) {
            user.checked = !user.checked;
            vm.selectOne(user);
        };

        vm.selectOne = function (user, $event) {
            vm.totalSelected += user.checked ? 1 : -1;
            if (user.checked && !vm.isSelectedAll) {
                vm.isSelectedAll = vm.totalSelected === vm.userList.length;
            } else if (vm.isSelectedAll) {
                vm.isSelectedAll = false;
            }
            if (!!$event) $event.stopPropagation();//to exclude raising event of clicking by row(it's parent element)
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
                okTitle: "Update",
                groups: vm.groups,
                roles: vm.roles,
                user: user
            });
        };

        function update(user) {
            initUserWithCheckedGroupsAndRoles(user);
            if (user.id) {
                var originUser = vm.userList.find(function (u) {
                    return u.id === user.id
                });
                angular.copy(user, originUser);
                UserService.update(user);
            } else {
                UserService.create(user, function (response) {
                    user.id = response.data;
                    vm.userList.push(user);
                });
            }
        }

        function showDialog(model) {
            var modalInstance = $uibModal.open({
                windowTemplateUrl: '/app/common/modal.dialog.template.html',
                templateUrl: 'app/user/user.modal.view.html',
                controller: 'ModalDialogController',
                resolve: {model: model}
            });
            modalInstance.result.then(function (model) {
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

        function initUserWithCheckedGroupsAndRoles(user){
            user.groups = vm.groups.filter(function (group) {
                return group.checked;
            });
            user.roles = vm.roles.filter(function (role) {
                return role.checked;
            });
        }

        vm.activate = function (newState) {
            vm.userList.forEach(function (user) {
                if (user.checked) {
                    user.active = newState;
                    if (newState) {
                        UserService.activate(user.id);
                    } else {
                        UserService.deactivate(user.id);
                    }
                }
            });
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
    }]);