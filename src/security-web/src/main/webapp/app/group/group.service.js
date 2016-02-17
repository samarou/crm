/**
 * @author yauheni.putsykovich
 */
"use strict";
angular.module("app").factory("GroupService", ["$http", "$q", "Handler",
    function ($http, $q, Handler) {
        this.fetchAll = fetchAll;

        return this;

        function fetchAll(successCallback) {
            var deferred = $q.defer();
            $http.get("/rest/group").then(deferred.resolve, deferred.reject);
            deferred.promise.catch(Handler.handleError("Error during fetching groups"));
            return deferred.promise;
        }
    }]);
