(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('validateUrl', isValidateUrl);

    function isValidateUrl() {
        return {
            require: 'ngModel',
            link: function(scope, element, attr, ctrl) {
                function myValidation(value) {
                    var data=attr.validateUrl;
                    if(!data){
                        ctrl.$setValidity('isSocialNetworkUrl', false);
                    }else {
                        if (data !== 'Other') {
                            var patt = new RegExp(".*" + data.toLowerCase() + ".*");
                            if (patt.test(value)) {
                                ctrl.$setValidity('isSocialNetworkUrl', true);
                            } else {
                                ctrl.$setValidity('isSocialNetworkUrl', false);
                            }
                        }
                    }
                    return value;
                }
                ctrl.$parsers.push(myValidation);
            }
        };
    }
})();
