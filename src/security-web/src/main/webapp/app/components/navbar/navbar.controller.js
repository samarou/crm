/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';
	angular
			.module('app')
			.controller('AppController', navBarController);

	function navBarController(AuthService, $location) {

		var vm = this;

		vm.isActive = function (path) {
			return $location.path().substr(0, path.length) === path;
		};

		vm.isLoggedUser = function () {
			return AuthService.isAuthenticated();
		};

		vm.getUserName = function () {
			var auth = AuthService.getAuthentication();
			return auth ? auth.username : null;
		};

		vm.logout = function () {
			AuthService.logout();
			$location.path('/login');
		};

		vm.isAdmin = AuthService.isAdmin;
		vm.isManager = AuthService.isManager;
		vm.isSpecialist = AuthService.isSpecialist;
	}
})();