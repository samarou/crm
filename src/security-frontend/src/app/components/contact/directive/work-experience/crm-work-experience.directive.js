/**
 * Created by maksim.kalenik on 30.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactWorkExperience', crmContactWorkExperience);

    /** @ngInject */
    function crmContactWorkExperience() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/work-experience/crm-work-experience.html',
            scope: false
        };
    }
})();
