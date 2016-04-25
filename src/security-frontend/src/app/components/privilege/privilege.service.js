/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('privilegeService', privilegeService);

	/** @ngInject */
	function privilegeService($http) {

		function getAll() {
			return $http.get('rest/privileges');
		}

		return {
			getAll: getAll
		}
	}
})();

