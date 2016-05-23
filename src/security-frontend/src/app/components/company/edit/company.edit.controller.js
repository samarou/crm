(function () {
	'use strict';

	angular
			.module('crm.company')
			.controller('companyEditController', companyEditController);

	/** @ngInject */
	function companyEditController(companyService, companyDetailsService, $stateParams) {
		var vm = this;

		vm.company = {};
		vm.submitText = 'Edit';
		vm.title = 'Edit company';
		vm.submit = submit;
		vm.cancel = companyDetailsService.cancel;
		vm.staticData = companyDetailsService.staticData;

		init();

		function init() {
			companyService.get($stateParams.id).then(function (response) {
				vm.company = response.data;
			});
		}

		function submit() {
			companyDetailsService.submit(vm, false);
		}
	}

})();