(function () {
	'use strict';

	angular
			.module('crm.role')
			.factory('pagingFilter', pagingFilter);

	/** @ngInject */
	function pagingFilter() {
		var service = this;
		service.config = {
			currentPage: 1,
			itemsPerPage: 10,
			visiblePages: 5,
			totalItems: null,
			privilegeSearchText: null,
			filterObject: {
				name: ''
			},
			sortProperty: 'name',
			sortAsc: true
		};

		return {
			config: service.config,
			updateFilterObject: updateFilterObject,
			setLength: setLength
		};

		function setLength(length) {
			service.config.totalItems = length;
		}

		function updateFilterObject(searchText) {
			service.config.filterObject.name = searchText;
			service.config.description = searchText;
		}
	}
})();
