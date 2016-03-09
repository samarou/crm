/**
 * @author yauheni.putsykovich
 */

angular.module("app").controller("CustomersController", ["CustomerService", "GroupService", "UserService", "UserBundle", "Util", "DialogService",
    function (CustomerService, GroupService, UserService, UserBundle, Util, DialogService) {
        var vm = this;

        vm.customersList = [];

        CustomerService.getAll().then(function (response) {
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
            CustomerService.getPermissions(customer.id).then(function (response) {
                openCustomerDialog({
                    customer: customer,
                    rights: response.data,
                    title: "Editing Customer"
                });
            });
        };

        vm.userBundle = UserBundle.publicMode();

        function openCustomerDialog(model) {
            model.actions = {
                addPermissionsForUser: addPermissionsForUser,
                addPermissionsForGroup: addPermissionsForGroup
            };
            DialogService.custom("app/customer/customer.modal.view.html", model).result.then(updateCustomer);
        }

        function updateCustomer(model) {
            console.log("model: ", model);
        }

        function addPermissionsForUser(customer) {
            vm.userBundle.find();
            DialogService.custom("app/customer/public-users.modal.view.html", {
                title: "Add Permissions for User",
                bundle: vm.userBundle,
                size: "user-table--modal"
            }).result.then(function (model) {
                    var aclEntries = model.bundle.userList
                        .filter(function (user) { return user.checked; })
                        .map(function (user) { return user.id; });
                    CustomerService.addPermission(customer.id, aclEntries).then(CustomerService.getPermissions(customer.id));
                });
        }

        /*
         name: user.userName,
         principalTypeName: "user",
         canRead: false,
         canWrite: false,
         canCreate: false,
         canDelete: false,
         canAdmin: false
         */

        function addPermissionsForGroup() {
            DialogService.custom("app/customer/public-groups.modal.view.html", {
                title: "Add Permissions for Group",
                groups: []
            }).result.then(function (model) {
                    console.log("object rights: ", model);
                });
        }
    }]);