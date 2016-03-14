/**
 * @author yauheni.putsykovich
 */
angular.module("app").service("SearchBundle", ["UserService", "CustomerService", "Collections", "Util",
    function (UserService, CustomerService, Collections, Util) {
        function createCommonBundle(){
            var bundle = {};

            var pageSize = 10;

            bundle.isSelectAll = false;
            bundle.itemsList = [];

            bundle.paging = {
                totalCount: 0,
                itemsPerPage: pageSize,
                currentPage: 1,
                visiblePages: 5
            };

            bundle.paging.onPageChanged = function () {
                bundle.filter.from = (bundle.paging.currentPage - 1) * pageSize;
                bundle.find();
            };

            bundle.sortProperties = {
                firstName: {name: "firstName", asc: true, enabled: true},
                lastName: {name: "lastName", asc: true, enabled: false},
                email: {name: "email", asc: true, enabled: false}
            };

            bundle.filter = {
                from: 0,
                count: pageSize,//todo: extract to config
                text: null,
                sortProperty: null,
                sortAsc: true
            };
            bundle.filter.sortProperty = bundle.sortProperties.firstName.name;
            bundle.filter.sortAsc = bundle.sortProperties.firstName.asc;

            bundle.find = function find() {
                //nulling, to prevent empty parameters in url
                angular.forEach(bundle.filter, function (value, key) {
                    if (typeof(value) !== "string") return;
                    bundle.filter[key] = !!value ? value : null;
                });
                bundle.performSeach(bundle.filter).then(function (response) {
                    bundle.itemsList = response.data.data;
                    var totalCount = response.data.totalCount;
                    var totalPages = Math.ceil(totalCount / bundle.filter.count) || 1;
                    bundle.paging.totalCount = totalCount;
                    bundle.paging.visiblePages = totalPages;
                    bundle.isSelectedAll = false;
                });
            };
            bundle.typing = Util.createDelayTypingListener(bundle.find, 500);

            bundle.sortBy = function (property) {
                console.log("property: ", property);
                angular.forEach(bundle.sortProperties, function (sortProperty) {
                    sortProperty.enabled = false;
                });
                property.enabled = true;
                property.asc = !property.asc;
                bundle.filter.sortAsc = property.asc;
                bundle.filter.sortProperty = property.name;
                bundle.find();
            };

            bundle.selectAll = function (checked) {
                bundle.itemsList.forEach(function (user) {
                    user.checked = checked;
                });
            };

            bundle.selectOne = function () {
                bundle.isSelectedAll = bundle.itemsList.every(function (user) {
                    return user.checked;
                });
            };

            return bundle;
        }

        return {
            userPublicMode: function () {
                var bundle = createCommonBundle();
                bundle.performSeach = UserService.findPublicUsers;
                bundle.sortProperties.userName = {name: "userName", asc: true, enabled: false};
                return bundle;
            },
            userSecuredMode: function () {
                var bundle = createCommonBundle();
                bundle.performSeach = UserService.find;
                bundle.sortProperties.userName = {name: "userName", asc: true, enabled: false};
                bundle.filter.groupId = null;
                bundle.filter.roleId = null;
                bundle.filter.active = true;
                return bundle;
            },
            customerMode: function () {
                var bundle = createCommonBundle();
                bundle.performSeach = CustomerService.find;
                bundle.sortProperties.address = {name: "address", asc: true, enabled: false};
                return bundle;
            }
        };
    }]);
