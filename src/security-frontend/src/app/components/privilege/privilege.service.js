/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('privilegeService', PrivilegeService);

	/** @ngInject */
	function PrivilegeService($http) {
		'use strict';

		function getAll() {
			return $http.get('rest/privileges');
		}

		return {
			getAll: getAll
		}
	}
})();

