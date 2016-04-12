/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('GroupsEditController', GroupsEditController);


	function GroupsEditController(GroupService, $stateParams, $state) {
		var vm = this;
		vm.group = {};
		vm.submitText = 'Edit';
		vm.cancelText = 'Cancel';
		vm.title = 'Edit group';

		GroupService.get($stateParams.id).then(function (response) {
			vm.group = response.data;
		});

		vm.submit = function () {
			GroupService.update(vm.group).then(function () {
				$state.go('groups.list');
			})
		};

		vm.cancel = function () {
			$state.go('groups.list');
		};

	}
})();
