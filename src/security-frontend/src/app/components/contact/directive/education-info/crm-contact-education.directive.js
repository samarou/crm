
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactEducationInfo', crmEducationInfo);

    /** @ngInject */
    function crmEducationInfo() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/education-info/crm-education-info.html',
            scope: false
        };
    }
})();
