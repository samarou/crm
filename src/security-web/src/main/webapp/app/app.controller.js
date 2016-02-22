/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("AppController", ["AuthService", function (AuthService) {
    "use strict";

    var vm = this;

    console.log("AppController");

    if (!AuthService.isAuthenticated()) {
        console.log("Restore token");
        AuthService.restore();
    }

    vm.partials = {
        menu: "app/partials/menu.partial.html",
        groupModalView: "/app/group/group.modal.view.html"
    };
}]);