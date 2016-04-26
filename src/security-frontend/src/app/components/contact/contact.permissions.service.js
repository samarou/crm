(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('contactPermissionsService', contactPermissionsService);

	/** @ngInject */
	function contactPermissionsService($q, collections, dialogService, groupSearch, searchService, contactService) {
		var service = this;
		service.groupBundle = groupSearch.publicMode();
		service.userBundle = searchService.userPublicMode();

		return {
			addPermissionsForUser: addPermissionsForUser,
			addPermissionsForGroup: addPermissionsForGroup,
			removePermissions: removePermissions
		};

		function removePermissions(scope) {
			var tasks = [];
			scope.permissions.forEach(function (p) {
				if (p.checked) {
					tasks.push(contactService.removePermissions(scope.contact.id, p.id));
				}
			});
			$q.all(tasks).then(function () {
				contactService.getPermissions(scope.contact.id).then(function (response) {
					scope.permissions = response.data;
				})
			});
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

		function addPermissionsForGroup(scope) {
			openGroupDialog().then(function (model) {
				model.bundle.groupList.forEach(function (group) {
					var alreadyPresent = !!collections.find(group, scope.permissions);
					if (!alreadyPresent && group.checked) {
						addDefaultPermission(group.id, group.name, 'group', scope);
					}
				});
			});
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

		function addDefaultPermission(id, name, type, scope) {
			var defaultPermission = {
				id: id,
				name: name,
				principalTypeName: type,
				canRead: false,
				canWrite: false,
				canCreate: false,
				canDelete: false,
				canAdmin: false
			};
			scope.permissions.push(defaultPermission);
		}
	}
})();
