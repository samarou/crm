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
			getAcls: getAcls,
			updateAcls: updateAcls,
			removeAcl: removeAcl,
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

		function getAcls(id) {
			return $http.get('rest/contacts/' + id + '/acls');
		}

		function updateAcls(id, acls) {
			return $http.put('rest/contacts/' + id + '/acls', acls);
		}

		function removeAcl(id, aclId) {
			return $http.delete('rest/contacts/' + id + '/acls/' + aclId)
		}

		function isAllowed(contactId, permission) {
			return $http.get('rest/contacts/' + contactId + '/actions/' + permission);
		}
	}
})();
