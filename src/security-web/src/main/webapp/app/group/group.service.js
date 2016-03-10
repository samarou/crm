/**
 * @author yauheni.putsykovich
 */
angular.module("app").factory("GroupService", ["$http", function ($http) {
    "use strict";
    this.getAll = function () {
        return $http.get("/rest/groups");
    };

    this.getPublicGroups = function () {
        return $http.get("/rest/groups/public");
    };

    this.create = function (group) {
        return $http.post("/rest/groups", group);
    };

    this.update = function (group) {
        return $http.put("/rest/groups", group);
    };

    this.remove = function (id) {
        return $http.delete("/rest/groups/" + id);
    };

    return this;
}]);
