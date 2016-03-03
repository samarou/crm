/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("RolesController", ["$q", "RoleService", "PrivilegeService", "DialogService", "Collections", "Util",
    function ($q, RoleService, PrivilegeService, DialogService, Collections, Util) {
        "use strict";

        var vm = this;

        vm.searchText = "";
        vm.pageRoles = [];

        //todo: resolve problem with filtering
        vm.pagingFilterConfig = {
            currentPage: 1,
            itemsPerPage: 10,
            visiblePages: 5,
            totalItems: null,
            privilegeSearchText: null,
            filterObject: {
                name: "",
                description: ""
            },
            sortProperty: "name",
            sortAsc: true
        };
        vm.privilegeObjects = [];

        function fetchAllRoles() {
            RoleService.fetchAll().then(function (response) {
                vm.roleList = response.data;
                vm.pagingFilterConfig.totalItems = vm.roleList.length;
            });
        }

        fetchAllRoles();


        PrivilegeService.fetchAll().then(function (response) {
            var objectTypeList = Object.create(null);
            var privileges = response.data;
            privileges.forEach(function (privilege) {
                if (!(privilege.objectTypeName in objectTypeList)) {
                    objectTypeList[privilege.objectTypeName] = [];
                }
                objectTypeList[privilege.objectTypeName].push({
                    id: privilege.action.id,
                    name: privilege.actionName,
                    privilege: privilege
                });
            });
            vm.privilegeObjects = Collections.sort(Object.keys(objectTypeList)).map(function (objectType) {
                return {
                    objectTypeName: objectType,
                    actions: Collections.sort(objectTypeList[objectType], true, Collections.byProperty("id"))
                };
            });
        });

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
                privilegeObjects: vm.privilegeObjects,
                role: angular.copy(role)
            });
        };

        vm.create = function () {
            checkPrivilegesOfRole({});
            showDialog({
                title: "Create a New Role",
                okTitle: "Add",
                privilegeObjects: vm.privilegeObjects,
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
            vm.privilegeObjects.forEach(function (privilegeObject) {
                privilegeObject.actions.forEach(function (action) {
                    action.privilege.checked = !!Collections.find(action.privilege, role.privileges);
                })
            });
        }

        function showDialog(model) {
            var dialog = DialogService.custom('app/role/role.modal.view.html', model);
            dialog.result.then(function (model) {
                update(model.role);
            });
        }
    }]);