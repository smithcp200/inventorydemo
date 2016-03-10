'use strict';

angular.module('ancestryApp')
    .factory('AnimalSpecies', function ($resource, DateUtils) {
        return $resource('api/animalSpeciess/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
