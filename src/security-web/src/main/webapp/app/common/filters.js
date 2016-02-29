/**
 * @author yauheni.putsykovich
 */

angular.module("app").filter("orderFilterPage", ["$filter", function ($filter) {
        /**
         * Performs filtering and cutting collection of items according filter configuration.
         *
         * @collection: source collection where need make filtering
         * @config object which contains setting for filtering, he should have following properties:
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
         *      }
         * @return: collection of filtered items
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
    