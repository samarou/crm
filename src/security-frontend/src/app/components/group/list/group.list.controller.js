(function () {
	'use strict';

	angular
			.module('crm.group')
			.controller('GroupsListController', GroupsListController);

	function GroupsListController($q, groupService, groupSearch, dialogService, $state) {
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

    vm.remove = function () {
      var checkedGroups = vm.bundle.pageGroups.filter(function (group) {
        return group.checked;
      });
      ifAllGroupsAreEmpty(checkedGroups).then(function () {
          removeAll(checkedGroups);
      }, function () {
        dialogService.notify('Allowed remove only empty groups!');
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
          return groupService.get(group.id).then(function (response) {
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
          return groupService.remove(group.id);
        })
      ).then(vm.bundle.find);
    }
	}
})();
