/**
 * Created by anton.charnou on 06.04.2016.
 */
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