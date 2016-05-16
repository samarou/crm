(function () {
    'use strict';

    angular
        .module('crm.common')
        .factory('httpInterceptor', httpInterceptor);

    /** @ngInject */
    function httpInterceptor($q, $log, $injector, toastr) {
        return {
            responseError: catchError
        };
        function catchError(response) {
            switch (response.status) {
                case 401: {
                    catchAuthError();
                    break;
                }
                default: {
                    catchDefaultError(response);
                }
            }
            return $q.reject(response);
        }

        function catchAuthError() {
            var AuthService = $injector.get('authService');
            if (AuthService.isAuthenticated()) {
                AuthService.logout();
            }
            $injector.get('$state').transitionTo('login');
            toastr.error('Authentication problem', 'Error');
        }

        function catchDefaultError(response) {
            $log.error(response.status + ':' + response.data.type + ' ' + response.data.message);
            toastr.error(response.data.message || 'Something goes wrong', 'Error');
        }
    }

})();
