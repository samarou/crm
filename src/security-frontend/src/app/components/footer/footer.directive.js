(function () {
	'use strict';

	angular
			.module('crm.footer')
			.directive('crmFooter', footer);

	/** @ngInject */
	function footer() {
		return {
			restrict: 'E',
			templateUrl: 'app/components/footer/footer.template.html'
		};
	}
})();