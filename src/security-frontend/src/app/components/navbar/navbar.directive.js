(function () {
	'use strict';

	angular
			.module('crm.navbar')
			.directive('navBar', navBar);

	/** @ngInject */
	function navBar() {
		return {
			restrict: 'E',
			templateUrl: 'app/components/navbar/navbar.template.html'
		};
	}
})();