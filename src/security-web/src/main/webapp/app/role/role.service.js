/**
 * @author yauheni.putsykovich
 */
angular.module("app").factory("RoleService", ["$http", "$q", "Handler",
    function ($http, $q, Handler) {
        this.fetchAll = function () {
            var deferred = $q.defer();
            $http.get("/rest/roles").then(deferred.resolve, deferred.reject);
            deferred.promise.catch(Handler.handleError("Error during fetching roles"));
            return deferred.promise;
        };

        this.create = function (group, successHandler) {
            $http.post("/rest/roles", group).then(successHandler, Handler.handleError("Creating of role fails"));
        };

        this.update = function (group, successHandler) {
            $http.put("/rest/roles", group).then(successHandler, Handler.handleError("Update role failed"));
        };

        return this;
    }]);
