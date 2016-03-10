/**
 * @author yauheni.putsykovich
 */

angular.module("app").controller("CustomersController", ["$q", "CustomerService", "GroupService", "UserService", "UserBundle", "Util", "Collections", "DialogService",
    function ($q, CustomerService, GroupService, UserService, UserBundle, Util, Collections, DialogService) {
        var vm = this;

        var customerBundle = {
            customer: null,
            permissions: null,
            actions: {
                addPermissionsForUser: addPermissionsForUser,
                addPermissionsForGroup: addPermissionsForGroup,
                removePermissions: removePermissions
            }
        };

        vm.customersList = [];

        CustomerService.getAll().then(function (response) {
            vm.customersList = response.data;
        });

        vm.create = function () {
            customerBundle.customer = {};
            customerBundle.permissions = [];
            openCustomerDialog({title: "Create Customer"});
        };

        vm.edit = function (customer) {
            CustomerService.getPermissions(customer.id).then(function (response) {
                customerBundle.customer = customer;
                customerBundle.permissions = response.data;
                openCustomerDialog({title: "Editing Customer"});
            });
        };

        vm.userBundle = UserBundle.publicMode();

        function openCustomerDialog(model) {
            model.bundle = customerBundle;
            DialogService.custom("app/customer/customer.modal.view.html", model).result.then(updateCustomer);
        }

        function updateCustomer(model) {
            CustomerService.update(model.bundle.customer);
            CustomerService.updatePermissions(model.bundle.customer.id, model.bundle.permissions);
        }

        function removePermissions(customer) {
            var tasks = [];
            customerBundle.permissions.forEach(function (p) {
                if (p.checked) tasks.push(CustomerService.removePermissions(customer.id, p.id));
            });
            $q.all(tasks).then(function () {
                CustomerService.getPermissions(customer.id).then(function (response) {
                    customerBundle.permissions = response.data;
                })
            });
        }

        function addPermissionsForUser(customer) {
            vm.userBundle.find();
            DialogService.custom("app/customer/public-users.modal.view.html", {
                title: "Add Permissions for User",
                bundle: vm.userBundle,
                size: "user-table--modal"
            }).result.then(function (model) {
                    model.bundle.userList.forEach(function (user) {
                        var alreadyPresent = !!Collections.find(user, customerBundle.permissions);
                        if (!alreadyPresent && user.checked) addDefaultPermissions(user.id, user.userName, "user");
                    });
                });
        }

        function addPermissionsForGroup() {
            DialogService.custom("app/customer/public-groups.modal.view.html", {
                title: "Add Permissions for Group",
                groups: []
            }).result.then(function (model) {
                    console.log("object rights: ", model);
                });
        }

        function addDefaultPermissions(id, name, type) {
            var defaultPermission = {
                id: id,
                name: name,
                principalTypeName: type,
                canRead: false,
                canWrite: false,
                canCreate: false,
                canDelete: false,
                canAdmin: false
            };
            customerBundle.permissions.push(defaultPermission);
        }
    }]);