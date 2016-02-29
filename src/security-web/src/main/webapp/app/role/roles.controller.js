/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("RolesController", ["$uibModal", "$filter", "RoleService", "PrivilegeService", "DialogService", "Collections",
    function ($uibModal, $filter, RoleService, PrivilegeService, DialogService, Collections) {
        "use strict";

        var vm = this;

        vm.sortProperties = {
            name: {name: "name", asc: true, enabled: true},
            description: {name: "description", asc: true, enabled: false}
        };
        vm.searchText = "";
        vm.pageRoles = [];

        vm.orderFilterPageConfig = {
            currentPage: 1,
            itemsPerPage: 10,
            visiblePages: 5,
            totalItems: null,
            filterObject: {
                name: "",
                description: ""
            },
            sortProperty: vm.sortProperties.name.name,
            sortAsc: vm.sortProperties.name.asc
        };


        RoleService.fetchAll().then(function (response) {
            vm.roleList = response.data;
            vm.orderFilterPageConfig.totalItems = vm.roleList.length;
        });

        PrivilegeService.fetchAll().then(function (response) {
            vm.privileges = response.data;
        });

        vm.updateFilterObject = function () {
            vm.orderFilterPageConfig.filterObject.name = vm.searchText;
            vm.orderFilterPageConfig.filterObject.description = vm.searchText;
        };

        vm.sortBy = function (property) {
            angular.forEach(vm.sortProperties, function (sortProperty) {
                sortProperty.enabled = false;
            });
            property.enabled = true;
            property.asc = !property.asc;
            vm.orderFilterPageConfig.sortProperty = property.name;
            vm.orderFilterPageConfig.sortAsc = property.asc;
        };

        vm.selectAll = function (checked) {
            if (checked) {
                vm.pageRoles.forEach(function (role) {
                    role.checked = true;
                });
            } else {
                vm.roleList.forEach(function (role) {
                    role.checked = false;
                });
            }
        };

        vm.selectOne = function () {
            vm.isSelectedAll = vm.pageRoles.every(function (role) {
                return role.checked;
            });
        };

        vm.edit = function (role) {
            checkPrivilegesOfRole(role);
            showDialog({
                title: "Editing Role",
                okTitle: "Update",
                privileges: vm.privileges,
                role: angular.copy(role)
            });
        };

        vm.create = function () {
            checkPrivilegesOfRole({});
            showDialog({
                title: "Create a New Role",
                okTitle: "Add",
                privileges: vm.privileges,
                role: {}
            });
        };

        vm.remove = function () {
            vm.roleList = vm.roleList.filter(function (role) {
                return !role.checked;
            });
            vm.orderFilterPageConfig.totalItems = vm.roleList.length;
            vm.isSelectedAll = false;
        };

        function update(role) {
            role.privileges = vm.privileges.filter(function (privilege) {
                return privilege.checked;
            });
            if (role.id) {
                var originRole = vm.roleList.find(function (r) {
                    return r.id === role.id
                });
                angular.copy(role, originRole);
                RoleService.update(role);
            } else {
                RoleService.create(role).then(function (response) {
                    role.id = response.data;
                    vm.roleList.push(role);
                    vm.orderFilterPageConfig.totalItems = vm.roleList.length;
                });
            }
        }

        function checkPrivilegesOfRole(role) {
            vm.privileges.forEach(function (privilege) {
                privilege.checked = !!Collections.find(privilege, role.privileges);
            });
        }

        function showDialog(model) {
            var dialog = DialogService.custom('app/role/role.modal.view.html', model);
            dialog.result.then(function (model) {
                update(model.role);
            });
        }
    }]);