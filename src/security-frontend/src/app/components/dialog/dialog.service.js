(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('dialogService', dialogService)
			.controller('ErrorDialogController', ErrorDialogController)
			.controller('NotifyDialogController', NotifyDialogController)
			.controller('ConfirmDialogController', ConfirmDialogController)
			.controller('CustomDialogController', CustomDialogController);

	/** @ngInject */
	function dialogService($uibModal) {

		function notify(message) {
			return $uibModal.open({
				templateUrl: 'app/components/dialog/notify.view.html',
				controller: 'NotifyDialogController',
				controllerAs: 'vm',
				resolve: {
					message: function () {
						return message
					}
				}
			});
		}

		function error(message) {
			return $uibModal.open({
				templateUrl: 'app/components/dialog/error.view.html',
				controller: 'ErrorDialogController',
				controllerAs: 'vm',
				resolve: {
					message: function () {
						return message
					}
				}
			});
		}

		function confirm(message) {
			return $uibModal.open({
				templateUrl: 'app/components/dialog/confirm.view.html',
				controller: 'ConfirmDialogController',
				controllerAs: 'vm',
				resolve: {
					message: function () {
						return message
					}
				}
			});
		}

		/**
		 * Display a customized modal dialog
		 * @param bodyUrl url of partial html that will be included as dialog body
		 * @param model dialog model with fields:
		 - title - dialog title
		 - okTitle - title for OK button
		 - cancelTitle - title for Cancel button
		 */
		function custom(bodyUrl, model) {
			return $uibModal.open({
				templateUrl: bodyUrl,
				windowTemplateUrl: 'app/components/dialog/custom.template.html',
				controller: 'CustomDialogController',
				controllerAs: 'vm',
				keyboard: true,
				backdrop: false,
				size: model.size,// if undefined or null, then property will not used
				resolve: {
					model: model
				}
			});
		}

		return {
			notify: notify,
			error: error,
			confirm: confirm,
			custom: custom
		};
	}

	/** @ngInject */
	function ErrorDialogController($uibModalInstance, message) {
		var vm = this;
		vm.message = message || 'An unknown error has occurred.';
		vm.close = function () {
			$uibModalInstance.close();
		};
	}

	/** @ngInject */
	function NotifyDialogController($uibModalInstance, message) {
		var vm = this;
		vm.message = message || 'Unknown application notification.';
		vm.close = function () {
			$uibModalInstance.close();
		};
	}

	/** @ngInject */
	function ConfirmDialogController($uibModalInstance, message) {
		var vm = this;
		vm.message = message || 'Are you sure?';
		vm.no = function () {
			$uibModalInstance.dismiss(false);
		};
		vm.yes = function () {
			$uibModalInstance.close(true);
		};
	}

	// todo: fix scope using
	/*eslint-disable */
	/** @ngInject */
	function CustomDialogController($scope, $uibModalInstance, model) {
		angular.extend($scope, model);
		$scope.title = model.title || 'Dialog';
		$scope.okTitle = model.okTitle;
		$scope.cancelTitle = model.cancelTitle;
		$scope.ok = function () {
			$uibModalInstance.close($scope);
		};
		$scope.cancel = function () {
			$uibModalInstance.dismiss();
		};
	}

	/*eslint-enable */

})();