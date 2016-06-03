/**
 * Created by yauheni.putsykovich on 02.06.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .directive('crmTaskGeneralInfo', taskGeneralInfo);

    function taskGeneralInfo() {
        return {
            restrict: 'E',
            require: 'task',
            scope: false,
            templateUrl: 'app/components/task/tabs/general-info/task.general.info.view.html',
            replace: true
        };
    }

})();
