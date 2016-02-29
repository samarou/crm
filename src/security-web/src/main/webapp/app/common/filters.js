/**
 * @author yauheni.putsykovich
 */

angular.module("app")
    .filter("paging", function () {
        /**
         * Performs cutting of collection of items according current page and page size.
         *
         * @collection: source collection where need to cut
         * @paging object which represents the current state of pagination.
         *      He should have following properties:
         *      {
         *          currentPage: #,
         *          itemsPerPage: #,
         *          outFilterResult: if 'outFilterResult' not undefined, then in it will be maintained returned value
         *      }
         * @return set of items according the current page
         */
        "use strict";
        return function (collection, paging) {
            if (!paging || !paging) return collection;
            var start = paging.itemsPerPage * (paging.currentPage - 1);
            var end = paging.itemsPerPage * paging.currentPage;
            var filterResult = collection.slice(start, end);
            if (paging.outFilterResult !== undefined) {
                paging.outFilterResult = filterResult;
            }
            return filterResult;
        }
    })
    .filter("filtering", ["$filter", function ($filter) {
        /**
         * Performs filtering of collection according setting in 'filterConfig' object, which have structure as shown below:
         *      {
         *          filterObject: {
         *              property1ToSearch: 'value',
         *              property2ToSearch: 'value',
         *              ...
         *              propertyNToSearch: 'value'
         *          }
         *          sortProperty: #,
         *          sortAsc: #,
         *          outFilterResult: if 'outFilterResult' not undefined, then in it will be maintained returned value
         *      }
         * @collection: source collection where need make filtering
         * @filterConfig: object with setting
         * @return: result of sorting
         */
        "use strict";
        return function (collection, filterConfig) {
            if (!collection || !filterConfig) return collection;
            var orderByResult = $filter("orderBy")(collection, filterConfig.sortProperty, !filterConfig.sortAsc);
            var filterResult = $filter("filter")(orderByResult, filterConfig.filterObject);
            if (filterConfig.outFilterResult !== undefined) {
                filterConfig.outFilterResult = filterResult;
            }
            return filterResult;
        }
    }]);
    