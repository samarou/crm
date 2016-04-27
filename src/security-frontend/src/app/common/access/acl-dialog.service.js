/**
 * Created by anton.charnou on 12.04.2016.
 */
(function () {
	'use strict';
	angular
			.module('securityManagement')
			.service('AclDialogServiceBuilder', AclDialogServiceBuilder);

	/** @ngInject */
	function AclDialogServiceBuilder(Collections, DialogService, GroupBundle, SearchBundle) {
		var vm = {};

		vm.groupBundle = GroupBundle.publicMode();
		vm.userBundle = SearchBundle.userPublicMode();

    function createAclServiceAction(aclService) {
      return function removePermissions(scope) {
        aclService.removeAcls(scope.acls.filter(function (p) {
          return p.checked;
        })).then(function () {
          aclService.getAcls().then(function (response) {
            scope.acls = response.data;
          })
        });
      }
    }

		function addAclsForUser(scope) {
			vm.userBundle.find();
			DialogService.custom('app/common/access/public-users.modal.view.html', {
				title: 'Add Permissions for User',
				bundle: vm.userBundle,
				size: 'lg',
				cancelTitle: 'Back',
				okTitle: 'Ok'
			}).result.then(function (model) {
				model.bundle.itemsList.forEach(function (user) {
					var stillNotPresent = !Collections.find(user, scope.acls);
					if (stillNotPresent && user.checked) {
						addDefaultAcl(user.id, user.userName, 'user', scope);
					}
				});
			});
		}

		function addAclsForGroup(scope) {
			vm.groupBundle.find();
			DialogService.custom('app/common/access/public-groups.modal.view.html', {
				title: 'Add Permissions for Group',
				bundle: vm.groupBundle,
				size: 'lg',
				cancelTitle: 'Back',
				okTitle: 'Ok'
			}).result.then(function (model) {
				model.bundle.groupList.forEach(function (group) {
					var alreadyPresent = !!Collections.find(group, scope.acls);
					if (!alreadyPresent && group.checked) {
						addDefaultAcl(group.id, group.name, 'group', scope);
					}
				});
			});
		}

		function addDefaultAcl(id, name, principalTypeName, scope) {
			var defaultAcl = {
				id: id,
				name: name,
				principalTypeName: principalTypeName,
				canRead: false,
				canWrite: false,
				canCreate: false,
				canDelete: false,
				canAdmin: false
			};
			scope.acls.push(defaultAcl);
		}

		return function (aclService) {
      return {
        addAclsForUser: addAclsForUser,
        addAclsForGroup: addAclsForGroup,
        removeAcls: createAclServiceAction(aclService)
      }
    }
	}
})();
