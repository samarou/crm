/**
 * @author yauheni.putsykovich
 */

angular.module("app").controller("CustomersController", ["CustomerService", "GroupService", "UserService", "UserBundle", "Util", "Collections", "DialogService",
    function (CustomerService, GroupService, UserService, UserBundle, Util, Collections, DialogService) {
        var vm = this;

        vm.customersList = [];
        vm.permissions = [];

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
                vm.permissions = response.data;
                openCustomerDialog({
                    customer: customer,
                    permissions: vm.permissions,
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
            CustomerService.update(model.customer);
            CustomerService.updatePermissions(model.customer.id, model.permissions);
        }

        function addPermissionsForUser(customer) {
            vm.userBundle.find();
            DialogService.custom("app/customer/public-users.modal.view.html", {
                title: "Add Permissions for User",
                bundle: vm.userBundle,
                size: "user-table--modal"
            }).result.then(function (model) {
                    model.bundle.userList.forEach(function (user) {
                        var alreadyPresent = !!Collections.find(user, vm.permissions);
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
            vm.permissions.push(defaultPermission);
        }
    }]);