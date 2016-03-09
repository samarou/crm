/**
 * @author yauheni.putsykovich
 */

angular.module("app").service("CustomerService", ["$http", function ($http) {
    this.fetchAll = function () {
        return $http.get("/rest/customers");
    };

    this.getRights = function (id) {
        return $http.get("/rest/customer/" + id + "/rights");
    }
}]);