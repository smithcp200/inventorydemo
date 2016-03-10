'use strict';

angular.module('ancestryApp')
    .controller('AnimalSpeciesDetailController', function ($scope, $rootScope, $stateParams, entity, AnimalSpecies, Animal) {
        $scope.animalSpecies = entity;
        $scope.load = function (id) {
            AnimalSpecies.get({id: id}, function(result) {
                $scope.animalSpecies = result;
            });
        };
        var unsubscribe = $rootScope.$on('ancestryApp:animalSpeciesUpdate', function(event, result) {
            $scope.animalSpecies = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
