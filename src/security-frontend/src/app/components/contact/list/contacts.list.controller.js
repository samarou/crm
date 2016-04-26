(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('ContactsListController', ContactsListController);

	/** @ngInject */
	function ContactsListController($q, authService, contactService, searchService, dialogService, $state, contactSecurityService) {
		'use strict';
		var vm = this;
		vm.isManager = authService.isManager();
		vm.searchContactBundle = searchService.contactMode();
		vm.add = add;
		vm.edit = edit;
		vm.remove = remove;

		init();

		function init() {
			vm.searchContactBundle.find();
		}

		function add() {
			$state.go('contacts.add');
		}

		function edit(contact) {
			contactSecurityService.checkReadPermission(contact).then(function () {
				$state.go('contacts.edit', {id: contact.id});
			});
		}

		function remove() {
			openRemoveDialog().then(function () {
				var checkedContacts = vm.searchContactBundle.itemsList.filter(function (contact) {
					return contact.checked;
				});
				contactSecurityService.checkDeletePermissionForList(checkedContacts).then(removeContacts)
			});
		}

		function removeContacts(checkedContacts) {
			var tasks = [];
			checkedContacts.forEach(function (contact) {
				if (contact.checked) {
					tasks.push(contactService.remove(contact.id));
				}
			});
			$q.all(tasks).then(vm.searchContactBundle.find)
		}

		function openRemoveDialog() {
			return dialogService.confirm('Do you want to delete this contact?').result;
		}
	}
})();