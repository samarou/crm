/**
 * @author yauheni.putsykovich
 */
angular.module("app").directive("pagination", [function () {
    return {
        template: "",
        replace: "",
        scope: {
            paging: "="
        },
        link: function (scope, element, attributes) {
            scope.$watch("paging", function (paging) {
                if (paging) {
                    $(element).twbsPagination({
                        totalPages: paging.totalPages,
                        visiblePages: paging.visiblePages,
                        onPageClick: function (event, page) {
                            paging.onPageClick(page);
                        }
                    });
                }
            });
        }
    }
}]);
