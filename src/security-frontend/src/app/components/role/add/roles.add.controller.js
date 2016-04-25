(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('RolesAddController', RolesAddController);


	function RolesAddController(roleService, rolePrivilegeService, $state) {
		var vm = this;
		vm.role = {};
		vm.submitText = 'Add';
		vm.title = 'Add role';
		vm.objectTypes = [];

		rolePrivilegeService.getObjectTypes(vm);


		vm.submit = function () {
			vm.role.privileges = [];
			rolePrivilegeService.getPrivilegesOfRole(vm);
			roleService.create(vm.role).then(function () {
				$state.go('roles.list');
			})
		};

		vm.cancel = function () {
			$state.go('roles.list');
		};

	}
})();
