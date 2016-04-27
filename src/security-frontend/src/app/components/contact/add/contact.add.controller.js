(function () {
	'use strict';

	angular
			.module('crm.contact')
			.controller('ContactsAddController', ContactsAddController);

	/** @ngInject */
	function ContactsAddController(contactDetailsService, contactPermissionsService) {
		var vm = this;

		vm.canEdit = true;
		vm.contact = {};
		vm.permissions = [];
		vm.actions = contactPermissionsService;
		vm.title = 'Add contact';
		vm.submitText = 'Add';
		vm.submit = submit;
		vm.cancel = contactDetailsService.cancel;

		function submit() {
			contactDetailsService.submit(vm.contact, vm.permissions, true);
		}
	}
})();