/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('GroupsListController', GroupsListController);


	function GroupsListController($q, GroupService, GroupBundle, $state) {
		var vm = this;

		vm.bundle = GroupBundle.securedMode();
		vm.bundle.find();
		vm.add = function () {
			$state.go('groups.add');
		};
		vm.edit = function (group) {
			$state.go('groups.edit', {id: group.id});
		};

		vm.remove = function () {
			var tasks = [];
			vm.bundle.pageGroups.forEach(function (group) {
				if (group.checked) {
					tasks.push(GroupService.remove(group.id))
				}
			});
			$q.all(tasks).then(vm.bundle.find);
			vm.bundle.isSelectedAll = false;
		};
	}
})();
