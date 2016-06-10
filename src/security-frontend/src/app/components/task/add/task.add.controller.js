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

        (function () {
            taskCommonService.initContext(vm).then(function () {
                vm.task = {
                    status: vm.statuses[0],// default is 'New', TODO: need to add some resolver
                    priority: vm.priorities[1]// default is 'Normal', TODO: need to add some resolver
                };
            });
        })();
    }
})();
