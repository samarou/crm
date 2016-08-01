(function () {
    'use strict';

    angular
        .module('crm.contact')
        .service('imageUploadService', imageUploadService);

    /** @ngInject */
    function imageUploadService(dialogService, FileUploader, MAX_FILE_SIZE) {

        return {
            getFileUploader: getFileUploader
        };

        function getFileUploader(vm) {
            return new FileUploader(
                {
                    tempFile: {},
                    withCredentials: true,
                    url: 'rest/files',
                    onAfterAddingFile: function (item) {
                        if (item._file.size < MAX_FILE_SIZE) {
                            this.tempFile.name = item._file.name;
                            this.uploadItem(item);
                        } else {
                            dialogService.notify('File size is too large. It should not exceed 100MB');
                            this.clearQueue();
                        }
                    },
                    onErrorItem: function (item, response) {
                        dialogService.error('File hasn\'t been uploaded because error happened: ' + response.message);
                        this.clearQueue();
                    },
                    onSuccessItem: function (item, response) {
                        if(!isImage(item)){
                            dialogService.error('You can upload only images');
                        }else{
                            var reader = new FileReader();
                            reader.onload = function (e) {
                                angular.element('#imageID').attr('src', e.target.result);
                            };
                            reader.readAsDataURL(item._file);
                            vm.contact.photoUrl = response;
                        }
                    }
                });
        }

        function isImage(item){
            var type = item._file.type;
            var mime = type.split('/');
            return mime.length !== 0 && mime[0] === "image";
        }
    }
})();
