/**
 * @author yauheni.putsykovich
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .controller('TaskAddController', TaskAddController);

    /** @ngInject */
    function TaskAddController(taskCommonService) {
        var vm = this;

        vm.title = 'Add Task';
        vm.submitText = 'Add';
        vm.task = {};
        vm.submit = submit;
        vm.cancel = taskCommonService.cancel;

        function submit() {
            taskCommonService.submit(vm.task);
        }

        (function () {
            taskCommonService.loadStaticData(vm).then(function() {
                vm.task.status = vm.statuses[0];
                vm.task.priority = vm.priorities[0];
                vm.task.assignee = vm.assigns[0];
            });
        })();
    }
})();
