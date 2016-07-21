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
            restrict: 'E',
            templateUrl: 'app/common/directives/crm-datetimepicker.html',
            replace: true,
            scope: {
                model: '=',
                onchange: '&?',
                datepickerOptions: '=?',
                isEdit: '=',
                label: '@?'
            },
            link: function (scope, element) {
                function nullOrPrependZeros(value) {
                    return angular.isNumber(value)
                        ? value < 10 ? '0' + value : value
                        : null;
                }

                function resetTimepicker(timepicker, newValue) {
                    timepicker.find('input[ng-model="hours"]').val(nullOrPrependZeros(newValue && newValue.getHours()));
                    timepicker.find('input[ng-model="minutes"]').val(nullOrPrependZeros(newValue && newValue.getMinutes()));
                }

                scope.$watch('model', function (newValue) {
                    resetTimepicker(element.find('[uib-timepicker].ng-hide'), newValue);
                    resetTimepicker(element.find('[uib-timepicker]:not(.ng-hide)'), newValue);
                });
            }
        };
    }
})();
