(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('privilegeService', privilegeService);

	/** @ngInject */
	function privilegeService($http) {
		return {
			getAll: getAll
		};

		function getAll() {
			return $http.get('rest/privileges');
		}
	}
})();

