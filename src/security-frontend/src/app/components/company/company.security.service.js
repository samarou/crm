(function () {
    'use strict';

    angular
        .module('crm.company')
        .factory('companySecurityService', companySecurityService);

    /** @ngInject */
    function companySecurityService(companyService, permissions, $q, dialogService, $log) {

        return {
            checkReadPermission: checkReadPermission,
            checkDeletePermissionForList: checkDeletePermissionForList,
            checkEditPermission: checkEditPermission,
            checkAdminPermission: checkAdminPermission
        };

        function checkReadPermission(company) {
            return companyService.isAllowed(company.id, permissions.read).then(function (response) {
                if (!response.data) {
                    dialogService.notify('You don\'t have permission to edit that company!');
                    return $q.reject(response);
                }
                return $q.resolve(response);
            })
        }

        function checkDeletePermissionForList(companyList) {
            var tasks = [];
            companyList.forEach(function (company) {
                var task = checkDeletePermission(company);
                tasks.push(task);
            });
            return $q.all(tasks).then(function (companyList) {
                $log.log(companyList);
                return $q.resolve(companyList);
            }).catch(function () {
                dialogService.notify('You don\'t have permissions to do it.');
                return $q.reject();
            })
        }

        function checkEditPermission(id) {
            return companyService.isAllowed(id, permissions.write).then(function (response) {
                return $q.resolve(!!response.data);
            })
        }

        function checkAdminPermission(id) {
            return companyService.isAllowed(id, permissions.admin).then(function (response) {
                return $q.resolve(!!response.data);
            })
        }

        function checkDeletePermission(company) {
            return companyService.isAllowed(company.id, permissions.delete).then(function (response) {
                if (!response.data) {
                    return $q.reject(company);
                }
                return $q.resolve(company);
            });
        }

    }

})();
