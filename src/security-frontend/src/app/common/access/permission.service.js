/**
 * @author yauheni.putsykovich
 */

(function () {
  'use strict';
  angular
    .module('securityManagement')
    .service('PermissionServiceBuilder', PermissionServiceBuilder);

  /** @ngInject */
  function PermissionServiceBuilder($q) {
    return function (getid, service) {
      return {
        getPermissions: function () {
          return service.getPermissions(getid());
        },
        removePermissions: function (permissions) {
          var resultDefer = $q.defer();
          $q.all(
            ([].concat(permissions)).map(function (permission) {
              return service.removePermissions(getid(), permission.id);
            })
          ).then(resultDefer.resolve, resultDefer.reject);
          return resultDefer.promise;
        }
      }
    }
  }
})();
