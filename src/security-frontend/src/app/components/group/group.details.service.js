(function () {
	'use strict';

	angular
			.module('crm.group')
			.factory('groupDetailsService', groupDetailsService);

	/** @ngInject */
	function groupDetailsService(groupService, $state) {
		return {
			submit: submit,
			cancel: goToList
		};

		function submit(group, isNew) {
			if (isNew) {
				groupService.create(group).then(goToList);
			} else {
				groupService.update(group).then(goToList);
			}
		}

		function goToList() {
			$state.go('groups.list');
		}
	}
})();
