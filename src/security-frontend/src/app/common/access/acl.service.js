(function () {
    'use strict';

    angular
        .module('crm.acl')
        .service('aclServiceBuilder', aclServiceBuilder);

    /** @ngInject */
    function aclServiceBuilder(collections, dialogService, groupSearch, searchService, $q) {
        var vm = {};

        vm.groupBundle = groupSearch.publicMode();
        vm.userBundle = searchService.userPublicMode();

        return function (getid, aclService) {
            return {
                addAclsForUser: addAclsForUser,
                addAclsForGroup: addAclsForGroup,
                removeAcls: createRemoveAclsAction(getid, aclService)
            };
        };

        function principalAclComparator(principal, acl) {
            return principal.id === acl.principalId;
        }

        function aclComparator(acl1, acl2) {
            return acl1.principalId === acl2.principalId;
        }

        function createRemoveAclsAction(getid, service) {
            return function (scope) {
                var checkedAcls = scope.acls.filter(function (acl) {
                    return acl.checked;
                });
                if (getid()) {
                    $q.all(
                        ([].concat(checkedAcls)).map(function (acl) {
                            return service.removeAcl(getid(), acl.principalId);
                        })
                    ).then(function () {
                        service.getAcls(getid()).then(function (response) {
                            scope.acls = response.data;
                        });
                    });
                } else {
                    scope.acls = collections.difference(scope.acls, checkedAcls, aclComparator);
                }
            };
        }

        function addAclsForUser(scope) {
            vm.userBundle.find();
            dialogService.custom('app/common/access/public-users.modal.view.html', {
                title: 'Add Permissions for User',
                bundle: vm.userBundle,
                size: 'lg',
                cancelTitle: 'Back',
                okTitle: 'Ok'
            }).result.then(function (model) {
                model.bundle.itemsList.forEach(function (user) {
                    var stillNotPresent = !collections.find(user, scope.acls, principalAclComparator);
                    if (stillNotPresent && user.checked) {
                        addDefaultAcl(user.id, user.userName, 'user', scope);
                    }
                });
            });
        }

        function addAclsForGroup(scope) {
            vm.groupBundle.find();
            dialogService.custom('app/common/access/public-groups.modal.view.html', {
                title: 'Add Permissions for Group',
                bundle: vm.groupBundle,
                size: 'lg',
                cancelTitle: 'Back',
                okTitle: 'Ok'
            }).result.then(function (model) {
                model.bundle.groupList.forEach(function (group) {
                    var stillNotPresent = !collections.find(group, scope.acls, principalAclComparator);
                    if (stillNotPresent && group.checked) {
                        addDefaultAcl(group.id, group.name, 'group', scope);
                    }
                });
            });
        }

        function addDefaultAcl(principalId, name, principalTypeName, scope) {
            var defaultAcl = {
                id: null,
                principalId: principalId,
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
    }
})();
