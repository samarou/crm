(function () {
	'use strict';

	angular
			.module('crm')
			.controller('GroupsEditController', GroupsEditController);

	function GroupsEditController(groupService, groupDetailsService, $stateParams) {
		var vm = this;
		vm.group = {};
		vm.submitText = 'Save';
		vm.title = 'Edit group';
		vm.submit = submit;
		vm.cancel = groupDetailsService.cancel;

		init();

		function init() {
			groupService.get($stateParams.id).then(function (response) {
				vm.group = response.data;
			});
		}

		function submit() {
			groupDetailsService.submit(vm.group, false);
		}
	}
})();
