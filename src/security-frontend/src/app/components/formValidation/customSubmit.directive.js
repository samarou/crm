(function () {
	'use strict';

	angular
			.module('securityManagement')
			.directive('customSubmit', customSubmit);

	/** @ngInject */
	function customSubmit() {
		return {
			restrict: 'A',
			link: function (scope, element, attributes) {
				var $element = angular.element(element);

				attributes.$set('novalidate', 'novalidate');
				$element.find('.ng-pristine').removeClass('ng-pristine');

				$element.bind('submit', function (e) {
					e.preventDefault();

					var form = scope[attributes.name];
					form.$pristine = false;
					form.$dirty = true;
					angular.forEach(form.$error, function (error) {
						angular.forEach(error, function (field) {
							field.$setDirty();
						});
					});
					scope.$digest();

					if (form.$invalid) {
						$element.find('.ng-invalid').first().focus();
						return;
					}

					scope.$eval(attributes.customSubmit);
					scope.$apply();
				});
			}
		}
	}
})();