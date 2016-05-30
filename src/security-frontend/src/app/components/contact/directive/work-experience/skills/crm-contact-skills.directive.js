/**
 * Created by maksim.kalenik on 30.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactSkills', crmContactSkills);

    /** @ngInject */
    function crmContactSkills() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/work-experience/skills/crm-contact-skills.html',
            scope: false
        };
    }
})();
