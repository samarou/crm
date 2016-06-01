/**
 * Created by yauheni.putsykovich on 31.05.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .controller('TaskListController', TaskListController);

    /** @ngInject */
    function TaskListController(taskService, taskCommonService, $state, $q) {
        var vm = this;

        vm.title = 'Add Task';
        vm.submitText = 'Add';
        vm.taskList = [];
        vm.add = add;
        vm.edit = edit;
        vm.remove = remove;

        function add() {
            $state.go('tasks.add');
        }

        function edit(task) {
            $state.go('tasks.edit', {id: task.id});
        }

        function remove() {
            $q.all(
                vm.taskList.filter(function (task) {
                    return task.checked;
                }).map(function (task) {
                    return taskService.remove(task.id);
                })
            ).then(loadTasks);
        }

        function loadTasks() {
            taskService.getAllTasks().then(function (response) {
                vm.taskList = response.data;
            })
        }

        (function () {
            loadTasks()
        })();
    }

})();
