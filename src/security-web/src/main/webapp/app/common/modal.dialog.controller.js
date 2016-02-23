angular.module("app").controller("ModalDialogController", ['$scope', '$uibModalInstance', 'model',
    function ($scope, $uibModalInstance, model) {
        "use strict";

        angular.extend($scope, model);

        $scope.ok = function () {
            $uibModalInstance.close($scope);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss();
        };
    }
]);