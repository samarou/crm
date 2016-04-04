angular.module('app')
    .factory('DialogService', ['$uibModal', function ($uibModal) {
        return {
            notify: function (message) {
                return $uibModal.open({
                    templateUrl: 'app/dialog/notify.view.html',
                    controller: 'NotifyDialogController',
                    controllerAs: 'vm',
                    resolve: {
                        message: function () {
                            return message
                        }
                    }
                });
            },
            error: function (message) {
                return $uibModal.open({
                    templateUrl: 'app/dialog/error.view.html',
                    controller: 'ErrorDialogController',
                    controllerAs: 'vm',
                    resolve: {
                        message: function () {
                            return message
                        }
                    }
                });
            },
            confirm: function (message) {
                return $uibModal.open({
                    templateUrl: 'app/dialog/confirm.view.html',
                    controller: 'ConfirmDialogController',
                    controllerAs: 'vm',
                    resolve: {
                        message: function () {
                            return message
                        }
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
    .controller('ErrorDialogController', ['$uibModalInstance', 'message', function ($uibModalInstance, message) {
        this.message = message || 'An unknown error has occurred.';
        this.close = function () {
            $uibModalInstance.close();
        };
    }])
    .controller('NotifyDialogController', ['$uibModalInstance', 'message', function ($uibModalInstance, message) {
        this.message = message || 'Unknown application notification.';
        this.close = function () {
            $uibModalInstance.close();
        };
    }])
    .controller('ConfirmDialogController', ['$uibModalInstance', 'message', function ($uibModalInstance, message) {
        this.message = message || 'Are you sure?';
        $scope.no = function () {
            $uibModalInstance.dismiss(false);
        };
        $scope.yes = function () {
            $uibModalInstance.close(true);
        };
    }])
    .controller("CustomDialogController", ['$scope', '$uibModalInstance', 'model',
        function ($scope, $uibModalInstance, model) {
            angular.extend($scope, model);
            $scope.title = model.title || 'Dialog';
            $scope.okTitle = model.okTitle || 'OK';
            $scope.cancelTitle = model.cancelTitle || 'Cancel';
            //TODO There shouldn't be customer logic!
            $scope.ok = function () {
                var as;
                if (model.bundle) as = model.bundle.actions;
                var hasNotClickOkListener = !as || !as.clickOkListener;
                if (hasNotClickOkListener) {
                    $uibModalInstance.close($scope);
                } else {
                    var okListenerTrue = !!as.clickOkListener && as.clickOkListener(model);
                    if (okListenerTrue) $uibModalInstance.close($scope);
                }
            };
            $scope.cancel = function () {
                $uibModalInstance.dismiss();
            };
        }
    ]);