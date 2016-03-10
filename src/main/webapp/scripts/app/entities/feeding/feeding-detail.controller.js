'use strict';

angular.module('ancestryApp')
    .controller('FeedingDetailController', function ($scope, $rootScope, $stateParams, entity, Feeding, Animal, Inventory) {
        $scope.feeding = entity;
        $scope.load = function (id) {
            Feeding.get({id: id}, function(result) {
                $scope.feeding = result;
            });
        };
        var unsubscribe = $rootScope.$on('ancestryApp:feedingUpdate', function(event, result) {
            $scope.feeding = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
