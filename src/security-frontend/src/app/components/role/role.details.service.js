(function () {
	'use strict';

	angular
			.module('crm')
			.factory('roleDetailsService', roleDetailsService);

	/** @ngInject */
	function roleDetailsService(roleService, rolePrivilegeService, $state) {
		return {
			submit: submit,
			cancel: goToList
		};

		function submit(scope, isNew) {
			scope.role.privileges = [];
			rolePrivilegeService.getPrivilegesOfRole(scope);
			if (isNew) {
				roleService.create(scope.role).then(goToList)
			} else {
				roleService.update(scope.role).then(goToList)
			}
		}

		function goToList() {
			$state.go('roles.list');
		}
	}
})();
