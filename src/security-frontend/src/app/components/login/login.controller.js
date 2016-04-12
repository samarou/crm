(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('LoginController', LoginController);

	function LoginController($state, AuthService, $log) {
		var vm = this;
		vm.login = function () {
			AuthService.login(vm.username, vm.password).then(
					function () {
						$state.go(AuthService.isAdmin() ? 'users.list' : 'contacts.list');
					},
					function (error) {
						$log.error(error);
						vm.error = 'Invalid login or password';
					});
		};
	}
})();