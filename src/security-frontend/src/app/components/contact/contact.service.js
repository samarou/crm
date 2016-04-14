/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';
	angular
			.module('securityManagement')
			.service('ContactService', ContactService);

	/** @ngInject */
	function ContactService($http) {
		var service = this;
		service.getAll = function () {
			return $http.get('rest/contacts');
		};

		service.create = function (contact) {
			return $http.post('rest/contacts', contact);
		};

		service.get = function (id) {
			return $http.get('rest/contacts/' + id);
		};

		service.update = function (contact) {
			return $http.put('rest/contacts', contact);
		};

		service.remove = function (id) {
			return $http.delete('rest/contacts/' + id);
		};

		service.find = function (filter) {
			return $http.get('rest/contacts/find', {params: filter});
		};

		service.getPermissions = function (id) {
			return $http.get('rest/contacts/' + id + '/permissions');
		};

		service.updatePermissions = function (id, permissions) {
			return $http.put('rest/contacts/' + id + '/permissions', permissions);
		};

		service.removePermissions = function (id, permissionId) {
			return $http.delete('rest/contacts/' + id + '/permissions/' + permissionId)
		};

		service.isAllowed = function (contactId, permission) {
			return $http.get('rest/contacts/' + contactId + '/actions/' + permission);
		};
	}
})();