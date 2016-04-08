/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.service('PrivilegeService', PrivilegeService);

	/** @ngInject */
	function PrivilegeService($http) {
		'use strict';
		this.fetchAll = function () {
			return $http.get('rest/privileges');
		};

		return this;
	}
})();

