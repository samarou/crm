/**
 * Created by anton.charnou on 12.04.2016.
 */
(function () {
  'use strict';

  angular
    .module('securityManagement')
    .controller('ContactsEditController', ContactsEditController);

  /** @ngInject */
  function ContactsEditController($q, $state, AuthService, ContactService, AclServiceBuilder, ConcatPermissionServiceBuilder, $stateParams) {
    'use strict';
    var vm = this;
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

    vm.isManager = AuthService.isManager();

    vm.contact = {};
    vm.aclHandler = {
      canEdit: false,
      permissions: [],
      actions: AclServiceBuilder(ConcatPermissionServiceBuilder(function () {
        return vm.contact;
      }))
    };

    $q.all(
      [
        ContactService.isAllowed($stateParams.id, permissions.write).then(function (response) {
          vm.aclHandler.canEdit = !!response.data;
          if (!vm.aclHandler.canEdit) {
            vm.submitText = null;
            vm.cancelText = 'Ok'
          }

        }),
        ContactService.getPermissions($stateParams.id).then(function (response) {
          vm.aclHandler.permissions = response.data;
        })
      ]
    ).then(function () {
        ContactService.get($stateParams.id).then(function (response) {
          vm.contact = response.data;
        })
      });


    vm.submit = function () {
      ContactService.update(vm.contact).then(function () {
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
