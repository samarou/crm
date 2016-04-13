/**
 * Created by anton.charnou on 13.04.2016.
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.component('formPanel', formPanel());

	/** @ngInject */
	function formPanel() {
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
			templateUrl: 'app/components/form/form.template.html'
		};
	}

})();