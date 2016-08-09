/**
 * Created by yauheni.putsykovich on 01.06.2016.
 */

(function () {
    'use strict';

    angular
        .module('crm.task')
        .controller('TaskEditController', TaskEditController);

    /** @ngInject */
    function TaskEditController(taskService, taskSecurityService, authService, taskCommonService, dialogService, $stateParams, $q) {
        var editCommentUrl = 'app/components/task/directive/task-comment-edit-modal.view.html';
        
        var vm = this;

        vm.canEdit = true;
        vm.timeless = false;
        vm.title = 'Edit Task';
        vm.submitText = 'Save';

        vm.addComment = addComment;
        vm.removeComment = removeComment;
        vm.editComment = editComment;

        (function () {
            taskCommonService.initContext(vm);
            taskService.getTaskById($stateParams.id).then(function (response) {
                vm.task = response.data;
                /*need converting because date have a millis format*/
                vm.task.startDate = vm.task.startDate ? new Date(vm.task.startDate) : null;
                vm.task.endDate = vm.task.endDate ? new Date(vm.task.endDate)  : null;
                vm.canComment = vm.task.assignee.userName == authService.getUserName();
                $q.all([
                    taskService.getAcls(vm.task.id),
                    taskSecurityService.checkAdminPermission(vm.task.id),
                    taskSecurityService.checkEditPermission(vm.task.id)
                ]).then(function (responses) {
                    vm.aclHandler.acls = responses[0].data;
                    vm.aclHandler.canEdit = responses[1];
                    vm.canEdit = responses[2];

                    vm.canEditDateTime = vm.canEdit;
                });
            });
        })();

        function addComment() {
            var comment = {
                taskId: vm.task.id,
                text: vm.newComment,
                dateCreated: Date.now()
            };
            taskService.addComment(vm.task.id, comment).then(function (response) {
                comment.id = response.data;
                vm.task.comments.push(comment);
                vm.newComment = '';
            });
        }

        function removeComment(comment) {
            taskService.removeComment(comment.id).then(function () {
                vm.task.comments.splice(vm.task.comments.indexOf(comment), 1);
            });
        }

        function editComment(comment, index) {
            openEditDialog(comment).then(function (model) {
                comment.text = model.comment.text;
                taskService.editComment(comment).then(function () {
                    vm.task.comments[index] = comment;
                });
            });
        }

        function openEditDialog(comment) {
            return dialogService.custom(editCommentUrl, {
                title: 'Edit comment',
                size: 'modal--user-table',
                cancelTitle: 'Cancel',
                okTitle: 'Save',
                comment: angular.copy(comment)
            }).result;
        }
    }
})();
