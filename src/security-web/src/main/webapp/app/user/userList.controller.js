(function () {
    'use strict';
 
    angular
        .module('app')
        .controller('UserListController', UserListController);

	UserListController.$inject = ['$location', 'UserService'];


	function UserListController($location, UserService) {
		console.log("User List Controller");

		var vm = this;
		vm.userList = UserService.GetAll;
		
	}

 
})();