(function () {
	'use strict';

	angular
			.module('crm.validation')
			.directive('crmInputValidation', crmInputValidation);

	/** @ngInject */
	function crmInputValidation() {
		return {
			restrict: 'A',
			require: '^form',
			link: function (scope, elem) {
				var formName = elem.parents('[ng-form]').length ? elem.parents('[ng-form]').attr('ng-form') : elem.parents('form').attr('name');
				var controlName = elem.find('.form-control').attr('name');
				elem.append('<span class="glyphicon form-control-feedback"></span>');
				elem.addClass('has-feedback');
				var icon = elem.find('.glyphicon');

				scope.$watch(formName + '.' + controlName + '.$dirty', function (newval) {
					if (newval) {
						watch();
					}
				});

				function watch() {
					scope.$watch(formName + '.' + controlName + '.$invalid', function (newval) {
						elem.toggleClass('has-error', newval);
						elem.toggleClass('has-success', !newval);
						icon.toggleClass('glyphicon-remove', newval);
						icon.toggleClass('glyphicon-ok', !newval);
					});
				}
			}
		};
	}
})();