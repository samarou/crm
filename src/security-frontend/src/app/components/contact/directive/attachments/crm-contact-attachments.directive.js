/**
 * Created by maksim.kalenik on 23.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContactAttachments', crmContactAttachments);

    /** @ngInject */
    function crmContactAttachments() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/attachments/crm-contact-attachments.html',
            scope: false
        };
    }
})();
