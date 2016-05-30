/**
 * Created by maksim.kalenik on 23.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactMessengers', crmContactMessengers);

    /** @ngInject */
    function crmContactMessengers() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/contact-info/messengers/crm-contact-messengers.html',
            scope: false
        };
    }
})();
