(function () {
    'use strict';

    angular
        .module('crm.contact')
        .directive('crmAvatarImage', ['$http',crmAvatarImage]);

    /** @ngInject */
    function crmAvatarImage($http) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                element.on('error', function () {
                    attrs.$set('src', "./assets/images/default-avatar.png");
                });
                attrs.$observe('contact', function (value) {
                    if (value) {
                        $http.get("rest/images/contact/" + value + '?' + getCurrentTime(),
                            {responseType:"arraybuffer"})
                            .success(function(data){
                                var arrayBufferView = new Uint8Array( data );
                                var blob = new Blob( [ arrayBufferView ], { type: "image/png" } );
                                var urlCreator = window.URL || window.webkitURL;
                                var imageUrl = urlCreator.createObjectURL( blob );
                                attrs.$set('ngSrc', imageUrl);
                            });
                    }
                });
                scope.ok = function(){
                    angular.element('#imageID').attr('src',scope.vm.contact.photoUrl);
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
