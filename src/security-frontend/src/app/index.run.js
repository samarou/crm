(function () {
	'use strict';

	angular.module('crm').run(run);

	/** @ngInject */
	function run($rootScope, $state, authService, $log) {
		var callback = $rootScope.$on('$stateChangeStart', function (event, next) {
			if (next.name == 'login') {
				authService.logout();
			} else if (!authService.isAuthenticated()) {
				$log.log('Try to restore token');
				if (!authService.restore()) {
					event.preventDefault();
					$state.go('login');
				}
			}
			if (next.name == 'home') {
				event.preventDefault();
				$state.go(authService.isAdmin() ? 'users.list' : 'contacts.list');
			}
		});

		$rootScope.$on('$destroy', callback);
	}
})();
