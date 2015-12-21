(function() {
	'use strict';
	
	angular
		.module('app')
		.factory('LoginService', LoginService);
		
		
	
	LoginService.$inject = ['$http', '$rootScope', 'UserService'];

	function LoginService($http, $rootScope, UserService) {
		console.log("LoginService");

		var service = {};

		service.Login = Login;
		return service;

		function Login(username, password, callback) {
			$http.post('/login', {
				username : username,
				password : password
			}).success(function(response) {
				callback(response);
			});
		}

	}

})();
