(function () {
    'use strict';

    angular
        .module('crm.contact')
        .factory('contactService', contactService);

    /** @ngInject */
    function contactService($http, $window) {
        return {
            create: create,
            get: get,
            update: update,
            remove: remove,
            find: find,
            getAcls: getAcls,
            updateAcls: updateAcls,
            removeAcl: removeAcl,
            isAllowed: isAllowed,
            addAttachment: addAttachment,
            removeAttachment: removeAttachment,
            getAttachment: getAttachment,
            removeEmail: removeEmail,
            removeTelephone: removeTelephone,
            removeAddress: removeAddress,
            removeMessengerAccount: removeMessengerAccount,
            removeSocialNetworkAccount: removeSocialNetworkAccount,
            getDictionary: getDictionary,
            removeWorkplace: removeWorkplace,
            removeSkill: removeSkill,
            parseProfile: parseProfile
        };

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
            return $http.get('rest/contacts', {params: filter});
        }

        function getAcls(id) {
            return $http.get('rest/contacts/' + id + '/acls');
        }

        function updateAcls(id, acls) {
            return $http.put('rest/contacts/' + id + '/acls', acls);
        }

        function removeAcl(id, aclId) {
            return $http.delete('rest/contacts/' + id + '/acls/' + aclId);
        }

        function isAllowed(contactId, permission) {
            return $http.get('rest/contacts/' + contactId + '/actions/' + permission);
        }

        function getAttachment(contactId, attachment) {
            var url = 'rest/contacts/files/' + contactId + '/attachments/' + attachment.id;
            return $http.get(url + '/check').then(function () {
                $window.open('rest/files/contacts/' + contactId + '/attachments/' + attachment.id + '/' + attachment.name);
            });
        }

        function addAttachment(id, attachment) {
            return $http.post('rest/contacts/' + id + '/attachments', {
                attachment: attachment,
                filePath: attachment.filePath
            });
        }

        function removeAttachment(id, attachmentId) {
            return $http.delete('rest/contacts/' + id + '/attachments/' + attachmentId);
        }

        function removeEmail(id, emailId) {
            return $http.delete('rest/contacts/' + id + '/emails/' + emailId);
        }

        function removeTelephone(id, telephoneId) {
            return $http.delete('rest/contacts/' + id + '/telephones/' + telephoneId);
        }

        function removeAddress(id, addressId) {
            return $http.delete('rest/contacts/' + id + '/addresses/' + addressId);
        }

        function removeMessengerAccount(id, accountId) {
            return $http.delete('rest/contacts/' + id + '/messengers/' + accountId);
        }

        function removeSocialNetworkAccount(id, accountId) {
            return $http.delete('rest/contacts/' + id + '/social_networks/' + accountId);
        }

        function getDictionary() {
            return $http.get('rest/dictionary');
        }

        function removeWorkplace(id, workplaceId) {
            return $http.delete('rest/contacts/' + id + '/workplaces/' + workplaceId);
        }

        function removeSkill(id, skillId) {
            return $http.delete('rest/contacts/' + id + '/skills/' + skillId);
        }

        function parseProfile(profileUrl) {
            return $http.get('rest/parser/linkedIn', {params: {url: profileUrl}});
        }
    }
})();
