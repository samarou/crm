/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("RolesController", ["RoleService", "$uibModal", "PrivilegeService", "Collections",
    function (RoleService, $uibModal, PrivilegeService, Collections) {
        "use strict";

        var vm = this;

        RoleService.fetchAll().then(function (response) {
            vm.roleList = response.data;
        });

        PrivilegeService.fetchAll().then(function (response) {
            vm.privileges = response.data;
        });

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
                var originGroup = vm.roleList.find(function (r) {
                    return r.id === role.id
                });
                angular.copy(role, originGroup);
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
                templateUrl: '/app/role/role.view.modal.html',
                controller: 'ModalDialogController',
                resolve: {model: model}
            });
            modalInstance.result.then(function (model) {
                update(model.role);
            });
        }
    }]);