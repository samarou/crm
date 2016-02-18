/**
 * @author yauheni.putsykovich
 */
angular.module("app").factory("GroupService", ["$http", "$q", "Handler", function ($http, $q, Handler) {
    "use strict";
    this.fetchAll = function fetchAll() {
        var deferred = $q.defer();
        $http.get("/rest/group").then(deferred.resolve, deferred.reject);
        deferred.promise.catch(Handler.handleError("Error during fetching groups"));
        return deferred.promise;
    };

    return this;
}]);
