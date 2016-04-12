/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('ContactsController', ContactsController);

	/** @ngInject */
	function ContactsController($q, AuthService, ContactService, GroupBundle, SearchBundle, Collections, DialogService) {
		'use strict';
		var vm = this;

		var editContactBundle = {
			contact: null,
			permissions: null,
			canEdit: false,
			actions: {
				addPermissionsForUser: addPermissionsForUser,
				addPermissionsForGroup: addPermissionsForGroup,
				removePermissions: removePermissions
			}
		};

		var permissions = {
			read: 'read',
			write: 'write',
			create: 'create',
			delete: 'delete',
			admin: 'admin'
		};

		vm.isManager = AuthService.isManager();
		vm.groupBundle = GroupBundle.publicMode();
		vm.userBundle = SearchBundle.userPublicMode();
		vm.searchContactBundle = SearchBundle.contactMode();
		vm.searchContactBundle.find();

		vm.create = function () {
				editContactBundle.canEdit = true;
				editContactBundle.contact = {};
				editContactBundle.permissions = [];
				openContactDialog({
					title: 'Create Contact',
					okTitle: 'Add',
					cancelTitle: 'Cancel'
				});
		};

		vm.edit = function (contact) {
			ContactService.isAllowed(contact.id, permissions.read).then(function (response) {
				if (!response.data) {
					DialogService.notify('You haven\'t permissions to edit that contact!');
					return;
				}
				ContactService.isAllowed(contact.id, permissions.write).then(function (response) {
					editContactBundle.canEdit = !!response.data;
					ContactService.getPermissions(contact.id).then(function (response) {
						editContactBundle.contact = angular.copy(contact);
						editContactBundle.permissions = response.data;
						openContactDialog({
							title: 'Editing Contact',
							okTitle: editContactBundle.canEdit ? 'Update' : null,
							cancelTitle: editContactBundle.canEdit ? 'Cancel' : 'Ok'
						});
					});
				});
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
					var task = ContactService.isAllowed(contact.id, permissions.delete).then(function (response) {
						if (!response.data) {
							allContactsCanBeDeleted = false;
						}
					});
					tasks.push(task);
				});
				$q.all(tasks).then(function () {
					if (!allContactsCanBeDeleted) {
						DialogService.notify('You don\'t have permissions to do it.');
						return;
					}

					tasks = [];
					checkedContacts.forEach(function (contact) {
						if (contact.checked) {
							tasks.push(ContactService.remove(contact.id));
						}
					});
					if (allContactsCanBeDeleted) {
						$q.all(tasks).then(vm.searchContactBundle.find)
					}
				});
			});
		};

		function openRemoveDialog() {
			return DialogService.confirm('Do you want to delete this contact?').result;
		}

		function openContactDialog(model) {
			model.bundle = editContactBundle;
			DialogService.custom('app/components/contact/contact.modal.view.html', model).result.then(updateContact);
		}

		function updateContact(model) {
			var contact = model.bundle.contact;
			if (contact.id) {
				ContactService.update(contact).then(vm.searchContactBundle.find);
				ContactService.updatePermissions(contact.id, model.bundle.permissions);
			} else {
				ContactService.create(contact).then(function (response) {
					var contactId = response.data;
					ContactService.updatePermissions(contactId, model.bundle.permissions).then(vm.searchContactBundle.find);
				});
			}
		}

		function removePermissions(contact) {
			var tasks = [];
			editContactBundle.permissions.forEach(function (p) {
				if (p.checked) {
					tasks.push(ContactService.removePermissions(contact.id, p.id));
				}
			});
			$q.all(tasks).then(function () {
				ContactService.getPermissions(contact.id).then(function (response) {
					editContactBundle.permissions = response.data;
				})
			});
		}

		function addPermissionsForUser() {
			vm.userBundle.find();
			DialogService.custom('app/components/contact/public-users.modal.view.html', {
				title: 'Add Permissions for User',
				bundle: vm.userBundle,
				size: 'modal--user-table',
				cancelTitle: 'Back',
				okTitle: 'Ok'
			}).result.then(function (model) {
				model.bundle.itemsList.forEach(function (user) {
					var stillNotPresent = !Collections.find(user, editContactBundle.permissions);
					if (stillNotPresent && user.checked) {
						addDefaultPermission(user.id, user.userName, 'user');
					}
				});
			});
		}

		function addPermissionsForGroup() {
			vm.groupBundle.find();
			DialogService.custom('app/components/contact/public-groups.modal.view.html', {
				title: 'Add Permissions for Group',
				bundle: vm.groupBundle,
				size: 'modal--group-table',
				cancelTitle: 'Back',
				okTitle: 'Ok'
			}).result.then(function (model) {
				model.bundle.groupList.forEach(function (group) {
					var alreadyPresent = !!Collections.find(group, editContactBundle.permissions);
					if (!alreadyPresent && group.checked) {
						addDefaultPermission(group.id, group.name, 'group');
					}
				});
			});
		}

		function addDefaultPermission(id, name, type) {
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
			editContactBundle.permissions.push(defaultPermission);
		}
	}
})();