(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('contactPermissionsService', ContactPermissionsService);

	/** @ngInject */
	function ContactPermissionsService($q, Collections, dialogService, groupBundle, SearchBundle, contactService) {
		var vm = this;
		vm.groupBundle = groupBundle.publicMode();
		vm.userBundle = SearchBundle.userPublicMode();

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
			vm.userBundle.find();
			openUserDialog().then(function (model) {
				model.bundle.itemsList.forEach(function (user) {
					var stillNotPresent = !Collections.find(user, scope.permissions);
					if (stillNotPresent && user.checked) {
						addDefaultPermission(user.id, user.userName, 'user', scope);
					}
				});
			});
		}

		function openUserDialog() {
			vm.userBundle.find();
			return dialogService.custom('app/components/contact/public-users.modal.view.html', {
				title: 'Add Permissions for User',
				bundle: vm.userBundle,
				size: 'modal--user-table',
				cancelTitle: 'Back',
				okTitle: 'Ok'
			}).result;
		}

		function addPermissionsForGroup(scope) {
			openGroupDialog().then(function (model) {
				model.bundle.groupList.forEach(function (group) {
					var alreadyPresent = !!Collections.find(group, scope.permissions);
					if (!alreadyPresent && group.checked) {
						addDefaultPermission(group.id, group.name, 'group', scope);
					}
				});
			});
		}

		function openGroupDialog() {
			vm.groupBundle.find();
			return dialogService.custom('app/components/contact/public-groups.modal.view.html', {
				title: 'Add Permissions for Group',
				bundle: vm.groupBundle,
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

		return {
			addPermissionsForUser: addPermissionsForUser,
			addPermissionsForGroup: addPermissionsForGroup,
			removePermissions: removePermissions
		}
	}
})();
