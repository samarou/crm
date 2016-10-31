(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactUniversities', crmContactUniversities);

    /** @ngInject */
    function crmContactUniversities() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/education-info/university/crm-contact-universities.html',
            scope: false
        };
    }
})();
