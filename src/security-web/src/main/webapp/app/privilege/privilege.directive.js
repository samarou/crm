/**
 * @author yauheni.putsykovich
 */

angular.module("app").directive("privilegeSearch", [function () {
    return {
        scope: {
            privilegeObjects: "="
        },
        restrict: "E",
        replace: true,
        templateUrl: "app/privilege/privilege.partial.view.html",
        link: function (scope, element, attributes) {
            scope.$watch("privilegeObjects", function (privilegeObjects) {
                if (scope.privilegeObjects) {
                    console.log("directive got privileges");
                }
            });
        }
    }
}]);
