(function () {
	'use strict';

	angular
			.module('crm.contact')
			.controller('ContactsAddController', ContactsAddController);

	/** @ngInject */
	function ContactsAddController(contactDetailsService, userService) {
		var vm = this;

		vm.canEdit = true;
		vm.contact = {};
		vm.title = 'Add contact';
		vm.submitText = 'Add';
		vm.submit = submit;
		vm.cancel = contactDetailsService.cancel;
    vm.aclHandler = contactDetailsService.createAclHandler(function () {
      return vm.contact.id;
    });

    init();

    function init() {
      userService.getDefaultAcls().then(function (response) {
        vm.aclHandler.acls = response.data;
      });
    }

		function submit() {
			contactDetailsService.submit(vm.contact, vm.aclHandler.acls, true);
		}
	}
})();
