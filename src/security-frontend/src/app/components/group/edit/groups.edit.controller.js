/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('GroupsEditController', GroupsEditController);


	function GroupsEditController(groupService, $stateParams, $state) {
		var vm = this;
		vm.group = {};
		vm.submitText = 'Save';
		vm.title = 'Edit group';

		groupService.get($stateParams.id).then(function (response) {
			vm.group = response.data;
		});

		vm.submit = function () {
			groupService.update(vm.group).then(function () {
				$state.go('groups.list');
			})
		};

		vm.cancel = function () {
			$state.go('groups.list');
		};

	}
})();
