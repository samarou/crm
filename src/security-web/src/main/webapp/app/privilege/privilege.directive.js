/**
 * @author yauheni.putsykovich
 */

angular.module("app").directive("privilegeSearch", [function () {
    return {
        scope: {
            objectTypes: "="
        },
        require: "objectTypes",
        restrict: "E",
        replace: true,
        templateUrl: "app/privilege/privilege.partial.view.html"
    }
}]);
