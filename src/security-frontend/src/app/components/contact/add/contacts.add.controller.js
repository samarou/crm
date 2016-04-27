/**
 * Created by anton.charnou on 12.04.2016.
 */
(function () {
  'use strict';

  angular
    .module('securityManagement')
    .controller('ContactsAddController', ContactsAddController);

  /** @ngInject */
  function ContactsAddController(UserService, ContactService, AclServiceBuilder, PermissionServiceBuilder, $state) {
    'use strict';
    var vm = this;
    vm.contact = {};

    function getId() {
      return vm.contact.id;
    }

    vm.aclHandler = {
      canEdit: true,
      permissions: [],
      actions: AclServiceBuilder(PermissionServiceBuilder(getId, ContactService))
    };

    vm.submitText = 'Add';
    vm.cancelText = 'Cancel';
    vm.title = 'Add contact';

    UserService.getPermissionOfCurrentUser().then(function (response) {
      vm.aclHandler.permissions = response.data;
    });

    vm.submit = function () {
      ContactService.create(vm.contact).then(function (response) {
        vm.contact.id = response.data;
        ContactService.updatePermissions(vm.contact.id, vm.aclHandler.permissions).then(
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
