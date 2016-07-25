(function(){
    'use strict';

    angular
        .module('crm.validation')
        .directive('crmMinDate', crmMinDate);

    /** @ngInject */
    function crmMinDate(){
        return {
            restrict:'A',
            require:"^form",
            link: function(scope, elem, attrs, form){
                var formName = elem.parents('[ng-form]').length ? elem.parents('[ng-form]').attr('ng-form') : elem.parents('form').attr('name');
                var controlName = elem.attr('name');
                scope.$watch(formName + '.' + controlName + '.$dirty', function (newval) {
                    if (newval) {
                        watch();
                    }
                });

                function watch() {
                    scope.$watch(formName + '.' + controlName + '.$invalid', function () {
                        var dependentFieldName = elem.attr('dependentField');
                        var dependentField = form[dependentFieldName];
                        angular.forEach(dependentField.$parsers, function (parser) {
                           parser(dependentField.$viewValue);
                        });
                    });
                }
            }
        };
    }
})();
