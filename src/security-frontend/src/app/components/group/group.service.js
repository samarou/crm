(function () {
	'use strict';

	angular
			.module('crm')
			.factory('groupService', groupService);

	/** @ngInject */
	function groupService($http) {
		return {
			getAll: getAll,
			getPublicGroups: getPublicGroups,
			get: get,
			create: create,
			update: update,
			remove: remove
		};

		function getAll() {
			return $http.get('rest/groups');
		}

		function getPublicGroups() {
			return $http.get('rest/groups/public');
		}

		function get(id) {
			return $http.get('rest/groups/' + id);
		}

		function create(group) {
			return $http.post('rest/groups', group);
		}

		function update(group) {
			return $http.put('rest/groups', group);
		}

		function remove(id) {
			return $http.delete('rest/groups/' + id);
		}
	}
})();
