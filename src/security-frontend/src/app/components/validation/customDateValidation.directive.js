(function () {
    'use strict';

    angular
        .module('crm.validation')
        .directive('crmCustomDateValidation', crmCustomDateValidation);

    /** @ngInject */
    function crmCustomDateValidation() {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, element, attributes, ngModel) {

                var pattern, regex;
                pattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}$";
                regex = new RegExp(pattern, 'i');

                ngModel.$parsers.unshift(function(value){
                    var valid = isValid(value);
                    ngModel.$setValidity('date_validation', valid);

                    return valid ? value : undefined;
                });

                ngModel.$formatters.unshift(function(value){
                    ngModel.$setValidity('date_validation', isValid(value));
                    return value;
                });

                function isValid(value){
                    var valid = true;
                    if(angular.isDefined(element.attr('min-date'))){
                        var min = element.attr('min-date');
                        var max = element.attr('max-date');

                        var minDate = regex.test(min) ? new Date(min) : undefined;
                        var maxDate = regex.test(max) ? new Date(max) : undefined;
                        var enteredDate = regex.test(value) ? new Date(value) : undefined;

                        if(enteredDate){
                            var minValue = (angular.isUndefined(minDate))||(minDate < enteredDate);
                            var maxValue = (angular.isUndefined(maxDate))||(maxDate >= enteredDate);
                            if(!minValue || !maxValue){
                                valid = false;
                            }
                        }
                        return valid;
                    }

                    return true;
                }
            }
        };
    }
})();
