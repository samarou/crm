(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmAvatarImage', crmAvatarImage);

    /** @ngInject */
    function crmAvatarImage() {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var photoUrl = "/rest/images/contact/";
                var defaultImagePath = "/assets/images/default-avatar.png";
                element.on('error', function () {
                    attrs.$set('src', defaultImagePath);
                });
                attrs.$observe('contact', function (value) {
                    if (value) {
                        attrs.$set('ngSrc', photoUrl + value + '?' + getCurrentTime());
                    }
                });
                scope.ok = function(){
                    angular.element('#imageID').attr('src',scope.vm.photoUrl);
                    scope.vm.contact.photoUrl = scope.vm.photoUrl;
                    hidePopover();
                };
                scope.cancel = function(){
                    hidePopover();
                };
                scope.selectImage = function(){
                    angular.element('#attachmentFile').trigger('click');
                    hidePopover();
                };
                function hidePopover(){
                    scope.vm.isOpen = false;
                }
                function getCurrentTime() {
                    return (new Date()).getTime();
                }
            }
        };
    }
})();
