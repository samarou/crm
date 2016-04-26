(function () {
	'use strict';

	angular
			.module('crm.user')
			.controller('UsersAddController', UserAddController);

	/** @ngInject */
	function UserAddController(userDetailsService) {
		'use strict';
		var vm = this;
		vm.user = {active: 'true'};
		vm.groups = [];
		vm.roles = [];
		vm.submitText = 'Add';
		vm.title = 'Add user';
		vm.submit = submit;
		vm.cancel = userDetailsService.cancel;

		init();

		function init() {
			userDetailsService.getGroupsAndRoles();
		}

		function submit() {
			userDetailsService.save(vm.user, vm.roles, vm.groups, true);
		}
	}
})();
