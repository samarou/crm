/**
 * Created by yauheni.putsykovich on 13.06.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.task')
        .factory('taskSecurityService', taskSecurityService);

    /** @ngInject */
    function taskSecurityService(taskService, permissions, $q, dialogService, $log) {
        return {
            checkReadPermission: checkReadPermission,
            checkDeletePermissionForList: checkDeletePermissionForList,
            checkEditPermission: checkEditPermission
        };

        function checkReadPermission(securedObject) {
            return service.isAllowed(securedObject.id, permissions.read).then(function (response) {
                if (!response.data) {
                    dialogService.notify('You haven\'t permissions to edit that item!');
                    return $q.reject(response);
                }
                return $q.resolve(response);
            });
        }

        function checkDeletePermissionForList(securedObjects) {
            var tasks = [];
            securedObjects.forEach(function (so) {
                var task = checkDeletePermission(so);
                tasks.push(task);
            });
            return $q.all(tasks).then(function (list) {
                $log.log(list);
                return $q.resolve(list);
            }).catch(function () {
                    dialogService.notify('You don\'t have permissions to do it.');
                    return $q.reject();
                }
            );
        }

        function checkEditPermission(id) {
            return taskService.isAllowed(id, permissions.write).then(function (response) {
                return $q.resolve(!!response.data);
            });
        }

        function checkDeletePermission(securedObject) {
            return taskService.isAllowed(securedObject.id, permissions.delete).then(function (response) {
                if (!response.data) {
                    return $q.reject(securedObject);
                }
                return $q.resolve(securedObject);
            });
        }
    }
})();
