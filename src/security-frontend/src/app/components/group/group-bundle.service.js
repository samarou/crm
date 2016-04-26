(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('groupBundle', groupBundle);

	/** @ngInject */
	function groupBundle(groupService) {
		return {
			publicMode: getPublicBundle,
			securedMode: getSecuredBundle
		};

		function getPublicBundle() {
			var bundle = createBundle();
			bundle.performSearch = groupService.getPublicGroups;
			return bundle;
		}

		function getSecuredBundle() {
			var bundle = createBundle();
			bundle.performSearch = groupService.getAll;
			return bundle;
		}

		function createBundle() {
			var bundle = {};

			bundle.searchText = '';
			bundle.pageGroups = [];

			// todo: resolve problem with filtering
			bundle.pagingFilterConfig = {
				currentPage: 1,
				itemsPerPage: 10,
				visiblePages: 5,
				totalCount: null,
				filterObject: {
					name: ''
				},
				sortProperty: 'name',
				sortAsc: true
			};

			bundle.performSearch = groupService.getPublicGroups;
			bundle.find = function () {
				bundle.performSearch().then(function (response) {
					bundle.groupList = response.data;
					bundle.pagingFilterConfig.totalCount = bundle.groupList.length;
				});
			};

			bundle.selectAll = function (checked) {
				if (checked) {
					bundle.pageGroups.forEach(function (group) {
						group.checked = true;
					});
				} else {
					bundle.groupList.forEach(function (group) {
						group.checked = false;
					});
				}
			};

			bundle.selectOne = function () {
				bundle.isSelectedAll = bundle.pageGroups.every(function (group) {
					return group.checked;
				});
			};

			return bundle;
		}
	}
})();