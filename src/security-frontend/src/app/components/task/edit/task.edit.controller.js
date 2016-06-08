/**
 * Created by yauheni.putsykovich on 01.06.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .controller('TaskEditController', TaskEditController);

    /** @ngInject */
    function TaskEditController(taskService, taskCommonService, $stateParams) {
        var vm = this;

        vm.title = 'Edit Task';
        vm.submitText = 'Edit';

        (function () {
            taskCommonService.initContext(vm);
            taskService.getTaskById($stateParams.id).then(function (response) {
                vm.task = response.data;
                /*need converting because date have a millis format(only if date was set up)*/
                vm.task.startDate = vm.task.startDate && new Date(vm.task.startDate);
                vm.task.endDate = vm.task.endDate && new Date(vm.task.endDate);
            });
        })();
    }
})();
