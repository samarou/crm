/**
 * @author yauheni.putsykovich
 */
angular.module("app").factory("GroupService", ["$http", function ($http) {
    "use strict";
    this.fetchAll = function fetchAll() {
        return $http.get("/rest/groups");
    };

    this.create = function (group) {
        return $http.post("/rest/groups", group);
    };

    this.update = function (group) {
        $http.put("/rest/groups", group);
    };

    return this;
}]);
