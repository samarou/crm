/**
 * @author yauheni.putsykovich
 */

angular.module("app").service("CustomerService", ["$http", function ($http) {
    this.fetchAll = function () {
        return $http.get("/rest/customers");
    }
}]);