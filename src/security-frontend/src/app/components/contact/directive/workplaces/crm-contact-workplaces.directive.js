/**
 * Created by maksim.kalenik on 23.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactWorkplaces', crmContactWorkplaces);

    /** @ngInject */
    function crmContactWorkplaces() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/workplaces/crm-contact-workplaces.html',
            scope: false
        };
    }
})();
