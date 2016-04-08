/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';
	angular
			.module('securityManagement')
			.factory('RoleService', RoleService);

	/** @ngInject */
	function RoleService($http) {
		var service = this;
		service.fetchAll = function () {
			return $http.get('rest/roles');
		};

		service.create = function (role) {
			return $http.post('rest/roles', role);
		};

		service.update = function (role) {
			return $http.put('rest/roles', role);
		};

		service.remove = function (id) {
			return $http.delete('rest/roles/' + id);
		};

		return service;
	}
})();
