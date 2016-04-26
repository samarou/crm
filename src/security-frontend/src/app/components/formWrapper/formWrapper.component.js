(function () {
	'use strict';

	angular
			.module('securityManagement')
			.component('formWrapper', formWrapper());

	/** @ngInject */
	function formWrapper() {
		return {
			restrict: 'E',
			transclude: true,
			bindings: {
				formTitle: '<',
				submitText: '@',
				cancelText: '@',
				formName: '=',
				cancelBtn: '&',
				submitBtn: '&',
				modal: '@'
			},
			templateUrl: 'app/components/formWrapper/formWrapper.template.html'
		};
	}

})();