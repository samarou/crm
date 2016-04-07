/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';
	
	angular.module('securityManagement').directive('privileges', privileges);

	/** @ngInject */
	function privileges() {
		return {
			scope: {
				objectTypes: '='
			},
			require: 'objectTypes',
			restrict: 'E',
			replace: true,
			templateUrl: 'app/components/privilege/privilege.partial.view.html',
			controller: ['$scope', function ($scope) {
				$scope.selectAll = function (objectType, checked) {
					objectType.actions.forEach(function (action) {
						action.privilege.checked = checked;
					})
				};
				$scope.selectOne = function (objectType) {
					objectType.isSelectAll = objectType.actions.every(function (action) {
						return action.privilege.checked;
					});
				};
			}]
		}
	}
})();
