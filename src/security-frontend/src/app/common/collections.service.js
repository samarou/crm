(function () {
	'use strict';

	angular
		.module('crm.common')
		.factory('collections', collections);

	/** @ngInject */
	function collections() {
		return {
			byId: byId,
			compare: compare,
			propertyComparator: propertyComparator,
			difference: difference,
			sort: sort,
			find: find
		};

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
	}
})();
