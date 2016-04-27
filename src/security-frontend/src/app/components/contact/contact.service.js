(function () {
	'use strict';

	angular
			.module('crm.contact')
			.factory('contactService', contactService);

	/** @ngInject */
	function contactService($http) {
		return {
			getAll: getAll,
			create: create,
			get: get,
			update: update,
			remove: remove,
			find: find,
			getPermissions: getPermissions,
			updatePermissions: updatePermissions,
			removePermissions: removePermissions,
			isAllowed: isAllowed
		};

		function getAll() {
			return $http.get('rest/contacts');
		}

		function create(contact) {
			return $http.post('rest/contacts', contact);
		}

		function get(id) {
			return $http.get('rest/contacts/' + id);
		}

		function update(contact) {
			return $http.put('rest/contacts', contact);
		}

		function remove(id) {
			return $http.delete('rest/contacts/' + id);
		}

		function find(filter) {
			return $http.get('rest/contacts/find', {params: filter});
		}

		function getPermissions(id) {
			return $http.get('rest/contacts/' + id + '/permissions');
		}

		function updatePermissions(id, permissions) {
			return $http.put('rest/contacts/' + id + '/permissions', permissions);
		}

		function removePermissions(id, permissionId) {
			return $http.delete('rest/contacts/' + id + '/permissions/' + permissionId)
		}

		function isAllowed(contactId, permission) {
			return $http.get('rest/contacts/' + contactId + '/actions/' + permission);
		}
	}
})();