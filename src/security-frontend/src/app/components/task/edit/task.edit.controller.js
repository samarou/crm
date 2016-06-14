/**
 * Created by yauheni.putsykovich on 01.06.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .controller('TaskEditController', TaskEditController);

    /** @ngInject */
    function TaskEditController(taskService, taskSecurityService, taskCommonService, $stateParams, $q) {
        var vm = this;

        vm.canEdit = false;
        vm.title = 'Edit Task';
        vm.submitText = 'Edit';

        (function () {
            taskCommonService.initContext(vm);
            taskService.getTaskById($stateParams.id).then(function (response) {
                vm.task = response.data;
                /*need converting because date have a millis format*/
                vm.task.startDate = vm.task.startDate && new Date(vm.task.startDate);
                vm.task.endDate = vm.task.endDate && new Date(vm.task.endDate);
                vm.aclHandler = taskCommonService.createAclHandler(function () {
                    return vm.task.id;
                });
                $q.all([
                    taskService.getAcls(vm.task.id),
                    taskSecurityService.checkEditPermission(vm.task.id)
                ]).then(function (responses) {
                    vm.aclHandler.acls = responses[0].data;
                    vm.aclHandler.canEdit = responses[1];
                    vm.canEdit = responses[1];
                });
            });
        })();
    }
})();
