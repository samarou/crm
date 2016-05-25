(function () {
    'use strict';

    angular
        .module('crm')
        .config(config);

    /** @ngInject */
    function config($logProvider, $uibModalProvider, $httpProvider, toastrConfig) {
        // Enable log
        $logProvider.debugEnabled(true);

        $httpProvider.interceptors.push('httpInterceptor');

        $uibModalProvider.options = {backdrop: 'static', keyboard: false};

        toastrConfig.positionClass = 'toast-top-right';
        toastrConfig.preventDuplicates = false;
        toastrConfig.closeButton = true;
    }

})();
