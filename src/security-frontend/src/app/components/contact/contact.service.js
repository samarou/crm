(function () {
    'use strict';

    angular
        .module('crm.contact')
        .factory('contactService', contactService);

    /** @ngInject */
    function contactService($http) {
        return {
            getAll: getAll,
            create: create,
            get: get,
            update: update,
            remove: remove,
            find: find,
            getAcls: getAcls,
            updateAcls: updateAcls,
            removeAcl: removeAcl,
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

        function getAcls(id) {
            return $http.get('rest/contacts/' + id + '/acls');
        }

        function updateAcls(id, acls) {
            return $http.put('rest/contacts/' + id + '/acls', acls);
        }

        function removeAcl(id, aclId) {
            return $http.delete('rest/contacts/' + id + '/acls/' + aclId)
        }

        function isAllowed(contactId, permission) {
            return $http.get('rest/contacts/' + contactId + '/actions/' + permission);
        }

        function getAttachment(attachment) {
            if (attachment.contactId) {
                // return $window.open('rest/contacts/' + attachment.contactId + '/attachments/' + attachment.id, '_blank');
                return $http({
                    method: 'GET',
                    url: 'rest/contacts/' + attachment.contactId + '/attachments/' + attachment.id
                })
                    .then(function (response) {
                        // return $http.get('rest/contacts/' + attachment.contactId + '/attachments/' + attachment.id).then(function (response) {
                        var data = new Blob([response.data], {type: response.headers('content-type')});
                        FileSaver.saveAs(data, attachment.name);
                        // saveData(response.data, attachment.name, response.headers('content-type'));
                    });
            }
        }

        var saveData = (function () {
            var a = document.createElement('a');
            document.body.appendChild(a);
            a.style = 'display: none';
            return function (data, fileName, type) {
                // var json = JSON.stringify(data);
                console.log(data);
                var file = new File([data], fileName, {type: type});
                console.log(file);
                var url = window.URL.createObjectURL(file);
                a.href = url;
                a.target = '_blank';
                // a.download = fileName;
                a.click();
                window.URL.revokeObjectURL(url);
            };
        }());

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
