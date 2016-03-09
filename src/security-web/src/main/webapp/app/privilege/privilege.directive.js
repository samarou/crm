/**
 * @author yauheni.putsykovich
 */

angular.module("app").directive("privileges", [function () {
    return {
        scope: {
            objectTypes: "="
        },
        require: "objectTypes",
        restrict: "E",
        replace: true,
        templateUrl: "app/privilege/privilege.partial.view.html",
        controller: ["$scope", function ($scope) {
            $scope.selectAll = function (objectType, checked) {
                objectType.actions.forEach(function (action) {
                    action.privilege.checked = checked;
                })
            };
            $scope.selectOne = function (objectType) {
                objectType.isSelectAll = objectType.actions.every(function (action) {
                    return action.privilege.checked;
                });
            };
        }]
    }
}]);
