(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('UsersListController', UserListController);


	/** @ngInject */
	function UserListController($q, userService, searchBundle, groupService, roleService, $state) {
		'use strict';
		var vm = this;

		groupService.getAll().then(function (response) {
			vm.groups = response.data;
		});
		roleService.fetchAll().then(function (response) {
			vm.roles = response.data;
		});

		vm.bundle = searchBundle.userSecuredMode();
		vm.bundle.find();

		vm.activate = function (newState) {
			var tasks = [];
			vm.bundle.itemsList.forEach(function (user) {
				if (user.checked) {
					if (newState) {
						tasks.push(userService.activate(user.id));
					} else {
						tasks.push(userService.deactivate(user.id));
					}
				}
			});
			$q.all(tasks).then(vm.bundle.find);
		};


		vm.add = function () {
			$state.go('users.add');
		};

		vm.edit = function (user) {
			$state.go('users.edit', {id: user.id});
		};
	}
})();
