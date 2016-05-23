/**
 * Created by maksim.kalenik on 23.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactEmails', crmContactEmails);

    /** @ngInject */
    function crmContactEmails() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/emails/crm-contact-emails.html',
            scope: false
        };
    }
})();
