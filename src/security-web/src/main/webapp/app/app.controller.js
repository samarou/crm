/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("AppController", ["$scope", function ($scope) {
    "use strict";
    console.log("AppController");
    $scope.partials = {
        menu: "app/partials/menu.partial.html",
        groupModalView: "/app/group/group.modal.view.html"
    };
}]);