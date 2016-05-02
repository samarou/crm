(function () {
	'use strict';

	angular
			.module('crm.search')
			.component('crmSearch', footer());

	/** @ngInject */
	function footer() {
		return {
			restrict: 'E',
			bindings: {
				scope: '='
			},
			templateUrl: 'app/components/search/search.template.html'
		};
	}
})();