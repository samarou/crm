/**
 * @author yauheni.putsykovich
 */
angular.module("app").factory("RoleService", ["$http", function ($http) {
    this.fetchAll = function () {
        return $http.get("/rest/roles");
    };

    this.create = function (role) {
        return $http.post("/rest/roles", role);
    };

    this.update = function (role) {
        return $http.put("/rest/roles", role);
    };

    return this;
}]);
