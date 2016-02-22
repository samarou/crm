/**
 * @author yauheni.putsykovich
 */
angular.module("app").directive("pagination", [function () {
    return {
        template: "",
        replace: true,
        scope: {paging: "=paging"},
        link: function (scope, element, attributes) {
            scope.$watch(function () {
                return scope.paging;
            }, function (paging) {
                if (!scope.paging) return;//paging is not available

                if ($(element).data("twbs-pagination")) $(element).twbsPagination('destroy');//drop old pagination

                if (paging.totalPages >= 1 && paging.visiblePages >= 1) {
                    $(element).twbsPagination({
                        totalPages: paging.totalPages,
                        visiblePages: paging.visiblePages,
                        onPageClick: function (event, page) {
                            paging.onPageClick(page);
                        }
                    });
                }
            }, true);
        }
    }
}]);
