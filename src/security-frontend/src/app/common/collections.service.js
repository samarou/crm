(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('collections', collections);

	/** @ngInject */
	function collections() {

		function byId(a, b) {
			return a.id === b.id;
		}

		function compareTo(a, b) {
			return a > b ? 1 : a === b ? 0 : -1;
		}

		function byProperty(propertyName) {
			return function (a, b) {
				return compareTo(a[propertyName], b[propertyName]);
			}
		}

		function difference(from, what, comparator) {
			comparator = comparator || self.byId;
			if (!from) {
				return what;
			}
			if (!what) {
				return from;
			}
			return from.filter(function (f) {
				return !what.some(function (w) {
					return comparator(f, w);
				})
			})
		}

		function sort(collection, asc, comparator) {
			asc = asc || true;
			comparator = comparator || compareTo;
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

		return {
			byId: byId,
			compareTo: compareTo,
			byProperty: byProperty,
			difference: difference,
			sort: sort,
			find: find
		}
	}
})();