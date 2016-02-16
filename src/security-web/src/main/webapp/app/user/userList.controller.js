(function () {
    'use strict';
 
    angular
        .module('app')
        .controller('UserListController', UserListController);

	UserListController.$inject = ['$location', 'UserService'];

	function UserListController($location, UserService) {
		console.log("User List Controller");

		var vm = this;
		UserService.GetAll(function (response) {
			vm.userList = response.data;
		});
		UserService.GetAll(function (data) {
			vm.userList = data.data;
		});
	}
})();