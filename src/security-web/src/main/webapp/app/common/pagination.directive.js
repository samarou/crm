/**
 * @author yauheni.putsykovich
 */
angular.module("app").directive("pagination", [function () {
        return {
            template: "",
            replace: true,
            scope: {paging: "="},
            link: function (scope, element, attributes) {
                scope.$watch("paging", function (paging) {
                    if (!!scope.paging) {
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
