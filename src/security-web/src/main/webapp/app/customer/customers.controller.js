/**
 * @author yauheni.putsykovich
 */

angular.module("app").controller("CustomersController", ["CustomerService", function (CustomerService) {
    var vm = this;

    CustomerService.fetchAll().then(function (response) {
        vm.customersList = response.data;
    });
}]);