/**
 * Created by anton.charnou on 12.04.2016.
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('ContactsAddController', ContactsAddController);

	/** @ngInject */
	function ContactsAddController(ContactService, AclServiceBuilder, ConcatPermissionServiceBuilder, $state) {
		'use strict';
		var vm = this;
		vm.contact = {};

    vm.aclHandler = {
      canEdit: true,
      permissions: [],
      actions: AclServiceBuilder(ConcatPermissionServiceBuilder(function () {
        return vm.contact;
      }))
    };

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
