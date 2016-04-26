/**
 * @author yauheni.putsykovich
 */

/**
 * Created by anton.charnou on 12.04.2016.
 */
(function () {
  'use strict';
  angular
    .module('securityManagement')
    .service('ConcatPermissionServiceBuilder', ConcatPermissionServiceBuilder);

  /** @ngInject */
  function ConcatPermissionServiceBuilder($q, ContactService) {
    return function (contact) {
      return {
        getPermissions: function () {
          return ContactService.getPermissions(contact.id);
        },
        removePermissions: function (permissions) {
          var resultDefer = $q.defer();
          $q.all(
            permissions.map(function (permission) {
              return ContactService.removePermissions(contact.id, permission.id);
            })
          ).then(resultDefer.resolve, resultDefer.reject);
          return resultDefer.promise;
        }
      }
    }
  }
})();
