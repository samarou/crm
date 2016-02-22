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

                var isDestroyed = false;
                if ($(element).data("twbs-pagination")) {
                    $(element).twbsPagination('destroy');//drop old pagination
                    isDestroyed = true;
                }
                
                $(element).twbsPagination({
                    totalPages: paging.totalPages,
                    visiblePages: paging.visiblePages,
                    onPageClick: function (event, page) {
                        if (!isDestroyed) paging.onPageClick(page);
                        else isDestroyed = false;
                    }
                });
            }, true);
        }
    }
}]);
