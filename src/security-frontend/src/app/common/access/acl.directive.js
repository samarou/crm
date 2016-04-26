/**
 * @author yauheni.putsykovich
 */

(function () {
  'use strict';

  angular
    .module('securityManagement')
    .directive('aclForm', aclForm);

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
