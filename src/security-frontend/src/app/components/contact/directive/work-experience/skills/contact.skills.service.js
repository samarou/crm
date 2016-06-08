/**
 * Created by maksim.kalenik on 08.06.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('contactSkillService', contactSkillService);

    /** @ngInject */
    function contactSkillService(contactService, dialogService, contactCommonService) {

        var detailsUrl = 'app/components/contact/directive/work-experience/skills/contact.skills.details.view.html';

        return {
            add: add,
            edit: edit,
            remove: remove
        };

        function add(scope) {
            openAddDialog().then(function (model) {
                scope.contact.skills.push(model.skill);
            });
        }

        function edit(skill) {
            openEditDialog(skill).then(function (model) {
                angular.copy(model.skill, skill);
            });
        }

        function remove(scope) {
            return contactCommonService.remove(scope.contact, scope.contact.skills, contactService.removeSkill);
        }

        function openAddDialog() {
            return dialogService.custom(detailsUrl, {
                title: 'Add Skill',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Add',
                skill: {}
            }).result;
        }

        function openEditDialog(skill) {
            return dialogService.custom(detailsUrl, {
                title: 'Update Skill',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                skill: angular.copy(skill)
            }).result;
        }
    }
})();
