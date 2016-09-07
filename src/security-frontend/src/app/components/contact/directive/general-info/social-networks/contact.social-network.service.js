/**
 * Created by maksim.kalenik on 07.06.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('contactSocialNetworkService', contactSocialNetworkService);

    /** @ngInject */
    function contactSocialNetworkService(contactService, dialogService, contactCommonService) {

        var detailsUrl = 'app/components/contact/directive/general-info/social-networks/contact.social-network.details.view.html';

        return {
            add: add,
            edit: edit,
            remove: remove,
            getTypeName: getTypeName,
            checkUrl: checkUrl
        };

        function add(scope) {
            openAddDialog(scope).then(function (model) {
                scope.contact.socialNetworks.push(model.account);
            });
        }

        function edit(account, scope) {
            openEditDialog(account, scope).then(function (model) {
                angular.copy(model.account, account);
            });
        }

        function remove(scope) {
            return contactCommonService.remove(scope.contact, scope.contact.socialNetworks, contactService.removeSocialNetworkAccount);
        }

        function getTypeName(id, types) {
            var result = null;
            types.forEach(function (o) {
                if (o.id == id) {
                    result = o.name;
                }
            });
            return result;
        }

        function openAddDialog(scope) {
            return dialogService.custom(detailsUrl, {
                title: 'Add Social Network account',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                account: {},
                socialNetworks: scope.dictionary.socialNetworks
            }).result;
        }

        function openEditDialog(account, scope) {
            return dialogService.custom(detailsUrl, {
                title: 'Update Social Network account',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                account: angular.copy(account),
                socialNetworks: scope.dictionary.socialNetworks
            }).result;
        }

        function checkUrl() {

        }
    }
})();
