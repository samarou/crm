(function () {
	'use strict';

	angular
			.module('securityManagement')
			.component('crmFormWrapper', crmFormWrapper());

	/** @ngInject */
	function crmFormWrapper() {
		return {
			restrict: 'E',
			transclude: true,
			bindings: {
				formTitle: '<',
				submitText: '@',
				cancelText: '@',
				crmFormValidation: '=',
				cancelBtn: '&',
				submitBtn: '&',
				modal: '@'
			},
			templateUrl: 'app/components/formWrapper/formWrapper.template.html'
		};
	}

})();