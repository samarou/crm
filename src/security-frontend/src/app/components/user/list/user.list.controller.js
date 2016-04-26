(function () {
	'use strict';

	angular
			.module('crm.user')
			.controller('UsersListController', UserListController);

	/** @ngInject */
	function UserListController($q, userService, userDetailsService, searchService, $state) {
		'use strict';
		var vm = this;
		vm.bundle = searchService.userSecuredMode();
		vm.activate = activate;
		vm.add = add;
		vm.edit = edit;

		init(vm.bundle);

		function init(bundle) {
			userDetailsService.getGroupsAndRoles().then(fullGroupsAndRoles);
			bundle.find();
		}

		function fullGroupsAndRoles(groupsAndRoles) {
			vm.groups = groupsAndRoles[0].data;
			vm.roles = groupsAndRoles[1].data;
		}

		function activate(newState) {
			var tasks = prepareActivationStateTasks(newState);
			$q.all(tasks).then(vm.bundle.find);
		}

		function add() {
			$state.go('users.add');
		}

		function edit(user) {
			$state.go('users.edit', {id: user.id});
		}

		function prepareActivationStateTasks(newState) {
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
			return tasks;
		}
	}
})();
