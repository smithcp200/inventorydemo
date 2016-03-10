'use strict';

angular.module('ancestryApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


