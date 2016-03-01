/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("RolesController", ["$q", "RoleService", "PrivilegeService", "DialogService", "Collections",
    function ($q, RoleService, PrivilegeService, DialogService, Collections) {
        "use strict";

        var vm = this;

        vm.sortProperties = {
            name: {name: "name", asc: true, enabled: true},
            description: {name: "description", asc: true, enabled: false}
        };
        vm.searchText = "";
        vm.pageRoles = [];

        //todo: resolve problem with filtering
        vm.pagingFilterConfig = {
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

        function fetchAllRoles() {
            RoleService.fetchAll().then(function (response) {
                vm.roleList = response.data;
                vm.pagingFilterConfig.totalItems = vm.roleList.length;
            });
        }

        fetchAllRoles();

        PrivilegeService.fetchAll().then(function (response) {
            vm.privileges = response.data;
        });

        vm.sortBy = function (property) {
            angular.forEach(vm.sortProperties, function (sortProperty) {
                sortProperty.enabled = false;
            });
            property.enabled = true;
            property.asc = !property.asc;
            vm.pagingFilterConfig.sortProperty = property.name;
            vm.pagingFilterConfig.sortAsc = property.asc;
        };

        vm.updateFilterObject = function () {
            vm.pagingFilterConfig.filterObject.name = vm.searchText;
            vm.pagingFilterConfig.filterObject.description = vm.searchText;
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
            //DialogService.
            var tasks = [];
            vm.pageRoles.forEach(function (role) {
                if (role.checked) {
                    tasks.push(RoleService.remove(role.id))
                }
            });
            $q.all(tasks).then(fetchAllRoles);
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
                    vm.pagingFilterConfig.totalItems = vm.roleList.length;
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