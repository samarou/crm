(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('ContactsEditController', ContactsEditController);

	/** @ngInject */
	function ContactsEditController($q, $state, authService, contactService, contactPermissionsService, $stateParams) {
		'use strict';
		var vm = this;
		vm.canEdit = false;
		vm.contact = {};
		vm.permissions = [];
		vm.actions = contactPermissionsService;

		vm.isManager = authService.isManager();

		var permissions = {
			read: 'read',
			write: 'write',
			create: 'create',
			delete: 'delete',
			admin: 'admin'
		};

		vm.submitText = 'Save';
		vm.cancelText = 'Cancel';
		vm.title = 'Edit contact';

		$q.all(
				[
					contactService.isAllowed($stateParams.id, permissions.write).then(function (response) {
						vm.canEdit = !!response.data;
						if (!vm.canEdit) {
							vm.submitText = null;
							vm.cancelText = 'Ok'
						}

					}),
					contactService.getPermissions($stateParams.id).then(function (response) {
						vm.permissions = response.data;
					})
				]
		).then(function () {
			contactService.get($stateParams.id).then(function (response) {
				vm.contact = response.data;
			})
		});


		vm.submit = function () {
			contactService.update(vm.contact).then(function () {
				contactService.updatePermissions(vm.contact.id, vm.permissions).then(
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