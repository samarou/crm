(function () {
	'use strict';

	angular
			.module('crm.dialog')
			.factory('dialogService', dialogService)
			.controller('ErrorDialogController', ErrorDialogController)
			.controller('NotifyDialogController', NotifyDialogController)
			.controller('ConfirmDialogController', ConfirmDialogController)
			.controller('CustomDialogController', CustomDialogController);

	/** @ngInject */
	function dialogService($uibModal) {
		return {
			notify: notify,
			error: error,
			confirm: confirm,
			custom: custom
		};

		function notify(message) {
			return $uibModal.open({
				templateUrl: 'app/components/dialog/notify.view.html',
				controller: 'NotifyDialogController',
				controllerAs: 'vm',
				resolve: {
					message: function () {
						return message;
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
						return message;
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
						return message;
					}
				}
			});
		}

		function custom(bodyUrl, model) {
			return $uibModal.open({
				templateUrl: bodyUrl,
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

	/** @ngInject */
	function CustomDialogController($uibModalInstance, model) {
		var vm = this;
		angular.extend(vm, model);
		vm.title = model.title || 'Dialog';
		vm.okTitle = model.okTitle;
		vm.cancelTitle = model.cancelTitle;
		vm.ok = function () {
			$uibModalInstance.close(vm);
		};
		vm.cancel = function () {
			$uibModalInstance.dismiss();
		};
	}

})();