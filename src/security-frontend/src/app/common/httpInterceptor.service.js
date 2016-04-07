(function () {
	'use strict';

	angular.module('securityManagement').factory('HttpInterceptor', HttpInterceptor);

	/** @ngInject */
	function HttpInterceptor($q, $log, $injector) {
		return {
			responseError: function (response) {
				if (response.status === 401) {
					var AuthService = $injector.get('AuthService');
					if (AuthService.isAuthenticated()) {
						AuthService.logout();
					}
				}
				$log.info('Redirect unauthorized to login');
				$injector.get('$state').transitionTo('login');
				return $q.reject(response);
			}
		}
	}

})();
