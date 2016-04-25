/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('AppController', navBarController);

	function navBarController(authService, $state, $location) {
		'use strict';

		var vm = this;

		vm.isActive = function (path) {
			return $location.path().substr(0, path.length) === path;
		};

		vm.isLoggedUser = function () {
			return authService.isAuthenticated();
		};

		vm.getUserName = function () {
			var auth = authService.getAuthentication();
			return auth ? auth.username : null;
		};

		vm.logout = function () {
			authService.logout();
			$state.go('login');
		};

		vm.isAdmin = authService.isAdmin;
		vm.isManager = authService.isManager;
		vm.isSpecialist = authService.isSpecialist;
	}
})();