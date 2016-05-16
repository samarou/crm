(function () {
	'use strict';
	
	angular
			.module('crm.company')
			.factory('companyService', companyService);
	
	/** @ngInject */
	function companyService($http) {
		
		return {
			find: find,
			create: create,
			get: get,
			update: update,
			remove: remove
		}
		
		function find(filter) {
            return $http.get('rest/companies', {params: filter});
        }
		
		function create(company) {
            return $http.post('rest/companies', company);
        }
		
		function get(id) {
			return $http.get('rest/companies/' + id);
		}
		
		function update(company) {
			return $http.put('rest/companies', company);
		}
		
		function remove(id) {
			return $http.delete('rest/companies/' + id);
		}

	}
	
})();