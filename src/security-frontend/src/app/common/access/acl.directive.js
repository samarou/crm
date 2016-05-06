/**
 * @author yauheni.putsykovich
 */

(function () {
  'use strict';

  angular
    .module('crm.acl')

  /** @ngInject */
  function aclForm() {
    return {
      restrict: 'E',
      templateUrl: 'app/common/access/acl-list.html',
      replace: true,
      scope: {
        handler: '='
      }
    };
  }
})();
