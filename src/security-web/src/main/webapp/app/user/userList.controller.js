(function () {
    'use strict';
 
    angular
        .module('app')
        .controller('UserListController', UserListController);

	UserListController.$inject = ['$location', 'UserService'];

	function UserListController($location, UserService) {
		console.log("User List Controller");

		var vm = this;
		UserService.getAll(function (response) {
			vm.userList = response.data;
		});
		UserService.getAll(function (data) {
			vm.userList = data.data;
		});
	}
})();