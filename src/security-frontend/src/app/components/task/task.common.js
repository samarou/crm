/**
 * Created by yauheni.putsykovich on 01.06.2016.
 */

(function() {
    'use strict';

    angular
        .module('crm.task')
        .service('taskCommonService', taskCommonService);

    /** @ngInject */
    function taskCommonService(taskService, $state, $q, userService) {
        return {
            loadStaticData: loadStaticData,
            cancel: goToTaskList,
            submit: submit
        };

        function loadStaticData(scope) {
            return $q.all([
                userService.getPublicUsers().then(function (response) {
                    scope.assigns = response.data;
                }),
                taskService.getPriorities().then(function (response) {
                    scope.priorities = response.data;
                }),
                taskService.getStatuses().then(function (response) {
                    scope.statuses = response.data;
                })]
            );
        }

        function goToTaskList() {
            $state.go('tasks.list');
        }

        function submit(task) {
            if (task.id) {
                taskService.update(task).then(goToTaskList)
            } else {
                taskService.create(task).then(goToTaskList)
            }
        }


    }
})();
