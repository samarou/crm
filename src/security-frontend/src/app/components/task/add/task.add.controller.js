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

        vm.canEdit = true;
        vm.title = 'Add Task';
        vm.submitText = 'Add';

        (function () {
            taskCommonService.initContext(vm).then(function () {
                vm.task = {
                    status: vm.statuses[0],// default is 'New', TODO: need to add some resolver
                    priority: vm.priorities[1]// default is 'Normal', TODO: need to add some resolver
                };
                vm.aclHandler = taskCommonService.createAclHandler(function () {
                    return vm.task.id;
                });
                vm.aclHandler.canEdit = vm.canEdit;
            });
        })();
    }
})();
