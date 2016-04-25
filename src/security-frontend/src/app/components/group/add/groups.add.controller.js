/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('GroupsAddController', GroupsAddController);


	function GroupsAddController(groupService, $state) {
		var vm = this;
		vm.group = {};
		vm.submitText = 'Add';
		vm.title = 'Add group';

		vm.submit = function () {
			groupService.create(vm.group).then(function () {
				$state.go('groups.list');
			})
		};

		vm.cancel = function () {
			$state.go('groups.list');
		};

	}
})();
