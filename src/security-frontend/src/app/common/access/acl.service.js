/**
 * @author yauheni.putsykovich
 */

(function () {
  'use strict';

  angular
    .module('securityManagement')
    .service('AclServiceBuilder', AclServiceBuilder);

  /** @ngInject */
  function AclServiceBuilder($q) {
    return function (getid, service) {
      return {
        getAcls: function () {
          return service.getAcls(getid());
        },
        removeAcls: function (acls) {
          var resultDefer = $q.defer();
          $q.all(
            ([].concat(acls)).map(function (acl) {
              return service.removeAcl(getid(), acl.id);
            })
          ).then(resultDefer.resolve, resultDefer.reject);
          return resultDefer.promise;
        }
      }
    }
  }
})();
