(function () {
	'use strict';

	angular
			.module('crm.privilege')
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

