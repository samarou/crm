(function () {
    'use strict';

    angular.module('crm.contact')
        .service('contactEducationService', contactEducationService);

    /** @ngInject */
    function contactEducationService(contactService, dialogService, contactCommonService) {

        var detailsUrl = "app/components/contact/directive/education-info/university/contact.education.details.view.html";

        return {
            add: add,
            edit: edit,
            remove: remove,
            getTypeName: getTypeName
        };

        function add(scope) {
            openAddDialog(scope).then(function (model) {
                scope.contact.educations.push(model.education);
            });
        }

        function edit(education, scope) {
            openEditDialog(education, scope).then(function (model) {
                angular.copy(model.education, education);
            });
        }

        function remove(scope) {
            return contactCommonService.remove(scope.contact, scope.contact.educations, contactService.removeEducation);
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

            return dialogService.custom(detailsUrl,{
                title:'Add Education Info',
                size:'modal--user-table',
                cancelTitle:'Cancel',
                okTitle:'Add',
                education:{},
                certificateTypes:scope.dictionary.certificateTypes,
                details:{
                    now:new Date()
                }
            }).result;
        }

        function openEditDialog(education, scope){
            return dialogService.custom(detailsUrl,{
                title:'Update Education Info',
                size:'modal--user-table',
                cancelTitle:'Cancel',
                okTitle:'Save',
                education:angular.copy(education),
                certificateTypes:scope.dictionary.certificateTypes
            }).result;
        }
    }

})();
