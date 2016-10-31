(function(){
    'use strict';

    angular
        .module('crm.contact')
        .filter('convertToHtml', convertToHtml);

    /** @ngInject */
    function convertToHtml($sce){
        return function(htmlCode){
            return $sce.trustAsHtml(htmlCode);
        }
    }
})();
