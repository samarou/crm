/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.factory('Util', Util);

	/** @ngInject */
	function Util($timeout) {
		this.createDelayTypingListener = function (action, delay) {
			return (function () {
				var timer;

				return {
					keyDown: function () {
						$timeout.cancel(timer);
					},
					keyUp: function () {
						$timeout.cancel(timer);
						timer = $timeout(action, delay);
					}
				}
			})();
		};

		return this;
	}
})();
