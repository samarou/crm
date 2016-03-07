/**
 * @author yauheni.putsykovich
 */
angular.module("app").service("AclService", ["$http", function ($http) {
    "use strict";
    this.getAcls = function (objectIdentity) {
        return $http.get("/rest/acl", {params: objectIdentity});
    }
}]);