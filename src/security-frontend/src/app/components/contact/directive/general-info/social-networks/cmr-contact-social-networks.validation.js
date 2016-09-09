/**
 * Created by vladzislau.drabkou on 9/8/2016.
 */
(function () {
    'use strict';
    angular
        .module('crm.contact')
        .directive('validateUrl', crmSocialNetworkValidator);

    function crmSocialNetworkValidator() {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ctrl) {

                function isValid(value) {
                    var data = attr.validateUrl;
                    ctrl.$setValidity('validUrl', true);
                    if (!data) {
                        ctrl.$setValidity('validUrl', false);
                    } else {
                        if (data !== 'Other') {
                            var regExp = new RegExp(".*" + data.toLowerCase() + ".*", "i");
                            ctrl.$setValidity('validUrl', regExp.test(value));
                        }

                    }
                    return value;
                }

                ctrl.$parsers.push(isValid);
            }
        };
    }

})();
