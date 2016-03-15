/**
 * @author yauheni.putsykovich
 */

angular.module("app").controller("CustomersController", ["$q", "AuthService", "CustomerService", "GroupService", "GroupBundle", "UserService", "SearchBundle", "Util", "Collections", "DialogService",
    function ($q, AuthService, CustomerService, GroupService, GroupBundle, UserService, SearchBundle, Util, Collections, DialogService) {
        "use strict";
        var vm = this;

        var editCustomerBundle = {
            customer: null,
            permissions: null,
            canEdit: false,
            actions: {
                addPermissionsForUser: addPermissionsForUser,
                addPermissionsForGroup: addPermissionsForGroup,
                removePermissions: removePermissions,
                clickOkListener: clickOkListener
            }
        };

        var permissions = {
            read: "read",
            write: "write",
            create: "create",
            delete: "delete",
            admin: "admin"
        };

        vm.isManager = AuthService.isManager();
        vm.groupBundle = GroupBundle.publicMode();
        vm.userBundle = SearchBundle.userPublicMode();
        vm.searchCustomerBundle = SearchBundle.customerMode();
        vm.searchCustomerBundle.find();

        vm.create = function () {
            editCustomerBundle.customer = {};
            editCustomerBundle.permissions = [];
            openCustomerDialog({title: "Create Customer"});
        };

        vm.edit = function (customer) {
            CustomerService.isAllowed(customer.id, permissions.read).then(function (response) {
                if (!response.data) {
                    DialogService.notify("You haven't permissions to edit that customer!");
                    return;
                }
                CustomerService.isAllowed(customer.id, permissions.write).then(function (response) {
                    editCustomerBundle.canEdit = !!response.data;
                    CustomerService.getPermissions(customer.id).then(function (response) {
                        editCustomerBundle.customer = angular.copy(customer);
                        editCustomerBundle.permissions = response.data;
                        openCustomerDialog({title: "Editing Customer"});
                    });
                });
            });
        };

        vm.remove = function () {
            var tasks = [];
            var allCustomersCanBeDeleted = true;
            var checkedCustomers = vm.searchCustomerBundle.itemsList.filter(function (customer) {
                return customer.checked;
            });
            checkedCustomers.forEach(function (customer) {
                var task = CustomerService.isAllowed(customer.id, permissions.delete).then(function (response) {
                    if (!response.data) allCustomersCanBeDeleted = false;
                });
                tasks.push(task);
            });
            $q.all(tasks).then(function () {
                if (!allCustomersCanBeDeleted) {
                    DialogService.notify("You don't have permissions to do it.");
                    return;
                }

                tasks = [];
                checkedCustomers.forEach(function (customer) {
                    if (customer.checked) tasks.push(CustomerService.remove(customer.id));
                });
                if (allCustomersCanBeDeleted) $q.all(tasks).then(vm.searchCustomerBundle.find)
            });
        };

        function openCustomerDialog(model) {
            model.bundle = editCustomerBundle;
            DialogService.custom("app/customer/customer.modal.view.html", model).result.then(updateCustomer);
        }

        function updateCustomer(model) {
            var customer = model.bundle.customer;
            if (!!customer.id) {
                CustomerService.update(customer).then(vm.searchCustomerBundle.find);
                CustomerService.updatePermissions(customer.id, model.bundle.permissions);
            } else {
                CustomerService.create(customer).then(function (response) {
                    var customerId = response.data;
                    CustomerService.updatePermissions(customerId, model.bundle.permissions).then(vm.searchCustomerBundle.find);
                });
            }
        }

        function removePermissions(customer) {
            var tasks = [];
            editCustomerBundle.permissions.forEach(function (p) {
                if (p.checked) tasks.push(CustomerService.removePermissions(customer.id, p.id));
            });
            $q.all(tasks).then(function () {
                CustomerService.getPermissions(customer.id).then(function (response) {
                    editCustomerBundle.permissions = response.data;
                })
            });
        }

        function clickOkListener(model) {
            if (!model.bundle.canEdit) {
                DialogService.notify("You don't have permissions to do it.");
                return false;
            }
            return true;
        }

        function addPermissionsForUser(customer) {
            vm.userBundle.find();
            DialogService.custom("app/customer/public-users.modal.view.html", {
                title: "Add Permissions for User",
                bundle: vm.userBundle,
                size: "modal--user-table",
                cancelTitle: "Back"
            }).result.then(function (model) {
                    model.bundle.itemsList.forEach(function (user) {
                        var stillNotPresent = !Collections.find(user, editCustomerBundle.permissions);
                        if (stillNotPresent && user.checked) addDefaultPermission(user.id, user.userName, "user");
                    });
                });
        }

        function addPermissionsForGroup() {
            vm.groupBundle.find();
            DialogService.custom("app/customer/public-groups.modal.view.html", {
                title: "Add Permissions for Group",
                bundle: vm.groupBundle,
                size: "modal--group-table",
                cancelTitle: "Back"
            }).result.then(function (model) {
                    model.bundle.groupList.forEach(function (group) {
                        var alreadyPresent = !!Collections.find(group, editCustomerBundle.permissions);
                        if (!alreadyPresent && group.checked) addDefaultPermission(group.id, group.name, "group");
                    });
                });
        }

        function addDefaultPermission(id, name, type) {
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
            editCustomerBundle.permissions.push(defaultPermission);
        }
    }]);