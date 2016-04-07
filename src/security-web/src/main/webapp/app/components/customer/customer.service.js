/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';
	angular
			.module('securityManagement')
			.service('CustomerService', CustomerService);

	/** @ngInject */
	function CustomerService($http) {
		var service = this;
		service.getAll = function () {
			return $http.get('/rest/customers');
		};

		service.create = function (customer) {
			return $http.post('/rest/customers', customer);
		};

		service.update = function (customer) {
			return $http.put('/rest/customers', customer);
		};

		service.remove = function (id) {
			return $http.delete('/rest/customers/' + id);
		};

		service.find = function (filter) {
			return $http.get('/rest/customers/find', { params: filter });
		};

		service.getPermissions = function (id) {
			return $http.get('/rest/customers/' + id + '/permissions');
		};

		service.updatePermissions = function (id, permissions) {
			return $http.put('/rest/customers/' + id + '/permissions', permissions);
		};

		service.removePermissions = function (id, permissionId) {
			return $http.delete('/rest/customers/' + id + '/permissions/' + permissionId)
		};

		service.isAllowed = function (customerId, permission) {
			return $http.get('/rest/customers/' + customerId + '/actions/' + permission);
		};
	}
})();