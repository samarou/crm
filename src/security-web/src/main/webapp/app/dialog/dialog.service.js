angular.module('app')
    .factory('DialogService', ['$uibModal', function ($uibModal) {
        return {
            notify: function (message) {
                return $uibModal.open({
                    templateUrl: 'app/dialog/notify.view.html',
                    controller: 'NotifyDialogController',
                    controllerAs: 'vm',
                    resolve: {
                        message: message
                    }
                });
            },
            error: function (message) {
                return $uibModal.open({
                    templateUrl: 'app/dialog/error.view.html',
                    controller: 'ErrorDialogController',
                    controllerAs: 'vm',
                    resolve: {
                        message: message
                    }
                });
            },
            confirm: function (message) {
                return $uibModal.open({
                    templateUrl: 'app/dialog/confirm.view.html',
                    controller: 'ConfirmDialogController',
                    controllerAs: 'vm',
                    resolve: {
                        message: message
                    }
                });
            },
            /**
             * Display a customized modal dialog
             * @param bodyUrl url of partial html that will be included as dialog body
             * @param model dialog model with fields:
             - title - dialog title
             - okTitle - title for OK button
             - cancelTitle - title for Cancel button
             */
            custom: function (bodyUrl, model) {
                return $uibModal.open({
                    templateUrl: bodyUrl,
                    windowTemplateUrl: 'app/dialog/custom.template.html',
                    controller: 'CustomDialogController',
                    controllerAs: 'vm',
                    keyboard: true,
                    backdrop: false,
                    size: model.size,//if undefined or null, then property will not used
                    resolve: {
                        model: model
                    }
                });
            }
        };
    }])
    .controller('ErrorDialogController', ['$modalInstance', 'message', function ($modalInstance, message) {
        this.message = message || 'An unknown error has occurred.';
        this.close = function () {
            $modalInstance.close();
        };
    }])
    .controller('NotifyDialogController', ['$modalInstance', 'message', function ($modalInstance, message) {
        this.message = message || 'Unknown application notification.';
        this.close = function () {
            $modalInstance.close();
        };
    }])
    .controller('ConfirmDialogController', ['$modalInstance', 'message', function ($modalInstance, message) {
        this.message = message || 'Are you sure?';
        $scope.no = function () {
            $modalInstance.dismiss(false);
        };
        $scope.yes = function () {
            $modalInstance.close(true);
        };
    }])
    .controller("CustomDialogController", ['$scope', '$uibModalInstance', 'model',
        function ($scope, $uibModalInstance, model) {
            angular.extend($scope, model);
            $scope.title = model.title || 'Dialog';
            $scope.okTitle = model.okTitle || 'OK';
            $scope.cancelTitle = model.cancelTitle || 'Cancel';
            $scope.ok = function () {
                $uibModalInstance.close($scope);
            };
            $scope.cancel = function () {
                $uibModalInstance.dismiss();
            };
        }
    ]);