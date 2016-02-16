/**
 * @author yauheni.putsykovich
 */

angular.module("app").factory("Handler", [function () {
    this.handleError = handleError;

    return this;

    function handleError(message) {
        return function (response) {
            return {
                message: message,
                response: response,
                isSuccess: true
            }
        }
    }
}]);
