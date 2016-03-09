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
                addObjectRights: openObjectRightsModal
            };
            DialogService.custom("app/customer/customer.modal.view.html", model).result.then(updateCustomer);
        }

        function updateCustomer(model) {
            console.log("model: ", model);
        }

        function openObjectRightsModal() {
            var users, groups;
            var tasks = [
                //UserService.getAll().then(function (response) { users = response.data; }),
                //GroupService.fetchAll().then(function (response) { groups = response.data; })
            ];
            $q.all(tasks).then(function () {
                DialogService.custom("app/user/users.view.html", {
                    user: [],
                    group: [],
                    size: "user-table--modal"
                }).result.then(function (model) {
                        console.log("object rights: ", model);
                    });
            });
        }
    }]);