/**
 * @author yauheni.putsykovich
 */

angular.module("app")
    .filter("orderFilterPage", ["$filter", function ($filter) {
        /**
         * Performs filtering of collection according setting.
         * Also performs cutting of collection of items according current page and page size.
         * @collection: source collection where need make filtering
         * @config object which represents the current state of pagination. He should have following properties:
         *      {
         *          currentPage: #,
         *          itemsPerPage: #,
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
         * @return: result of sorting
         */
        "use strict";
        return function (collection, config) {
            if (!collection || !config) return collection;
            var orderByResult = $filter("orderBy")(collection, config.sortProperty, !config.sortAsc);
            var filterResult = $filter("filter")(orderByResult, config.filterObject);
            var start = config.itemsPerPage * (config.currentPage - 1);
            var end = config.itemsPerPage * config.currentPage;
            return filterResult.slice(start, end);
        }
    }]);
    