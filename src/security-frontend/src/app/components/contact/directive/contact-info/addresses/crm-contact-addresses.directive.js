/**
 * Created by maksim.kalenik on 23.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactAddresses', crmContactAddresses);

    /** @ngInject */
    function crmContactAddresses() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/contact-info/addresses/crm-contact-addresses.html',
            scope: false
        };
    }
})();
