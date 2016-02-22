/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("AppController", ["$scope", "$location",
    function ($scope, $location) {
        "use strict";
        console.log("AppController");
        $scope.partials = {
            menu: "app/partials/menu.partial.html",
            groupModalView: "/app/group/group.modal.view.html"
        };

        $scope.isActive = function (path) {
            return $location.path().substr(0, path.length) === path ? "active" : "";
        }
    }]);