(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('LoginController', LoginController);

	function LoginController($state, AuthService) {
		var vm = this;
		vm.login = function () {
			AuthService.login(vm.username, vm.password).then(
					function () {
						$state.go(AuthService.isAdmin() ? 'users.list' : 'contacts.list');
					}).catch(
					function () {
						vm.error = 'Invalid login or password';
					});

		}
	}
})();