/**
 * @author yauheni.putsykovich
 */
angular.module("app").factory("RoleService", ["$http", "$q", "Handler",
    function ($http, $q, Handler) {
        this.fetchAll = fetchAll;

        return this;

        function fetchAll() {
            var deferred = $q.defer();
            $http.get("/rest/roles").then(deferred.resolve, deferred.reject);
            deferred.promise.catch(Handler.handleError("Error during fetching roles"));
            return deferred.promise;
        }
    }]);
