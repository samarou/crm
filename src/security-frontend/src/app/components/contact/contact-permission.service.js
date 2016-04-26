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
    return function (getcontact) {
      return {
        getPermissions: function () {
          return ContactService.getPermissions(getcontact().id);
        },
        removePermissions: function (permissions) {
          var resultDefer = $q.defer();
          $q.all(
            permissions.map(function (permission) {
              return ContactService.removePermissions(getcontact().id, permission.id);
            })
          ).then(resultDefer.resolve, resultDefer.reject);
          return resultDefer.promise;
        }
      }
    }
  }
})();
