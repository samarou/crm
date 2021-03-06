(function () {
    'use strict';

    angular
        .module('crm.common')
        .factory('util', util);

    /** @ngInject */
    function util($timeout, collections) {
        return {
            createDelayTypingListener: createDelayTypingListener,
            createSelectAction: createSelectAction,
            createRemoveAction: createRemoveAction,
            getDateTrimMinutes: getDateTrimMinutes
        };

        function createDelayTypingListener(action, delay) {
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
                };
            })();
        }

        function createSelectAction(collectionResolver, handlerCallback) {
            if (handlerCallback) {
                return function () {
                    handlerCallback(collectionResolver().every(function (item) {
                        return item.checked;
                    }));
                };
            } else {
                return function (checked) {
                    collectionResolver().forEach(function (item) {
                        item.checked = checked;
                    });
                };
            }
        }

        function createRemoveAction(collectionResolver, handlerCallback) {
            return function () {
                var items = collectionResolver();
                var diff = collections.difference(items, items.filter(function (item) {
                    return item.checked;
                }));
                handlerCallback(diff);
            };
        }

        function getDateTrimMinutes(date) {
            var newDate = new Date(date || new Date());
            if (newDate.getMinutes() > 0) {
                newDate.setMinutes(0);
                newDate.setHours(newDate.getHours() + 1);
            }
            return newDate;
        }
    }
})();
