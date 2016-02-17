/**
 * @author yauheni.putsykovich
 */

"use strict";

angular.module("app").service("Collections", [function () {
    this.difference = function difference(from, what, comparator) {
        if (!from) return what;
        if (!what) return from;
        return from.filter(function (f) {
            return !what.some(function (w) {
                return comparator(f, w);
            })
        })
    };

    this.Comparators = {
        BY_ID: function comparator(f, w) {
            return f.id === w.id;
        }
    };

    return this;
}]);
