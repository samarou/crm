(function(){
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmImageUrlValidator', crmImageUrlValidator);

    function crmImageUrlValidator(){
        return{
            restrict:'A',
            require:'ngModel',
            link:function(scope, element, attrs, ngModel){

                ngModel.$parsers.unshift(function(value){
                    var valid = isValid(value);
                    console.log('valid - ' + valid);
                    ngModel.$setValidity('image_url_validation', valid);
                    return valid ? value :undefined;
                });

                ngModel.$formatters.unshift(function(value){
                    ngModel.$setValidity('image_url_validation', isValid(value));
                    return value;
                });

                function isValid(value){
                    var pattern, regExp;
                    pattern = "^(http|https):\/\/.+\$";
                    regExp = new RegExp(pattern, 'i');
                    return regExp.test(value);
                }
            }
        };
    }
})();
