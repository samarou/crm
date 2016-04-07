(function () {
	'use strict';

	angular
			.module('securityManagement')
			.config(config);

	/** @ngInject */
	function config($logProvider, $uibModalProvider, $httpProvider) {
		// Enable log
		$logProvider.debugEnabled(true);

		$httpProvider.interceptors.push('HttpInterceptor');

		$uibModalProvider.options = {backdrop: 'static', keyboard: false};
	}

})();
