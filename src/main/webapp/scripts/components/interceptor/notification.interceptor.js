 'use strict';

angular.module('ancestryApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-ancestryApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-ancestryApp-params')});
                }
                return response;
            }
        };
    });
