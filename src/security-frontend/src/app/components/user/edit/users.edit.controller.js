/**
 * Created by anton.charnou on 11.04.2016.
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('UsersEditController', UserEditController);


	/** @ngInject */
	function UserEditController(userService, groupService, roleService, $state, Collections, $stateParams, $q) {
		'use strict';
		var vm = this;
		vm.user = {};
		vm.groups = [];
		vm.roles = [];
		vm.submitText = 'Save';
		vm.title = 'Edit user';

		$q.all(
				[
					groupService.getAll().then(function (response) {
						vm.groups = response.data;
					}),
					roleService.fetchAll().then(function (response) {
						vm.roles = response.data;
					})
				]
		).then(function () {
			userService.getById($stateParams.id).then(function (response) {
				vm.user = response.data;
				checkGroupsAndRolesWhichUserHas(vm.user);
			});
		});

		vm.submit = function () {
			checkGroups(vm.user);
			checkRoles(vm.user);
			userService.update(vm.user).then(function () {
				$state.go('users.list');
			})
		};

		vm.cancel = function () {
			$state.go('users.list');
		};

		function checkGroups(user) {
			user.groups = vm.groups.filter(function (group) {
				return group.checked;
			});

		}

		function checkRoles(user) {
			user.roles = vm.roles.filter(function (role) {
				return role.checked;
			});
		}

		function checkGroupsAndRolesWhichUserHas(user) {
			vm.groups.forEach(function (group) {
				group.checked = !!Collections.find(group, user.groups);
			});
			vm.roles.forEach(function (role) {
				role.checked = !!Collections.find(role, user.roles);
			});
		}


	}
})();
