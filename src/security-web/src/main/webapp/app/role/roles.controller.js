/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("RolesController", ["RoleService",
    function (RoleService) {
        "use strict";
        var vm = this;
        RoleService.fetchAll().then(function (response) {
            vm.roleList = response.data;
        });
        vm.partials = {
            roleModalView: "/app/role/role.modal.view.html"
        };
        //vm.role - it's transfer object to pass data to modal and back
        vm.edit = function (role) {
            vm.role = {};
            angular.copy(role, vm.role);
            vm.viewTitle = "Editing Role";
            vm.actionTitle = "Update";
            vm.action = update;
        };
        vm.create = function () {
            vm.role = {};
            vm.viewTitle = "Create a New Role";
            vm.actionTitle = "Add";
            vm.action = add;
        };

        function add(role) {
            RoleService.create(role, function (response) {
                role.id = response.data;
                vm.roleList.push(role);
            });
            closeModalView();
        }

        function update(role) {
            var originRole = vm.roleList.find(function (r) {
                return r.id === role.id
            });
            angular.copy(role, originRole);
            RoleService.update(role, function () {
                
            });
            closeModalView();
        }

        function closeModalView() {
            $("#modal-close-button").trigger("click");//todo: need improve(find other solution)
        }
    }]);