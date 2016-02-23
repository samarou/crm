/**
 * @author yauheni.putsykovich
 */

angular.module("app").service("Collections", [function () {
    "use strict";

    var Comparators = {
        BY_ID: function comparator(a, b) {
            return a.id === b.id;
        },
        COMPARE_TO: function (a, b) {
            return a > b ? 1 : a === b ? 0 : -1;
        }
    };

    this.Comparators = Comparators;

    this.difference = function difference(from, what, comparator) {
        comparator = comparator || Comparators.BY_ID;
        if (!from) return what;
        if (!what) return from;
        return from.filter(function (f) {
            return !what.some(function (w) {
                return comparator(f, w);
            })
        })
    };

    /* property is object with name of property(name) and order of sorting(asc): {name: 'title', asc: true } */
    this.sort = function sort(collection, property, comparator) {
        comparator = comparator || Comparators.COMPARE_TO;
        collection.sort(function (a, b) {
            return (property.asc ? 1 : -1) * comparator(a, b);
        });
    };

    this.find = function find(item, collection, comparator) {
        if (!collection || !item) return false;
        comparator = comparator || Comparators.BY_ID;
        var foundItem = collection.find(function (collectionItem) {
            return comparator(item, collectionItem);
        });

        return foundItem;
    };

    this.indexOf = function indexOf(item, collection, comparator) {
        comparator = comparator || Comparators.BY_ID;
        var index = -1;
        collection.find(function (collectionItem, collectionItemIndex) {
            if (comparator(item, collectionItem)) {
                index = collectionItemIndex;
                return true;
            }
            return false;
        });
        return index;
    };

    return this;
}]);
