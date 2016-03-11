/**
 * @author yauheni.putsykovich
 */

angular.module("app").service("CustomerService", ["$http", function ($http) {
    this.getAll = function () {
        return $http.get("/rest/customers");
    };

    this.create = function (customer) {
        return $http.post("/rest/customers", customer);
    };

    this.update = function (customer) {
        return $http.put("/rest/customers", customer);
    };

    this.remove = function (id) {
        return $http.delete("/rest/customers/" + id);
    };

    this.find = function (filter) {
        return $http.get("/rest/customers/find", {params: filter});
    };

    this.getPermissions = function (id) {
        return $http.get("/rest/customers/" + id + "/permissions");
    };

    this.updatePermissions = function (id, permissions) {
        return $http.put("/rest/customers/" + id + "/permissions", permissions);
    };

    this.removePermissions = function (id, permissionId) {
        return $http.delete("/rest/customers/" + id + "/permissions/" + permissionId)
    }
}]);