angular.module("app").service("Collections", [function () {
    "use strict";

    var self = this;

    self.byId = function (a, b) {
        return a.id === b.id;
    };
    self.compareTo = function (a, b) {
        return a > b ? 1 : a === b ? 0 : -1;
    };
    self.byProperty = function (propertyName) {
        return function (a, b) {
            return self.compareTo(a[propertyName], b[propertyName]);
        }
    };

    self.difference = function difference(from, what, comparator) {
        comparator = comparator || self.byId;
        if (!from) return what;
        if (!what) return from;
        return from.filter(function (f) {
            return !what.some(function (w) {
                return comparator(f, w);
            })
        })
    };

    self.sort = function sort(collection, asc, comparator) {
        asc = asc || true;
        comparator = comparator || self.compareTo;
        collection.sort(function (a, b) {
            return (asc ? 1 : -1) * comparator(a, b);
        });
        return collection;
    };

    self.find = function find(item, collection, comparator) {
        if (!collection || !item) return false;
        comparator = comparator || self.byId;
        var foundItem = collection.find(function (collectionItem) {
            return comparator(item, collectionItem);
        });

        return foundItem;
    };

    return self;
}]);