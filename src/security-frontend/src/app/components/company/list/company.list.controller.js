(function () {
    'use strict';

    angular
        .module('crm.company')
        .controller('companyListController', companyListController);

    /** @ngInject */
    function companyListController(companyService, companySecurityService, searchService, dialogService,
                                   $q, $state) {
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
            getStaticData().then(find);
        }

        function getStaticData() {
            return companyService.getStaticData().then(function (staticData) {
                vm.staticData = staticData;
            });
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
            find();
        }

        function remove() {
            openRemoveDialog().then(function () {
                var checkedCompanies = vm.searchBundle.itemsList.filter(function (company) {
                    return company.checked;
                });
                companySecurityService.checkDeletePermissionForList(checkedCompanies).then(removeCompanies);
            });
        }

        function removeCompanies(companies) {
            var tasks = [];
            companies.forEach(function (company) {
                tasks.push(companyService.remove(company.id));
            });
            $q.all(tasks).then(find);
        }

        function openRemoveDialog() {
            return dialogService.confirm('Do you want to delete this company?').result;
        }

        function find() {
            vm.searchBundle.find();
        }
    }
})();
