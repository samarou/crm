/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("RolesController", ["$uibModal", "$filter", "RoleService", "PrivilegeService", "Collections",
    function ($uibModal, $filter, RoleService, PrivilegeService, Collections) {
        "use strict";

        var vm = this;

        RoleService.fetchAll().then(function (response) {
            vm.roleList = response.data;

            vm.paging = {
                totalItems: vm.roleList.length,
                currentPage: 1,
                itemsPerPage: 10,
                visiblePages: 5,
                outFilterResult: null
            }
        });

        PrivilegeService.fetchAll().then(function (response) {
            vm.privileges = response.data;
        });

        vm.sortProperties = {
            name: {name: "name", asc: true, enabled: true},
            description: {name: "description", asc: true, enabled: false}
        };

        vm.filter = {
            text: "",
            filterObject: {
                name: "",
                description: ""
            },
            sortProperty: vm.sortProperties.name.name,
            sortAsc: vm.sortProperties.name.asc
        };

        vm.updateFilterObject = function () {
            vm.filter.filterObject.name = vm.filter.text;
            vm.filter.filterObject.description = vm.filter.text;
        };

        vm.sortBy = function (property) {
            angular.forEach(vm.sortProperties, function (sortProperty) {
                sortProperty.enabled = false;
            });
            property.enabled = true;
            property.asc = !property.asc;
            vm.filter.sortProperty = property.name;
            vm.filter.sortAsc = property.asc;
        };

        vm.selectAll = function (checked) {
            if (checked) {
                vm.paging.outFilterResult.forEach(function (role) {
                    role.checked = true;
                });
            } else {
                vm.roleList.forEach(function (role) {
                    role.checked = false;
                });
            }
        };

        vm.selectOne = function () {
            vm.isSelectedAll = vm.paging.outFilterResult.every(function (role) {
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
            vm.paging.totalItems = vm.roleList.length;
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
                    vm.paging.totalItems = vm.roleList.length;
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