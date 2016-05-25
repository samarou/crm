(function () {
    'use strict';

    angular
        .module('crm.company')
        .factory('companySecurityService', companySecurityService);

    /** @ngInject */
    function companySecurityService(companyService, permissions, $q, dialogService, $log) {

        return {
            checkDeletePermissionForList: checkDeletePermissionForList,
            checkPermission: checkPermission
        };

        function checkDeletePermissionForList(companyList) {
            var tasks = [];
            companyList.forEach(function (company) {
                var task = checkPermission(company.id, permissions.delete);
                tasks.push(task);
            });
            return $q.all(tasks).then(function () {
                $log.log(companyList);
                return $q.resolve(companyList);
            }).catch(function () {
                dialogService.notify('You don\'t have permissions to do it.');
                return $q.reject();
            });
        }

        function checkPermission(id, permission) {
            return companyService.isAllowed(id, permission).then(function (response) {
                if (!response.data) {
                    return $q.reject();
                }
                $q.resolve();
            });
        }

    }

})();
