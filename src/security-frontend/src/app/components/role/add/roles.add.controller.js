(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('RolesAddController', RolesAddController);


	function RolesAddController(RoleService, RolePrivilegeService, $state) {
		var vm = this;
		vm.role = {};
		vm.submitText = 'Add';
		vm.title = 'Add role';
		vm.objectTypes = [];

		RolePrivilegeService.getObjectTypes(vm);


		vm.submit = function () {
			vm.role.privileges = [];
			RolePrivilegeService.getPrivilegesOfRole(vm);
			RoleService.create(vm.role).then(function () {
				$state.go('roles.list');
			})
		};

		vm.cancel = function () {
			$state.go('roles.list');
		};

	}
})();
