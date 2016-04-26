(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('ContactsEditController', ContactsEditController);

	/** @ngInject */
	function ContactsEditController($q, contactDetailsService, contactSecurityService, authService, contactService, contactPermissionsService, $stateParams) {
		var vm = this;
		vm.canEdit = false;
		vm.contact = {};
		vm.permissions = [];
		vm.actions = contactPermissionsService;
		vm.isManager = authService.isManager();
		vm.submitText = 'Save';
		vm.title = 'Edit contact';
		vm.submit = submit;
		vm.cancel = contactDetailsService.cancel;

		init();

		function init() {
			$q.all(
					[
						isEditable(),
						getPermissions()

					]
			).then(getContact);
		}

		function submit() {
			contactDetailsService.submit(vm.contact, vm.permissions, false);
		}

		function getPermissions() {
			return contactService.getPermissions($stateParams.id).then(function (response) {
				vm.permissions = response.data;
			})
		}

		function isEditable() {
			return contactSecurityService.checkEditPermission($stateParams.id).then(function (canEdit) {
				vm.canEdit = canEdit;
				if (!vm.canEdit) {
					vm.submitText = null;
					vm.cancelText = 'Ok'
				}
			})
		}

		function getContact() {
			return contactService.get($stateParams.id).then(function (response) {
				vm.contact = response.data;
			});
		}
	}
})();