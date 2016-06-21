/**
 * @author yauheni.putsykovich
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .controller('TaskAddController', TaskAddController);

    /** @ngInject */
    function TaskAddController(taskCommonService, authService, util, $log) {
        var vm = this;

        vm.canEdit = true;
        vm.timeless = true;
        vm.title = 'Add Task';
        vm.submitText = 'Add';

        vm.onStartDateChange = function (newValue) {
            vm.task.endDate = new Date(vm.task.startDate);
        };

        vm.onDateTimeChange = function (newValue) {
            var pass = util.shouldBeLess(newValue, vm.task.startDate);
            if (!pass) {
                var date = new Date(vm.task.startDate.getTime());
                date.setHours(vm.task.startDate.getHours());
                date.setMinutes(vm.task.startDate.getMinutes());
                vm.task.endDate = date;
            }
        };

        (function () {
            taskCommonService.initContext(vm).then(function () {
                var currentUsername = authService.getUserName();
                var currentUser = vm.assigns.find(function (item) {
                    return currentUsername === item.userName;
                });

                var startDate = new Date();
                if (startDate.getMinutes() > 0) {
                    startDate.setMinutes(0);
                    startDate.setHours(startDate.getHours() + 1);
                }
                vm.task = {
                    startDate: startDate,
                    status: vm.statuses[0],// default is 'New', TODO: need to add some resolver
                    priority: vm.priorities[1],// default is 'Normal', TODO: need to add some resolver
                    assignee: currentUser
                };

                vm.aclHandler = taskCommonService.createAclHandler(function () {
                    return vm.task.id;
                });
                vm.aclHandler.canEdit = vm.canEdit;
            });
        })();
    }
})();
