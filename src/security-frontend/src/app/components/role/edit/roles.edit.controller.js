(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('RolesEditController', RolesEditController);

	/** @ngInject */
	function RolesEditController(roleService, roleDetailsService, $stateParams, rolePrivilegeService) {
		var vm = this;
		vm.role = {};
		vm.submitText = 'Save';
		vm.title = 'Edit role';
		vm.objectTypes = [];
		vm.submit = submit;
		vm.cancel = roleDetailsService.cancel;

		init();

		function init() {
			roleService.get($stateParams.id).then(function (response) {
				vm.role = response.data;
			}).then(function () {
				rolePrivilegeService.getObjectTypes(vm).then(function () {
					rolePrivilegeService.checkPrivilegesOfRole(vm);
				});
			});
		}

		function submit() {
			roleDetailsService.submit(vm, false);
		}
	}
})();
