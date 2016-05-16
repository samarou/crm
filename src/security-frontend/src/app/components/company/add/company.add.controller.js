(function () {
	'use strict';
	
	angular
			.module('crm.company')
			.controller('companyAddController', companyAddController)
	
	/** @ngInject */
	function companyAddController(companyDetailsService) {
		var vm = this;
		
		vm.company = {};
		vm.submitText = 'Add';
		vm.title = 'Add company';
		vm.submit = submit;
		vm.cancel = companyDetailsService.cancel;
		
		function submit() {
			companyDetailsService.submit(vm, true);
		}
	}

})()