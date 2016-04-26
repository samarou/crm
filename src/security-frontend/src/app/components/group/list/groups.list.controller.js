(function () {
	'use strict';

	angular
			.module('crm')
			.controller('GroupsListController', GroupsListController);

	function GroupsListController($q, groupService, groupSearch, $state) {
		var vm = this;
		vm.bundle = groupSearch.securedMode();
		vm.add = add;
		vm.edit = edit;
		vm.remove = remove;

		init();

		function init() {
			vm.bundle.find();
		}

		function add() {
			$state.go('groups.add');
		}

		function edit(group) {
			$state.go('groups.edit', {id: group.id});
		}

		function remove() {
			var tasks = [];
			vm.bundle.pageGroups.forEach(function (group) {
				if (group.checked) {
					tasks.push(groupService.remove(group.id))
				}
			});
			$q.all(tasks).then(vm.bundle.find);
			vm.bundle.isSelectedAll = false;
		}
	}
})();
