(function () {
	'use strict';

	angular.module('securityManagement').factory('HttpInterceptor', HttpInterceptor);

	/** @ngInject */
	function HttpInterceptor($q, $log, $injector, toastr) {
		return {
			responseError: function (response) {
				switch (response.status) {
					case 401: {
						var AuthService = $injector.get('AuthService');
						if (AuthService.isAuthenticated()) {
							AuthService.logout();
						}
						$injector.get('$state').transitionTo('login');
						toastr.error('Your credentials are gone', 'Error');
						break;
					}
					default: {
						$log.error(response.status + ':' + response.data.type + ' ' + response.data.message);
						toastr.error('Something goes wrong', 'Error');
					}

				}
				return $q.reject(response);

			}
		}
	}

})();
