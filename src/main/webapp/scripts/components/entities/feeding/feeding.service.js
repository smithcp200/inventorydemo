'use strict';

angular.module('ancestryApp')
    .factory('Feeding', function ($resource, DateUtils) {
        return $resource('api/feedings/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.feedingDate = DateUtils.convertLocaleDateFromServer(data.feedingDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.feedingDate = DateUtils.convertLocaleDateToServer(data.feedingDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.feedingDate = DateUtils.convertLocaleDateToServer(data.feedingDate);
                    return angular.toJson(data);
                }
            }
        });
    });
