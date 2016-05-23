/**
 * Created by maksim.kalenik on 23.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactSocialNetworks', crmContactSocialNetworks);

    /** @ngInject */
    function crmContactSocialNetworks() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/social-networks/crm-contact-social-networks.html',
            scope: false
        };
    }
})();
