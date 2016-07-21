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

        vm.canEdit = true;
        vm.timeless = false;
        vm.title = 'Edit Task';
        vm.submitText = 'Save';

        (function () {
            taskCommonService.initContext(vm);
            taskService.getTaskById($stateParams.id).then(function (response) {
                vm.task = response.data;
                /*need converting because date have a millis format*/
                vm.task.startDate = vm.task.startDate ? new Date(vm.task.startDate) : null;
                vm.task.endDate = vm.task.endDate ? new Date(vm.task.endDate)  : null;
                $q.all([
                    taskService.getAcls(vm.task.id),
                    taskSecurityService.checkAdminPermission(vm.task.id),
                    taskSecurityService.checkEditPermission(vm.task.id)
                ]).then(function (responses) {
                    vm.aclHandler.acls = responses[0].data;
                    vm.aclHandler.canEdit = responses[1];
                    vm.canEdit = responses[2];

                    vm.canEditDateTime = vm.canEdit;
                });
            });
        })();
    }
})();
