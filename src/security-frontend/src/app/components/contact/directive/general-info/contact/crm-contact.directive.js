/**
 * Created by maksim.kalenik on 23.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContact', crmContact);

    /** @ngInject */
    function crmContact() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/general-info/contact/crm-contact.html',
            scope: false
        };
    }
})();
