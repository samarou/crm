(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('contactDetailsService', contactDetailsService);

	/** @ngInject */
	function contactDetailsService(contactService, $state) {
		return {
			submit: submit,
			cancel: goToList
		};

		function submit(contact, permissions, isNew) {
			if (isNew) {
				contactService.create(contact).then(function (response) {
					var id = response.data;
					contactService.updatePermissions(id, permissions).then(goToList);
				});
			} else {
				contactService.update(contact).then(function () {
					contactService.updatePermissions(contact.id, permissions).then(goToList);
				});
			}
		}

		function goToList() {
			$state.go('contacts.list');
		}
	}
})();