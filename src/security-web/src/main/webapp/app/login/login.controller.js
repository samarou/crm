(function () {
    'use strict';
 
    angular
        .module('app')
        .controller('LoginController', LoginController);
        
    LoginController.$inject = ['$location', 'LoginService'];


	function LoginController($location, LoginService) {
		console.log("Login Controller");

		var vm = this;

		vm.login = login;

		function login() {
			console.log('login: ' + vm.username + " " + vm.password);
			LoginService.Login(vm.username, vm.password, function(response) {
				if(response.success) {
					$location.path('/users');
				} else {
					//Error
					console.log('Error');
				}
			});
		};
	}

 
})();