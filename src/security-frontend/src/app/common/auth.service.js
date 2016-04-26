(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('authService', authService);

	/** @ngInject */
	function authService($http, $q, $log) {
		var service = this;
		service.authentication = null;

		return {
			login: login,
			logout: logout,
			restore: restore,
			isAuthenticated: isAuthenticated,
			getAuthentication: getAuthentication,
			isAdmin: isAdmin,
			isManager: isManager,
			isSpecialist: isSpecialist
		};

		function setAuthentication(authData) {
			service.authentication = authData;
			$http.defaults.headers.common['X-Auth-Token'] =
					authData ? authData.token : undefined;
			if (sessionStorage) {
				if (authData) {
					sessionStorage.setItem('auth.data', angular.toJson(authData));
				} else {
					sessionStorage.removeItem('auth.data');
				}
			}
		}

		function login(username, password) {
			return $http.post('rest/login', {
				username: username,
				password: password
			}).then(function (response) {
				setAuthentication(response.data);
				return response;
			}, function (error) {
				setAuthentication(null);
				return $q.reject(error);
			});
		}

		function logout() {
			setAuthentication(null);
		}

		function restore() {
			if (sessionStorage) {
				var authData = sessionStorage.getItem('auth.data');
				if (authData) {
					try {
						setAuthentication(angular.fromJson(authData));
						return true;
					} catch (e) {
						$log.error(e);
					}
				}
			}
			return false;
		}

		function isAuthenticated() {
			return !!service.authentication;
		}

		function getAuthentication() {
			return angular.copy(service.authentication);
		}

		function hasRole(role) {
			return isAuthenticated()
					&& service.authentication
					&& service.authentication.roles
					&& service.authentication.roles.indexOf(role) !== -1;
		}

		function isAdmin() {
			return hasRole('ADMIN');
		}

		function isManager() {
			return hasRole('MANAGER');
		}

		function isSpecialist() {
			return hasRole('SPECIALIST');
		}
	}
})();
