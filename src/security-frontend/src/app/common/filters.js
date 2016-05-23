(function () {
	'use strict';

	angular.module('crm.common')
			.filter('pagingFilter', pagingFilter)
			.filter('isCurrentUser', isCurrentUser)
			.filter('html', html);

	/** @ngInject */
	function pagingFilter($filter) {
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
		'use strict';
		return function (collection, config) {
			if (!collection || !config) {
				return collection;
			}
			var orderByResult = $filter('orderBy')(collection, config.sortProperty, !config.sortAsc);
			var filterResult = $filter('filter')(orderByResult, config.filterObject);
			var start = config.itemsPerPage * (config.currentPage - 1);
			var end = config.itemsPerPage * config.currentPage;
			return filterResult.slice(start, end);
		};
	}

	/** @ngInject */
	function html($sce) {
        /*
         * Mark the html in 'val' as safe using the $sce.trustAsHtml function
         * */
		return function (val) {
			return $sce.trustAsHtml(val);
		};
	}

    /** @ngInject */
    function isCurrentUser(authService) {
        /*
         * It checks if user is a currently logged user
         * */
        return function (user) {
            var auth = authService.getAuthentication();
            var name = auth ? auth.username : null;
            if (name) {
                return user.userName === name;
            }
            return false;
        };
    }
})();
