(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('LoginController', LoginController);

	function LoginController($state, authService) {
		var vm = this;
		vm.login = function () {
			authService.login(vm.username, vm.password).then(
					function () {
						$state.go(authService.isAdmin() ? 'users.list' : 'contacts.list');
					}).catch(
					function () {
						vm.error = 'Invalid login or password';
					});

		}
	}
})();