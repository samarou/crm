/**
 * Created by anton.charnou on 12.04.2016.
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('ContactsAddController', ContactsAddController);

	/** @ngInject */
	function ContactsAddController(ContactService, ContactPermissionsService, $state) {
		'use strict';
		var vm = this;
		vm.canEdit = true;
		vm.contact = {};
		vm.permissions = [];
		vm.actions = ContactPermissionsService;

		vm.submitText = 'Add';
		vm.cancelText = 'Cancel';
		vm.title = 'Add contact';


		vm.submit = function () {
			ContactService.create(vm.contact).then(function (response) {
				var id = response.data;
				ContactService.updatePermissions(id, vm.permissions).then(
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