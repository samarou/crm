/**
 * @author yauheni.putsykovich
 */

angular.module("app").factory("Util", ["$timeout", function ($timeout) {
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
}]);
