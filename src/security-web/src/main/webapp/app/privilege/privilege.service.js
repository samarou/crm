/**
 * @author yauheni.putsykovich
 */

angular.module("app").service("PrivilegeService", ["$http", function ($http) {
    "use strict";
    this.fetchAll = function () {
        return $http.get("/rest/privileges");
    };

    return this;
}]);
