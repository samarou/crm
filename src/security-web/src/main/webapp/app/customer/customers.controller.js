/**
 * @author yauheni.putsykovich
 */

angular.module("app").controller("CustomersController", ["$q", "CustomerService", "GroupService", "UserService", "DialogService",
    function ($q, CustomerService, GroupService, UserService, DialogService) {
        var vm = this;

        vm.customersList = [];

        CustomerService.fetchAll().then(function (response) {
            vm.customersList = response.data;
        });

        vm.create = function () {
            openCustomerDialog({
                customer: {},
                rights: [],
                title: "Create Customer"
            });
        };

        vm.edit = function (customer) {
            CustomerService.getRights(customer.id).then(function (response) {
                openCustomerDialog({
                    customer: customer,
                    rights: response.data,
                    title: "Editing Customer"
                });
            });
        };

        function openCustomerDialog(model) {
            model.actions = {
                addObjectRightsForUser: addObjectRightsForUser,
                addObjectRightsForGroup: addObjectRightsForGroup
            };
            DialogService.custom("app/customer/customer.modal.view.html", model).result.then(updateCustomer);
        }

        function updateCustomer(model) {
            console.log("model: ", model);
        }

        function addObjectRightsForUser() {
            DialogService.custom("app/customer/permissions-for-user.modal.view.html", {
                title: "Add Permissions for User",
                user: [],
                size: "user-table--modal"
            }).result.then(function (model) {
                    console.log("object rights: ", model);
                });
        }

        function addObjectRightsForGroup() {
            DialogService.custom("app/customer/permissions-for-group.modal.view.html", {
                title: "Add Permissions for Group",
                groups: []
            }).result.then(function (model) {
                    console.log("object rights: ", model);
                });
        }
    }]);