'use strict';

angular.module('ancestryApp')
    .controller('AnimalDetailController', function ($scope, $rootScope, $stateParams, entity, Animal, AnimalSpecies, Feeding) {
        $scope.animal = entity;
        $scope.load = function (id) {
            Animal.get({id: id}, function(result) {
                $scope.animal = result;
            });
        };
        var unsubscribe = $rootScope.$on('ancestryApp:animalUpdate', function(event, result) {
            $scope.animal = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
