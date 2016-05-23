(function () {
    'use strict';

    angular
        .module('crm.group')
        .controller('GroupsAddController', GroupsAddController);

    function GroupsAddController(groupDetailsService) {
        var vm = this;

        vm.group = {};
        vm.submitText = 'Add';
        vm.title = 'Add group';
        vm.submit = submit;
        vm.cancel = groupDetailsService.cancel;

        function submit() {
            groupDetailsService.submit(vm.group, true);
        }
    }
})();
