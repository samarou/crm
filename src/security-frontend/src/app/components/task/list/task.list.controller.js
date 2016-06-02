/**
 * Created by yauheni.putsykovich on 31.05.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .controller('TaskListController', TaskListController);

    /** @ngInject */
    function TaskListController(taskService, dialogService, searchService, $state, $q) {
        var vm = this;

        vm.bundle = searchService.getTaskBundle();
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
            dialogService.confirm('Do you want to delete the selected task(s)?')
                .result.then(function (answer) {
                var checked = vm.bundle.itemsList.filter(function (task) {
                    return task.checked;
                });
                if (answer) {
                    $q.all(
                        checked.map(function (task) {
                            return taskService.remove(task.id);
                        })
                    ).then(vm.bundle.find, function () {
                        dialogService.error('Occurred an error during removing tasks');
                    });
                }
            });
        }

        (function () {
            vm.bundle.find();
        })();
    }

})();
