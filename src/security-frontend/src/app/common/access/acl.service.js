/**
 * Created by anton.charnou on 12.04.2016.
 */
(function () {
	'use strict';
	angular
			.module('securityManagement')
			.service('AclServiceBuilder', AclServiceBuilder);

	/** @ngInject */
	function AclServiceBuilder(Collections, DialogService, GroupBundle, SearchBundle) {
		var vm = {};

		vm.groupBundle = GroupBundle.publicMode();
		vm.userBundle = SearchBundle.userPublicMode();

    function createRemovePermissionsAction(permissionsService) {
      return function removePermissions(scope) {
        permissionsService.removePermissions(scope.permissions.filter(function (p) {
          return p.checked;
        })).then(function () {
          permissionsService.getPermissions().then(function (response) {
            scope.permissions = response.data;
          })
        });
      }
    }

		function addPermissionsForUser(scope) {
			vm.userBundle.find();
			DialogService.custom('app/common/access/public-users.modal.view.html', {
				title: 'Add Permissions for User',
				bundle: vm.userBundle,
				size: 'lg',
				cancelTitle: 'Back',
				okTitle: 'Ok'
			}).result.then(function (model) {
				model.bundle.itemsList.forEach(function (user) {
					var stillNotPresent = !Collections.find(user, scope.permissions);
					if (stillNotPresent && user.checked) {
						addDefaultPermission(user.id, user.userName, 'user', scope);
					}
				});
			});
		}

		function addPermissionsForGroup(scope) {
			vm.groupBundle.find();
			DialogService.custom('app/common/access/public-groups.modal.view.html', {
				title: 'Add Permissions for Group',
				bundle: vm.groupBundle,
				size: 'lg',
				cancelTitle: 'Back',
				okTitle: 'Ok'
			}).result.then(function (model) {
				model.bundle.groupList.forEach(function (group) {
					var alreadyPresent = !!Collections.find(group, scope.permissions);
					if (!alreadyPresent && group.checked) {
						addDefaultPermission(group.id, group.name, 'group', scope);
					}
				});
			});
		}

		function addDefaultPermission(id, name, principalTypeName, scope) {
			var defaultPermission = {
				id: id,
				name: name,
				principalTypeName: principalTypeName,
				canRead: false,
				canWrite: false,
				canCreate: false,
				canDelete: false,
				canAdmin: false
			};
			scope.permissions.push(defaultPermission);
		}

		return function (permissionService) {
      return {
        addPermissionsForUser: addPermissionsForUser,
        addPermissionsForGroup: addPermissionsForGroup,
        removePermissions: createRemovePermissionsAction(permissionService)
      }
    }
	}
})();
