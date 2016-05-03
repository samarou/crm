/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('GroupsListController', GroupsListController);


	function GroupsListController($q, GroupService, GroupBundle, DialogService, $state) {
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
      DialogService.confirm('You sure want to delete this group?').result.then(function (answer) {
        var checkedGroups = vm.bundle.pageGroups.filter(function (group) {
          return group.checked;
        });
        if (answer) {
          $q.all(
            checkedGroups.map(function (group) {
              return GroupService.remove(group.id);
            })
          ).then(vm.bundle.find, function () {
              DialogService.error('Occurred an error during removing groups');
            });
        }
      });
    };
  }
})();
