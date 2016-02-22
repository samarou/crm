/**
 * @author yauheni.putsykovich
 */
angular.module("app").controller("AppController", ["AuthService", "$location",
    function (AuthService, $location) {
        "use strict";

        var vm = this;

        console.log("AppController");

        if (!AuthService.isAuthenticated()) {
            console.log("Restore token");
            AuthService.restore();
        }
    }]);