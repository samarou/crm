(function () {
	'use strict';

	angular
			.module('crm.contact')
			.factory('aclService', aclService);

	/** @ngInject */
	function aclService(collections, dialogService, groupSearch, searchService) {
		var service = this;

		service.groupBundle = groupSearch.publicMode();
		service.userBundle = searchService.userPublicMode();

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
      service.userBundle.find();
      openUserDialog().then(function (model) {
        model.bundle.itemsList.forEach(function (user) {
          var stillNotPresent = !collections.find(user, scope.permissions);
          if (stillNotPresent && user.checked) {
            addDefaultPermission(user.id, user.userName, 'user', scope);
          }
        });
      });
    }

		function addPermissionsForGroup(scope) {
			openGroupDialog().then(function (model) {
				model.bundle.groupList.forEach(function (group) {
          var stillNotPresent = !collections.find(group, scope.permissions);
					if (stillNotPresent && group.checked) {
						addDefaultPermission(group.id, group.name, 'group', scope);
					}
				});
			});
		}

    function openUserDialog() {
      service.userBundle.find();
      return dialogService.custom('app/components/contact/public-users.modal.view.html', {
        title: 'Add Permissions for User',
        bundle: service.userBundle,
        size: 'modal--user-table',
        cancelTitle: 'Back',
        okTitle: 'Ok'
      }).result;
    }

		function openGroupDialog() {
			service.groupBundle.find();
			return dialogService.custom('app/components/contact/public-groups.modal.view.html', {
				title: 'Add Permissions for Group',
				bundle: service.groupBundle,
				size: 'modal--group-table',
				cancelTitle: 'Back',
				okTitle: 'Ok'
			}).result
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
