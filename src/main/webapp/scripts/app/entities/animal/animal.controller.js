'use strict';

angular.module('ancestryApp')
    .controller('AnimalController', function ($scope, $state, Animal) {

        $scope.animals = [];
        $scope.loadAll = function() {
            Animal.query(function(result) {
               $scope.animals = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.animal = {
                animalName: null,
                id: null
            };
        };
    });
