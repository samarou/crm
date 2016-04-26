(function () {
	'use strict';

	angular
			.module('securityManagement')
			.directive('navBar', navBar);

	/** @ngInject */
	function navBar() {
		return {
			restrict: 'E',
			templateUrl: 'app/components/navbar/navbar.template.html'
		};
	}
})();