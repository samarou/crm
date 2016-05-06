(function () {
	'use strict';

	angular
			.module('crm.contact')
			.controller('ContactsEditController', ContactsEditController);

	/** @ngInject */
	function ContactsEditController($q, contactDetailsService, contactSecurityService, authService, contactService, $stateParams) {
		var vm = this;

		vm.canEdit = false;
		vm.contact = {};
		vm.isManager = authService.isManager();
		vm.submitText = 'Save';
		vm.title = 'Edit contact';
		vm.submit = submit;
		vm.cancel = contactDetailsService.cancel;
    vm.aclHandler = contactDetailsService.createAclHandler(function () {
      return vm.contact.id;
    });

		init();

		function init() {
			$q.all(
					[
						isEditable(),
						getAcls()

					]
			).then(getContact);
		}

		function submit() {
			contactDetailsService.submit(vm.contact, vm.aclHandler.acls, false);
		}

		function getAcls() {
			return contactService.getAcls($stateParams.id).then(function (response) {
				vm.aclHandler.acls = response.data;
			})
		}

		function isEditable() {
			return contactSecurityService.checkEditPermission($stateParams.id).then(function (canEdit) {
				vm.canEdit = canEdit;
				vm.aclHandler.canEdit = canEdit;
				if (!canEdit) {
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
