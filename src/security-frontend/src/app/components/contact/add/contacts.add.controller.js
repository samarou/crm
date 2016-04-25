(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('ContactsAddController', ContactsAddController);

	/** @ngInject */
	function ContactsAddController(contactService, contactPermissionsService, $state) {
		'use strict';
		var vm = this;
		vm.canEdit = true;
		vm.contact = {};
		vm.permissions = [];
		vm.actions = contactPermissionsService;

		vm.submitText = 'Add';
		vm.cancelText = 'Cancel';
		vm.title = 'Add contact';


		vm.submit = function () {
			contactService.create(vm.contact).then(function (response) {
				var id = response.data;
				contactService.updatePermissions(id, vm.permissions).then(
						function () {
							$state.go('contacts.list');
						}
				)
			});

		};

		vm.cancel = function () {
			$state.go('contacts.list');
		};
	}
})();