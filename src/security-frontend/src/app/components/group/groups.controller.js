/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('GroupsController', GroupsController);


	function GroupsController($q, GroupService, GroupBundle, DialogService) {
		var vm = this;

		vm.bundle = GroupBundle.securedMode();
		vm.bundle.find();
		vm.edit = function (group) {
			showDialog({title: 'Editing Group', okTitle: 'Update', group: angular.copy(group)});
		};
		vm.create = function () {
			showDialog({title: 'Create a New Group', okTitle: 'Add', group: {}});
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

		function showDialog(model) {
			DialogService
					.custom('app/components/group/group.modal.view.html', model)
					.result.then(
					function (model) {
						update(model.group);
					});
		}

		function update(group) {
			if (group.id) {
				var originGroup = vm.bundle.groupList.find(function (g) {
					return g.id === group.id
				});
				angular.copy(group, originGroup);
				GroupService.update(group);
			} else {
				GroupService.create(group).then(function (response) {
					group.id = response.data;
					vm.bundle.groupList.push(group);
					vm.bundle.pagingFilterConfig.totalItems = vm.bundle.groupList.length;
				});
			}
		}
	}
})();
