'use strict';

angular.module('ancestryApp')
    .factory('Inventory', function ($resource, DateUtils) {
        return $resource('api/inventorys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.inventoryDate = DateUtils.convertLocaleDateFromServer(data.inventoryDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.inventoryDate = DateUtils.convertLocaleDateToServer(data.inventoryDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.inventoryDate = DateUtils.convertLocaleDateToServer(data.inventoryDate);
                    return angular.toJson(data);
                }
            }
        });
    });
