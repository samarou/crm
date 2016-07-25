(function () {
    'use strict';

    angular
        .module('crm.common')
        .factory('collections', collections);

    /** @ngInject */
    function collections() {
        return {
            getChecked: getChecked,
            getId: getId,
            byId: byId,
            compare: compare,
            propertyComparator: propertyComparator,
            propertyListComparator: propertyListComparator,
            difference: difference,
            sort: sort,
            find: find,
            exists: exists
        };

        function getChecked(item) {
            return item.checked;
        }

        function getId(item) {
            return item.id;
        }

        function byId(a, b) {
            return a.id === b.id;
        }

        function compare(a, b) {
            return a > b ? 1 : a === b ? 0 : -1;
        }


        function propertyComparator(propertyName) {
            return function (a, b) {
                return compare(a[propertyName], b[propertyName]);
            };
        }

        function propertyListComparator(propertyList) {
            return function (a, b) {
                var result = true;
                propertyList.forEach( function(propertyName) {
                    result &= !compare(a[propertyName], b[propertyName]);
                });
                return result;
            }
        }

        function difference(from, what, comparator) {
            comparator = comparator || byId;
            if (!from) {
                return what;
            }
            if (!what) {
                return from;
            }
            return from.filter(function (f) {
                return !what.some(function (w) {
                    return comparator(f, w);
                });
            });
        }

        function sort(collection, asc, comparator) {
            comparator = comparator || propertyComparator('id');
            collection.sort(function (a, b) {
                return (asc ? 1 : -1) * comparator(a, b);
            });
            return collection;
        }

        function find(item, collection, comparator) {
            if (!collection || !item) {
                return false;
            }
            comparator = comparator || byId;
            return collection.find(function (collectionItem) {
                return comparator(item, collectionItem);
            });
        }

        function exists(item, source, comparator) {
            return !!find(item, source, comparator);
        }
    }
})();
