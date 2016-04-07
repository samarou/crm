/**
 * @author yauheni.putsykovich
 */
(function () {
	'use strict';

	angular
			.module('securityManagement')
			.controller('CustomersController', CustomersController);

	/** @ngInject */
	function CustomersController($q, AuthService, CustomerService, GroupBundle, SearchBundle, Collections, DialogService) {
		'use strict';
		var vm = this;

		var editCustomerBundle = {
			customer: null,
			permissions: null,
			canEdit: false,
			actions: {
				addPermissionsForUser: addPermissionsForUser,
				addPermissionsForGroup: addPermissionsForGroup,
				removePermissions: removePermissions
			}
		};

		var permissions = {
			read: 'read',
			write: 'write',
			create: 'create',
			delete: 'delete',
			admin: 'admin'
		};

		vm.isManager = AuthService.isManager();
		vm.groupBundle = GroupBundle.publicMode();
		vm.userBundle = SearchBundle.userPublicMode();
		vm.searchCustomerBundle = SearchBundle.customerMode();
		vm.searchCustomerBundle.find();

		vm.create = function () {
			editCustomerBundle.customer = {};
			editCustomerBundle.permissions = [];
			openCustomerDialog({title: 'Create Customer'});
		};

		vm.edit = function (customer) {
			CustomerService.isAllowed(customer.id, permissions.read).then(function (response) {
				if (!response.data) {
					DialogService.notify('You haven\'t permissions to edit that customer!');
					return;
				}
				CustomerService.isAllowed(customer.id, permissions.write).then(function (response) {
					editCustomerBundle.canEdit = !!response.data;
					CustomerService.getPermissions(customer.id).then(function (response) {
						editCustomerBundle.customer = angular.copy(customer);
						editCustomerBundle.permissions = response.data;
						openCustomerDialog({
							title: 'Editing Customer',
							okTitle: editCustomerBundle.canEdit ? 'Update' : null,
							cancelTitle: editCustomerBundle.canEdit ? 'Cancel' : 'Ok'
						});
					});
				});
			});
		};


		vm.remove = function () {
			openRemoveDialog().then(function () {
				var tasks = [];
				var allCustomersCanBeDeleted = true;
				var checkedCustomers = vm.searchCustomerBundle.itemsList.filter(function (customer) {
					return customer.checked;
				});
				checkedCustomers.forEach(function (customer) {
					var task = CustomerService.isAllowed(customer.id, permissions.delete).then(function (response) {
						if (!response.data) {
							allCustomersCanBeDeleted = false;
						}
					});
					tasks.push(task);
				});
				$q.all(tasks).then(function () {
					if (!allCustomersCanBeDeleted) {
						DialogService.notify('You don\'t have permissions to do it.');
						return;
					}

					tasks = [];
					checkedCustomers.forEach(function (customer) {
						if (customer.checked) {
							tasks.push(CustomerService.remove(customer.id));
						}
					});
					if (allCustomersCanBeDeleted) {
						$q.all(tasks).then(vm.searchCustomerBundle.find)
					}
				});
			});
		};

		function openRemoveDialog() {
			return DialogService.confirm('Do you want to delete this customers?').result;
		}

		function openCustomerDialog(model) {
			model.bundle = editCustomerBundle;
			DialogService.custom('app/components/customer/customer.modal.view.html', model).result.then(updateCustomer);
		}

		function updateCustomer(model) {
			var customer = model.bundle.customer;
			if (customer.id) {
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
				if (p.checked) {
					tasks.push(CustomerService.removePermissions(customer.id, p.id));
				}
			});
			$q.all(tasks).then(function () {
				CustomerService.getPermissions(customer.id).then(function (response) {
					editCustomerBundle.permissions = response.data;
				})
			});
		}

		function addPermissionsForUser(customer) {
			vm.userBundle.find();
			DialogService.custom('app/components/customer/public-users.modal.view.html', {
				title: 'Add Permissions for User',
				bundle: vm.userBundle,
				size: 'modal--user-table',
				cancelTitle: 'Back'
			}).result.then(function (model) {
				model.bundle.itemsList.forEach(function (user) {
					var stillNotPresent = !Collections.find(user, editCustomerBundle.permissions);
					if (stillNotPresent && user.checked) {
						addDefaultPermission(user.id, user.userName, 'user');
					}
				});
			});
		}

		function addPermissionsForGroup() {
			vm.groupBundle.find();
			DialogService.custom('app/components/customer/public-groups.modal.view.html', {
				title: 'Add Permissions for Group',
				bundle: vm.groupBundle,
				size: 'modal--group-table',
				cancelTitle: 'Back'
			}).result.then(function (model) {
				model.bundle.groupList.forEach(function (group) {
					var alreadyPresent = !!Collections.find(group, editCustomerBundle.permissions);
					if (!alreadyPresent && group.checked) {
						addDefaultPermission(group.id, group.name, 'group');
					}
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
	}
})();