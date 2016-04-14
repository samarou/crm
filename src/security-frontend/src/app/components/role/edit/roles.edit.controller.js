(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('RolesEditController', RolesEditController);


	/** @ngInject */
	function RolesEditController(RoleService, $stateParams, $state, RolePrivilegeService) {
		var vm = this;
		vm.role = {};
		vm.submitText = 'Edit';
		vm.cancelText = 'Cancel';
		vm.title = 'Edit role';
		vm.objectTypes = [];


		RoleService.get($stateParams.id).then(function (response) {
			vm.role = response.data;
		}).then(function () {
			RolePrivilegeService.getObjectTypes(vm).then(function () {
				RolePrivilegeService.checkPrivilegesOfRole(vm);
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
			RoleService.update(vm.role).then(function () {
				$state.go('roles.list');
			})
		};

		vm.cancel = function () {
			$state.go('roles.list');
		};


	}
})();
