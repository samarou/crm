/**
 * Created by maksim.kalenik on 07.06.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('contactWorkplaceService', contactWorkplaceService);

    /** @ngInject */
    function contactWorkplaceService(contactService, dialogService, contactCommonService) {

        var detailsUrl = 'app/components/contact/directive/work-experience/workplaces/contact.workplace.details.view.html';

        return {
            add: add,
            edit: edit,
            remove: remove
        };

        function add(scope) {
            openAddDialog().then(function (model) {
                scope.contact.workplaces.push(model.workplace);
            });
        }

        function edit(workplace) {
            openEditDialog(workplace).then(function (model) {
                angular.copy(model.workplace, workplace);
            });
        }

        function remove(scope) {
            return contactCommonService.remove(scope.contact, scope.contact.workplaces, contactService.removeWorkplace);
        }

        function openAddDialog() {
            return dialogService.custom(detailsUrl, {
                title: 'Add Workplace',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                workplace: {},
                details:{now:new Date()}
            }).result;
        }

        function openEditDialog(workplace) {
            return dialogService.custom(detailsUrl, {
                title: 'Update Workplace',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                workplace: angular.copy(workplace)
            }).result;
        }
    }
})();
