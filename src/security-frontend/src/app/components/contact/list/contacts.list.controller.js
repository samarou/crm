/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('ContactsListController', ContactsListController);

	/** @ngInject */
	function ContactsListController($q, AuthService, contactService, SearchBundle, dialogService, $state) {
		'use strict';
		var vm = this;

		var permissions = {
			read: 'read',
			write: 'write',
			create: 'create',
			delete: 'delete',
			admin: 'admin'
		};

		vm.isManager = AuthService.isManager();
		vm.searchContactBundle = SearchBundle.contactMode();
		vm.searchContactBundle.find();

		vm.add = function () {
			$state.go('contacts.add');
		};

		vm.edit = function (contact) {
			contactService.isAllowed(contact.id, permissions.read).then(function (response) {
				if (!response.data) {
					dialogService.notify('You haven\'t permissions to edit that contact!');
					return;
				}
				$state.go('contacts.edit', {id: contact.id});
			});
		};

		vm.remove = function () {
			openRemoveDialog().then(function () {
				var tasks = [];
				var allContactsCanBeDeleted = true;
				var checkedContacts = vm.searchContactBundle.itemsList.filter(function (contact) {
					return contact.checked;
				});
				checkedContacts.forEach(function (contact) {
					var task = contactService.isAllowed(contact.id, permissions.delete).then(function (response) {
						if (!response.data) {
							allContactsCanBeDeleted = false;
						}
					});
					tasks.push(task);
				});
				$q.all(tasks).then(function () {
					if (!allContactsCanBeDeleted) {
						dialogService.notify('You don\'t have permissions to do it.');
						return;
					}

					tasks = [];
					checkedContacts.forEach(function (contact) {
						if (contact.checked) {
							tasks.push(contactService.remove(contact.id));
						}
					});
					if (allContactsCanBeDeleted) {
						$q.all(tasks).then(vm.searchContactBundle.find)
					}
				});
			});
		};

		function openRemoveDialog() {
			return dialogService.confirm('Do you want to delete this contact?').result;
		}


	}
})();