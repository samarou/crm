(function () {
    'use strict';

    angular
        .module('crm.company')
        .controller('companyListController', companyListController);

    /** @ngInject */
    function companyListController($q, companyService, searchService, dialogService, $state) {
        var vm = this;

        vm.searchBundle = searchService.companyMode();
        vm.add = add;
        vm.edit = edit;
        vm.remove = remove;
        vm.filterUpdated = filterUpdated;
        vm.staticData = companyService.staticData;
        vm.selectedEmployeeNumberCategory = {};

        init();

        function init() {
            vm.searchBundle.find();
        }

        function add() {
            $state.go('companies.add');
        }

        function edit(company) {
            $state.go('companies.edit', {id: company.id});
        }

        function filterUpdated() {
            vm.searchBundle.filter.employeeNumberCategoryId = vm.selectedEmployeeNumberCategory ?
                    vm.selectedEmployeeNumberCategory.id : null;
            vm.searchBundle.find();
        }

        function remove() {
            openRemoveDialog().then(function () {
                var checkedCompanies = vm.searchBundle.itemsList.filter(function (company) {
                    return company.checked;
                });
                removeCompanies(checkedCompanies);
            });
        }

        function removeCompanies(companies) {
            var tasks = [];
            companies.forEach(function (company) {
                tasks.push(companyService.remove(company.id));
            });
            $q.all(tasks).then(vm.searchBundle.find);
        }

        function openRemoveDialog() {
            return dialogService.confirm('Do you want to delete this company?').result;
        }
    }
})();