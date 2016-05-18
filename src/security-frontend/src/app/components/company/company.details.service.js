(function () {
	'use strict';

	angular
			.module('crm.company')
			.factory('companyDetailsService', companyDetailsService)

	/** @ngInject */
	function companyDetailsService(companyService, $state) {
		return {
			submit: submit,
			cancel: goToList,
			staticData: companyService.staticData
		};

		function submit(scope, isNew) {
			if (isNew) {
				companyService.create(scope.company).then(goToList)
			} else {
				companyService.update(scope.company).then(goToList)
			}
		}

		function goToList() {
            $state.go('companies.list');
        }
	}
})();