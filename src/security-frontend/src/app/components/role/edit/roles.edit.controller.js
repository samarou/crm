(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('RolesEditController', RolesEditController);


	/** @ngInject */
	function RolesEditController($q, RoleService, $stateParams, $state, PrivilegeService, Collections) {
		var vm = this;
		vm.role = {};
		vm.submitText = 'Edit';
		vm.cancelText = 'Cancel';
		vm.title = 'Edit role';
		vm.objectTypes = [];

		$q.all(
				[
					RoleService.get($stateParams.id).then(function (response) {
						vm.role = response.data;
					}),

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
					})
				]
		).then(function () {
			checkPrivilegesOfRole(vm.role);
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

		function checkPrivilegesOfRole(role) {
			vm.objectTypes.forEach(function (privilegeObject) {
				privilegeObject.actions.forEach(function (action) {
					action.privilege.checked = !!Collections.find(action.privilege, role.privileges);
				})
			});
		}

	}
})();
