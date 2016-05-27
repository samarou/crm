(function () {
    'use strict';

    angular
        .module('crm.company')
        .controller('companyAddController', companyAddController);

    /** @ngInject */
    function companyAddController(companyDetailsService, userService, $q) {
        var vm = this;

        vm.canEdit = true;
        vm.company = {};
        vm.submitText = 'Add';
        vm.title = 'Add company';
        vm.submit = submit;
        vm.cancel = companyDetailsService.cancel;
        vm.aclHandler = companyDetailsService.createAclHandler(function () {
            return vm.company.id;
        });
        vm.staticData = {};

        init();

        function init() {
            $q.all([
                getDefaultAcls(),
                getStaticData()
            ]);
        }

        function getDefaultAcls() {
            return userService.getDefaultAcls().then(function (response) {
                vm.aclHandler.acls = response.data;
            });
        }

        function getStaticData() {
            return companyDetailsService.getStaticData().then(function (staticData) {
                vm.staticData = staticData;
            });
        }

        function submit() {
            var promise = companyDetailsService.create(vm.company).then(function (id) {
                companyDetailsService.updateAcls(id, vm.aclHandler.acls);
            });
            companyDetailsService.submit(promise);
        }
    }

})();
