(function () {
    'use strict';

    angular
        .module('crm.contact')
        .factory('contactService', contactService);

    /** @ngInject */
    function contactService($http, $window) {
        return {
            getAll: getAll,
            create: create,
            get: get,
            update: update,
            remove: remove,
            find: find,
            getPermissions: getPermissions,
            updatePermissions: updatePermissions,
            removePermissions: removePermissions,
            isAllowed: isAllowed,
            getAttachments: getAttachments,
            addAttachments: addAttachments,
            addAttachment: addAttachment,
            updateAttachment: updateAttachment,
            removeAttachment: removeAttachment,
            uploadAttachment: uploadAttachment,
            getAttachment: getAttachment
        };

        function getAll() {
            return $http.get('rest/contacts');
        }

        function create(contact) {
            return $http.post('rest/contacts', contact);
        }

        function get(id) {
            return $http.get('rest/contacts/' + id);
        }

        function update(contact) {
            return $http.put('rest/contacts', contact);
        }

        function remove(id) {
            return $http.delete('rest/contacts/' + id);
        }

        function find(filter) {
            return $http.get('rest/contacts/find', {params: filter});
        }

        function getPermissions(id) {
            return $http.get('rest/contacts/' + id + '/permissions');
        }

        function updatePermissions(id, permissions) {
            return $http.put('rest/contacts/' + id + '/permissions', permissions);
        }

        function removePermissions(id, permissionId) {
            return $http.delete('rest/contacts/' + id + '/permissions/' + permissionId)
        }

        function isAllowed(contactId, permission) {
            return $http.get('rest/contacts/' + contactId + '/actions/' + permission);
        }

        function getAttachment(attachment) {
            if (attachment.contactId) {
                var url = 'rest/files/contacts/' + attachment.contactId + '/attachments/' + attachment.id;
                return $http.get(url).then(function () {
                    $window.open(url);
                });
            }
        }

        function getAttachments(id) {
            return $http.get('rest/contacts/' + id + '/attachments');
        }

        function addAttachments(id, attachments) {
            return $http.post('rest/contacts/' + id + '/attachments', attachments);
        }

        function addAttachment(id, attachment) {
            var formData = new FormData();
            formData.append('attachment', angular.toJson(attachment));
            formData.append('file', attachment.file);
            return $http.post('rest/contacts/' + id + '/attachments',
                formData,
                {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                });
        }


        service.updateAttachment = function (id, attachment) {
            return $http.put('rest/contacts/' + id + '/attachments', attachment);
        };

        function removeAttachment(id, attachmentId) {
            return $http.delete('rest/contacts/' + id + '/attachments/' + attachmentId);
        }

        function uploadAttachment(id, attachment, file) {
            return $http.delete('rest/contacts/' + id + '/attachments', attachment, file);
        }
    }
})();
