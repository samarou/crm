/**
 * @author yauheni.putsykovich
 */

angular.module("app").service("Collections", [function () {
    "use strict";

    var Comparators = {
        BY_ID: function comparator(f, w) {
            return f.id === w.id;
        }
    };

    this.Comparators = Comparators;

    this.difference = function difference(from, what, comparator) {
        if (!from) return what;
        if (!what) return from;
        return from.filter(function (f) {
            return !what.some(function (w) {
                return comparator(f, w);
            })
        })
    };

    this.find = function find(item, collection, comparator) {
        comparator = comparator || Comparators.BY_ID;
        var foundItem = collection.find(function (collectionItem) {
            return comparator(item, collectionItem);
        });

        return foundItem;
    };

    this.indexOf = function remove(item, collection, comparator) {
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
