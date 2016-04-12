(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('UsersListController', UserListController);


	/** @ngInject */
	function UserListController($q, UserService, SearchBundle, GroupService, RoleService, $state) {
		'use strict';
		var vm = this;

		GroupService.getAll().then(function (response) {
			vm.groups = response.data;
		});
		RoleService.fetchAll().then(function (response) {
			vm.roles = response.data;
		});

		vm.bundle = SearchBundle.userSecuredMode();
		vm.bundle.find();

		vm.activate = function (newState) {
			var tasks = [];
			vm.bundle.itemsList.forEach(function (user) {
				if (user.checked) {
					if (newState) {
						tasks.push(UserService.activate(user.id));
					} else {
						tasks.push(UserService.deactivate(user.id));
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
