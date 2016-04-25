(function () {
	'use strict';

	angular
			.module('securityManagement')
			.component('customForm', customForm());

	/** @ngInject */
	function customForm() {
		return {
			restrict: 'E',
			transclude: true,
			bindings: {
				title: '<',
				submitText: '<',
				cancelText: '<',
				cancelBtn: '&',
				submitBtn: '&'
			},
			templateUrl: 'app/components/customForm/customForm.template.html'
		};
	}

})();