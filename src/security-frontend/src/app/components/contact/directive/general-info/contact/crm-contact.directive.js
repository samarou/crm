/**
 * Created by maksim.kalenik on 23.05.2016.
 */
(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmContact', crmContact);

    /** @ngInject */
    function crmContact(imageUploadService) {
        return {
            restrict: 'E',
            templateUrl: 'app/components/contact/directive/general-info/contact/crm-contact.html',
            scope: false,
            controller: function ($scope) {
                $scope.vm.uploader = imageUploadService.getFileUploader($scope.vm);
                $scope.vm.attachment = $scope.vm.uploader.tempFile;
            }
        };

    }
})();
