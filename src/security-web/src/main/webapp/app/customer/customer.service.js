/**
 * @author yauheni.putsykovich
 */

angular.module("app").service("CustomerService", ["$http", function ($http) {
    this.getAll = function () {
        return $http.get("/rest/customers");
    };

    this.create = function (customer) {
        return $http.post("/rest/customer", customer);
    };

    this.update = function (data) {
        return $http.put("/rest/customer", data);
    };

    this.getPermissions = function (id) {
        return $http.get("/rest/customer/" + id + "/permissions");
    };

    this.addPermission = function (id, acl) {
        return $http.post("/rest/customer/" + id + "/permissions", acl);
    };
}]);