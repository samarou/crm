/**
 * Created by yauheni.putsykovich on 31.05.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.common')
        .directive('crmDatepicker', crmDatepicker);

    function crmDatepicker() {
        return {
            require: 'model',
            restrict: 'E',
            templateUrl: 'app/common/directives/crm-datepicker.html',
            replace: true,
            scope: {
                model: '=',
                datepickerOptions: '=?',
                isEdit: '='
            }
        };
    }
})();
