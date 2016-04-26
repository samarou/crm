(function () {
	'use strict';

	angular
			.module('crm')
			.directive('navBar', navBar);

	/** @ngInject */
	function navBar() {
		return {
			restrict: 'E',
			templateUrl: 'app/components/navbar/navbar.template.html'
		};
	}
})();