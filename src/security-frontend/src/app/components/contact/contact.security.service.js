(function () {
	'use strict';

	angular
			.module('crm.contact')
			.factory('contactSecurityService', contactSecurityService);

	/** @ngInject */
	function contactSecurityService(contactService, permissions, $q, dialogService, $log) {
		return {
			checkReadPermission: checkReadPermission,
			checkDeletePermissionForList: checkDeletePermissionForList,
			checkEditPermission: checkEditPermission
		};

		function checkReadPermission(contact) {
			return contactService.isAllowed(contact.id, permissions.read).then(function (response) {
				if (!response.data) {
					dialogService.notify('You haven\'t permissions to edit that contact!');
					return $q.reject(response);
				}
				return $q.resolve(response);
			});
		}

		function checkDeletePermissionForList(contactList) {
			var tasks = [];
			contactList.forEach(function (contact) {
				var task = checkDeletePermission(contact);
				tasks.push(task);
			});
			return $q.all(tasks).then(function (contactList) {
				$log.log(contactList);
				return $q.resolve(contactList);
			}).catch(function () {
						dialogService.notify('You don\'t have permissions to do it.');
						return $q.reject();
					}
			);
		}

		function checkEditPermission(id) {
			return contactService.isAllowed(id, permissions.write).then(function (response) {
				return $q.resolve(!!response.data);
			});
		}

		function checkDeletePermission(contact) {
			return contactService.isAllowed(contact.id, permissions.delete).then(function (response) {
				if (!response.data) {
					return $q.reject(contact);
				}
				return $q.resolve(contact);
			});
		}
	}
})();