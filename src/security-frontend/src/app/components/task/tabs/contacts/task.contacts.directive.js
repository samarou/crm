/**
 * Created by yauheni.putsykovich on 02.06.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .directive('crmTaskContacts', taskContacts);

    function taskContacts() {
        return {
            restrict: 'E',
            scope: false,
            templateUrl: 'app/components/task/tabs/contacts/task.contacts.view.html',
            replace: true
        }
    }

})();
