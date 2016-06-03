/**
 * Created by yauheni.putsykovich on 01.06.2016.
 */

(function () {
    'use stricr';

    angular
        .module('crm.task')
        .controller('TaskEditController', TaskEditController);

    /** @ngInject */
    function TaskEditController(taskService, taskCommonService, $stateParams) {
        var vm = this;

        vm.title = 'Edit Task';
        vm.submitText = 'Edit';
        vm.task = {};
        vm.cancel = taskCommonService.cancel;
        vm.submit = submit;

        function submit() {
            taskCommonService.submit(vm.task);
        }

        (function () {
            taskCommonService.loadStaticData(vm);
            taskService.getTaskById($stateParams.id).then(function (response) {
                vm.task = response.data;
            });
        })();
    }
})();
