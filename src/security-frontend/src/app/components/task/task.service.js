/**
 * @author yauheni.putsykovich
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .service('taskService', taskService);

    /** @ngInject */
    function taskService($http) {
        return {
            getTaskById: getTaskById,
            find: find,
            create: create,
            update: update,
            remove: remove,
            getPriorities: getPriorities,
            getStatuses: getStatuses
        };

        function getTaskById(id) {
            return $http.get('rest/tasks/' + id);
        }

        function find(filter) {
            return $http.get('rest/tasks/find', {params: filter});
        }

        function create(task) {
            return $http.post('rest/tasks', task);
        }

        function update(task) {
            return $http.put('rest/tasks', task);
        }

        function remove(id) {
            return $http.delete('rest/tasks/' + id);
        }

        function getPriorities() {
            return $http.get('rest/tasks/priorities');
        }

        function getStatuses() {
            return $http.get('rest/tasks/statuses');
        }
    }

})();
