/**
 * Created by maksim.kalenik on 30.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactInfo', crmContactInfo);

    /** @ngInject */
    function crmContactInfo() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/contact-info/crm-contact-info.html',
            scope: false
        };
    }
})();
