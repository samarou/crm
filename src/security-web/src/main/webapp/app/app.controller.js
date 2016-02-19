/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("AppController", ["$scope", "AuthService", function ($scope, AuthService) {
    "use strict";

    console.log("AppController");

    if (!AuthService.isAuthenticated()) {
        console.log("Restore token");
        AuthService.restore();
    }

    $scope.partials = {
        menu: "app/partials/menu.partial.html",
        groupModalView: "/app/group/group.modal.view.html"
    };
}]);