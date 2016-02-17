/**
 * @author yauheni.putsykovich
 */
"use strict";

angular.module("app").directive("pagination", [function () {
    var directive = {};
    directive.restict = "E";
    directive.templateUrl = "/app/common/pagination.template.html";
    directive.scope = false;
    directive.scope = {
        paging: "=paging"
    };
    directive.compile = function () {
    };
    directive.link = function (scope, element, attrs) {
        scope.paging.onchange();
    };
    return directive;
}]);