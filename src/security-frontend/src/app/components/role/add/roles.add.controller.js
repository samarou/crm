(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('RolesAddController', RolesAddController);


	function RolesAddController(RoleService, PrivilegeService, Collections, $state) {
		var vm = this;
		vm.role = {};
		vm.submitText = 'Add';
		vm.cancelText = 'Cancel';
		vm.title = 'Add role';
		vm.objectTypes = [];

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
			vm.objectTypes = Collections.sort(Object.keys(objectTypeList)).map(function (objectTypeName) {
				return {
					objectTypeName: objectTypeName,
					actions: Collections.sort(objectTypeList[objectTypeName], true, Collections.byProperty('id'))
				};
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
			RoleService.create(vm.role).then(function () {
				$state.go('roles.list');
			})
		};

		vm.cancel = function () {
			$state.go('roles.list');
		};

	}
})();
