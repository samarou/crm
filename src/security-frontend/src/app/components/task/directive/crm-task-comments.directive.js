(function () {
    'use strict';

    angular
        .module('crm.task')
        .directive('crmTaskComments', crmTaskComments);

    /** @ngInject */
    function crmTaskComments() {
        return {
            restrict: 'E',
            templateUrl: 'app/components/task/directive/crm-task-comments.html',
            scope: false
        };
    }
})();
