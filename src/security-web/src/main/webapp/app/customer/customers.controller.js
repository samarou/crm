/**
 * @author yauheni.putsykovich
 */

angular.module("app").controller("CustomersController", ["CustomerService", "GroupService", "UserService", "DialogService",
    function (CustomerService, GroupService, UserService, DialogService) {
        var vm = this;

        vm.customersList = [];

        CustomerService.fetchAll().then(function (response) {
            vm.customersList = response.data;
        });


        vm.edit = function (customer) {
            CustomerService.getRights(customer.id).then(function (response) {
                console.log("response: ", response);
            });
        };

        vm.create = function () {
            var dialog = DialogService.custom("app/customer/customer.modal.view.html", {
                customer: {}
            });
            dialog.result.then(function (model) {
                console.log("model: ", model);
            });
        };
    }]);