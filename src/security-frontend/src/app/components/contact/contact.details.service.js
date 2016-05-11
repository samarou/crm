(function () {
	'use strict';

	angular
			.module('crm.contact')
			.factory('contactDetailsService', contactDetailsService);

	/** @ngInject */
	function contactDetailsService(contactService, aclServiceBuilder, $state) {
		return {
			submit: submit,
			cancel: goToList,
      createAclHandler: createAclHandler
		};

    function createAclHandler(getId) {
      return {
        canEdit: true,
        acls: [],
        actions: aclServiceBuilder(getId, contactService)
      }
    }

		function submit(contact, acls, isNew) {
			if (isNew) {
				contactService.create(contact).then(function (response) {
					var id = response.data;
					contactService.updateAcls(id, acls).then(goToList);
				});
			} else {
				contactService.update(contact).then(function () {
					contactService.updateAcls(contact.id, acls).then(goToList);
				});
			}
		}

		function goToList() {
			$state.go('contacts.list');
		}
	}
})();
