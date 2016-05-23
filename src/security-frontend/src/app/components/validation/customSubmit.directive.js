(function () {
    'use strict';

    angular
        .module('crm.validation')
        .directive('crmCustomSubmit', crmCustomSubmit);

    /** @ngInject */
    function crmCustomSubmit() {
        return {
            restrict: 'A',
            link: function (scope, element, attributes) {
                var button = angular.element(element);


                button.bind('click', function (e) {
                    e.preventDefault();

                    var form = scope.$apply(attributes.crmFormValidation);
                    if (form) {
                        form.$setDirty();
                        angular.forEach(form.$error, function (error) {
                            angular.forEach(error, function (field) {
                                field.$setDirty();
                            });
                        });
                        scope.$apply();

                        if (form.$invalid) {
                            angular.element('input[class~=ng-invalid]').first().focus();
                            return;
                        }
                    }
                    scope.$eval(attributes.crmCustomSubmit);
                    scope.$apply();
                });
            }
        };
    }
})();