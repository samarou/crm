/**
 * Created by maksim.kalenik on 06.06.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('contactEmailService', contactEmailService);

    /** @ngInject */
    function contactEmailService(contactService, dialogService, contactCommonService) {

        var detailsUrl = 'app/components/contact/directive/contact-info/emails/contact.email.details.view.html';

        return {
            add: add,
            edit: edit,
            remove: remove,
            getTypeName: getTypeName
        };

        function add(scope) {
            openAddDialog(scope).then(function (model) {
                scope.contact.emails.push(model.email);
            });
        }

        function edit(email, scope) {
            openEditDialog(email, scope).then(function (model) {
                angular.copy(model.email, email);
            });
        }

        function remove(scope) {
            return contactCommonService.remove(scope.contact, scope.contact.emails, contactService.removeEmail);
        }

        function getTypeName(type, types) {
            var result = null;
            if (types) {
                types.forEach(function (o) {
                    if (o.name == type) {
                        result = o.value;
                    }
                });
            }
            return result;
        }

        function openAddDialog(scope) {
            return dialogService.custom(detailsUrl, {
                title: 'Add Email',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                email: {},
                emailTypes: scope.dictionary.emailTypes
            }).result;
        }

        function openEditDialog(email, scope) {
            return dialogService.custom(detailsUrl, {
                title: 'Update Email',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                email: angular.copy(email),
                emailTypes: scope.dictionary.emailTypes
            }).result;
        }
    }
})();
