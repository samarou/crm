(function () {
	'use strict';

	angular.module('securityManagement').run(run);

	/** @ngInject */
	function run($rootScope, $state, AuthService, $log) {
		var callback = $rootScope.$on('$stateChangeStart', function (event, next) {
			if (next.controller == 'LoginController') {
				AuthService.logout();
			} else if (!AuthService.isAuthenticated()) {
				$log.log('Try to restore token');
				if (!AuthService.restore()) {
					$state.go('/login');
				}
			}
		});

		$rootScope.$on('$destroy', callback);
	}
})();
