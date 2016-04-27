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

		service.getAcls = function (id) {
			return $http.get('rest/contacts/' + id + '/acls');
		};

		service.updateAcls = function (id, acls) {
			return $http.put('rest/contacts/' + id + '/acls', acls);
		};

		service.removeAcl = function (id, aclId) {
			return $http.delete('rest/contacts/' + id + '/acls/' + aclId)
		};

		service.isAllowed = function (contactId, permission) {
			return $http.get('rest/contacts/' + contactId + '/actions/' + permission);
		};
	}
})();
