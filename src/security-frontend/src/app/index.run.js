(function () {
    'use strict';

    angular.module('crm').run(run);

    /** @ngInject */
    function run($rootScope, $state, authService) {
        authService.getAuthStatus().then(authService.restore,
            function () {
                event.preventDefault();
                $state.go('login');
            });

        var callback = $rootScope.$on('$stateChangeStart', listenRouteChange);

        $rootScope.$on('$destroy', callback);

        function listenRouteChange(event, next) {
            if (next.name == 'home') {
                event.preventDefault();
                $state.go(authService.isAdmin() ? 'users.list' : 'contacts.list');
            }
        }
    }
})();
