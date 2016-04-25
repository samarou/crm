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
      var checkedGroups = vm.bundle.pageGroups.filter(function (group) {
        return group.checked;
      });
      ifAllGroupsAreEmpty(checkedGroups).then(function () {
          removeAll(checkedGroups);
      }, function () {
        DialogService.notify('Allowed remove only empty groups!');
      });
    };

    function ifAllGroupsAreEmpty(groups) {
      var allGroupsAreEmpty = true;
      var resultDefer = $q.defer();
      $q.all(
        groups.map(function (group) {
          if (!allGroupsAreEmpty) {
            return;
          }
          return GroupService.get(group.id).then(function (response) {
            var group = response.data;
            if (group.members.length > 0) {
              allGroupsAreEmpty = false;
            }
          });
        })
      ).then(function () {
        if (allGroupsAreEmpty) {
          resultDefer.resolve();
        } else {
          resultDefer.reject();
        }
      });
      return resultDefer.promise;
    }

    function removeAll(groups) {
      $q.all(
        groups.map(function (group) {
          return GroupService.remove(group.id);
        })
      ).then(vm.bundle.find);
    }
	}
})();
