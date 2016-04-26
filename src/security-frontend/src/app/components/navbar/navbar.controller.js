(function () {
	'use strict';

	angular
			.module('crm.navbar')
			.controller('AppController', navBarController);

	function navBarController(authService, $state, $location) {
		var vm = this;
		vm.isAdmin = authService.isAdmin;
		vm.isManager = authService.isManager;
		vm.isSpecialist = authService.isSpecialist;
		vm.isActive = isActive;
		vm.isLoggedUser = isLoggedUser;
		vm.getUserName = getUserName;
		vm.logout = logout;

		function isActive(path) {
			return $location.path().substr(0, path.length) === path;
		}

		function isLoggedUser() {
			return authService.isAuthenticated();
		}

		function getUserName() {
			var auth = authService.getAuthentication();
			return auth ? auth.username : null;
		}

		function logout() {
			authService.logout();
			$state.go('login');
		}
	}
})();