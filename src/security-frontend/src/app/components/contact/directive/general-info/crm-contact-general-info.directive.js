/**
 * Created by maksim.kalenik on 30.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactGeneralInfo', crmContactGeneralInfo);

    /** @ngInject */
    function crmContactGeneralInfo() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/general-info/crm-contact-general-info.html',
            scope: false
        };
    }
})();
