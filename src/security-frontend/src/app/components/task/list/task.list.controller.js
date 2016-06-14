/**
 * Created by yauheni.putsykovich on 31.05.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .controller('TaskListController', TaskListController);

    /** @ngInject */
    function TaskListController(taskService, taskSecurityService, dialogService, collections, searchService, $state, $q) {
        var vm = this;

        vm.bundle = searchService.getTaskBundle();
        vm.add = add;
        vm.edit = edit;
        vm.remove = remove;

        function add() {
            $state.go('tasks.add');
        }

        function edit(task) {
            taskSecurityService.checkEditPermission(task.id).then(function () {
                $state.go('tasks.edit', {id: task.id});
            });
        }

        function remove() {
            dialogService.confirm('Do you want to delete the selected task(s)?').result.then(function () {
                var checked = vm.bundle.itemsList.filter(collections.getChecked);
                taskSecurityService.checkDeletePermissionForList(checked).then(function () {
                    $q.all(checked.map(collections.getId).map(taskService.remove)).then(vm.bundle.find);
                });
            });
        }

        (function () {
            vm.bundle.find();
        })();
    }

})();
