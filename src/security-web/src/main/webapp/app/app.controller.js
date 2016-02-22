/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("AppController", ["$scope", "$location",
    function ($scope, $location) {
        "use strict";
        $scope.isActive = function (path) {
            return $location.path().substr(0, path.length) === path ? "active" : "";
        }
    }]);