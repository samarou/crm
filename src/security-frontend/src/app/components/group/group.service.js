/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('GroupService', GroupService);

	/** @ngInject */
	function GroupService($http) {
		var service = this;
		service.getAll = function () {
			return $http.get('rest/groups');
		};

		service.getPublicGroups = function () {
			return $http.get('rest/groups/public');
		};

		service.create = function (group) {
			return $http.post('rest/groups', group);
		};

		service.update = function (group) {
			return $http.put('rest/groups', group);
		};

		service.remove = function (id) {
			return $http.delete('rest/groups/' + id);
		};

		return service;
	}
})();
