(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('RolesEditController', RolesEditController);


	/** @ngInject */
	function RolesEditController(roleService, $stateParams, $state, rolePrivilegeService) {
		var vm = this;
		vm.role = {};
		vm.submitText = 'Save';
		vm.title = 'Edit role';
		vm.objectTypes = [];


		roleService.get($stateParams.id).then(function (response) {
			vm.role = response.data;
		}).then(function () {
			rolePrivilegeService.getObjectTypes(vm).then(function () {
				rolePrivilegeService.checkPrivilegesOfRole(vm);
			});
		});


		vm.submit = function () {
			vm.role.privileges = [];
			vm.objectTypes.forEach(function (objectType) {
				objectType.actions.forEach(function (action) {
					if (action.privilege.checked) {
						vm.role.privileges.push(action.privilege);
					}
				});
			});
			roleService.update(vm.role).then(function () {
				$state.go('roles.list');
			})
		};

		vm.cancel = function () {
			$state.go('roles.list');
		};


	}
})();
