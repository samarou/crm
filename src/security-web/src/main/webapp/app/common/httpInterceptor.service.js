(function () {
    "use strict";

    angular.module('app').factory('HttpInterceptor', ['$q', '$location', '$injector',
        function ($q, $location, $injector) {
            return {
                responseError: function (response) {
                    if (response.status === 401) {
                        var AuthService = $injector.get('AuthService');
                        if (AuthService.isAuthenticated()) {
                            AuthService.logout();
                        }
                    }
                    console.log("Redirect unathorized to login");
                    $location.path("/login");
                    return $q.reject(response);
                }
            }
        }]);

})();
