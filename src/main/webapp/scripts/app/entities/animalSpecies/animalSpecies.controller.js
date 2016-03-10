'use strict';

angular.module('ancestryApp')
    .controller('AnimalSpeciesController', function ($scope, $state, AnimalSpecies) {

        $scope.animalSpeciess = [];
        $scope.loadAll = function() {
            AnimalSpecies.query(function(result) {
               $scope.animalSpeciess = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.animalSpecies = {
                animalSpeciesName: null,
                id: null
            };
        };
    });
