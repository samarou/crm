(function () {
    "use strict";

    angular.module('app').factory('HttpInterceptor', ['$q', '$location', '$injector',
        function ($q, $location, $injector) {
            return {
                responseError: function (response) {
                    switch (response.status) {
                        case 401:
                            var AuthService = $injector.get('AuthService');
                            if (AuthService.isAuthenticated()) {

                            }


                            console.log("Redirect unathorized to login");
                            $location.path("/login");
                            break;
                        /*case 0:
                         //don't get a response back
                         break;
                         case 400:
                         //other errors
                         break;
                         case 500:
                         //server error
                         break;
                         default:
                         //other errors
                         break;*/
                    }
                    return $q.reject(response);
                }
            }
        }]);
    /*
     return {
     'request': function (config) {

     //injected manually to get around circular dependency problem.
     var AuthService = $injector.get('Auth');

     if (!AuthService.isAuthenticated()) {
     $location.path('/login');
     } else {
     //add session_id as a bearer token in header of all outgoing HTTP requests.
     var currentUser = AuthService.getCurrentUser();
     if (currentUser !== null) {
     var sessionId = AuthService.getCurrentUser().sessionId;
     if (sessionId) {
     config.headers.Authorization = 'Bearer ' + sessionId;
     }
     }
     }

     //add headers
     return config;
     },
     'responseError': function (rejection) {
     if (rejection.status === 401) {

     //injected manually to get around circular dependency problem.
     var AuthService = $injector.get('Auth');

     //if server returns 401 despite user being authenticated on app side, it means session timed out on server
     if (AuthService.isAuthenticated()) {
     AuthService.appLogOut();
     }
     $location.path('/login');
     return $q.reject(rejection);
     }
     }
     };
     */

})();
