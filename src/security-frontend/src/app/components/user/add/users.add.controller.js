(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('UsersAddController', UserAddController);


	/** @ngInject */
	function UserAddController(userService, GroupService, roleService, $state) {
		'use strict';
		var vm = this;
		vm.user = {active: 'true'};
		vm.gtoups = [];
		vm.roles = [];
		vm.submitText = 'Add';
		vm.title = 'Add user';

		GroupService.getAll().then(function (response) {
			vm.groups = response.data;
		});
		roleService.fetchAll().then(function (response) {
			vm.roles = response.data;
		});

		vm.submit = function () {
			checkGroups(vm.user);
			checkRoles(vm.user);
			userService.create(vm.user).then(function () {
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


	}
})();
