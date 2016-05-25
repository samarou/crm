/**
 * Created by maksim.kalenik on 23.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactTelephones', crmContactTelephones);

    /** @ngInject */
    function crmContactTelephones() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/telephones/crm-contact-telephones.html',
            scope: false
        };
    }
})();
