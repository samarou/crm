/**
 * Created by yauheni.putsykovich on 31.05.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.common')
        .directive('crmDateTimePicker', crmDateTimePicker);

    function crmDateTimePicker() {
        return {
            require: 'model',
            restrict: 'E',
            templateUrl: 'app/common/directives/crm-datetimepicker.html',
            replace: true,
            scope: {
                model: '=',
                onchange: '&?',
                datepickerOptions: '=?',
                isEdit: '=',
                label: '@?'
            }
        };
    }
})();
