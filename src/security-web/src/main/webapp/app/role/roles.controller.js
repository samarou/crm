/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("RolesController", ["$uibModal", "$timeout", "RoleService", "PrivilegeService", "Collections", "Util",
    function ($uibModal, $timeout, RoleService, PrivilegeService, Collections, Util) {
        "use strict";

        var vm = this;

        RoleService.fetchAll().then(function (response) {
            vm.roleList = response.data;
        });

        PrivilegeService.fetchAll().then(function (response) {
            vm.privileges = response.data;
        });

        vm.filter = {
            text: null,
            sortProperty: null,
            sortAsc: true
        };

        vm.sortProperties = {
            name: {name: "name", asc: true, enabled: true},
            description: {name: "description", asc: true, enabled: false}
        };

        vm.sortBy = function (property) {
            property.enabled = true;
            property.asc = !property.asc;
            vm.filter.sortProperty = property.name;
            vm.filter.sortAsc = property.asc;
        };

        vm.selectAll = function (checked) {
            vm.roleList.forEach(function (role) {
                role.checked = checked;
            });
        };

        vm.selectOne = function () {
            vm.isSelectedAll = vm.roleList.every(function (role) {
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
                });
            }
        }

        function checkPrivilegesOfRole(role) {
            vm.privileges.forEach(function (privilege) {
                privilege.checked = !!Collections.find(privilege, role.privileges);
            });
        }

        function showDialog(model) {
            var modalInstance = $uibModal.open({
                windowTemplateUrl: '/app/common/modal.dialog.template.html',
                templateUrl: '/app/role/role.modal.view.html',
                controller: 'ModalDialogController',
                resolve: {model: model}
            });
            modalInstance.result.then(function (model) {
                update(model.role);
            });
        }
    }]);