(function () {
	'use strict';

	angular.module('securityManagement').run(run);

	/** @ngInject */
	function run($rootScope, $state, AuthService, $log) {
		var callback = $rootScope.$on('$stateChangeStart', function (event, next) {
			if (next.name == 'login') {
				AuthService.logout();
			} else if (!AuthService.isAuthenticated()) {
				$log.log('Try to restore token');
				if (!AuthService.restore()) {
					event.preventDefault();
					$state.go('login');
				}
			}
			if (next.name == 'home') {
				event.preventDefault();
				$state.go(AuthService.isAdmin() ? 'users.list' : 'contacts.list');
			}
		});

		$rootScope.$on('$destroy', callback);
	}
})();
