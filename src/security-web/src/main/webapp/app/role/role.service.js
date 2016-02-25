/**
 * @author yauheni.putsykovich
 */
angular.module("app").factory("RoleService", ["$http", function ($http) {
    this.fetchAll = function () {
        return $http.get("/rest/roles");
    };

    this.create = function (group) {
        $http.post("/rest/roles", group);
    };

    this.update = function (group) {
        $http.put("/rest/roles", group);
    };

    return this;
}]);
