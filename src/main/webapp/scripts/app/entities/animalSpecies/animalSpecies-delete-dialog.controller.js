'use strict';

angular.module('ancestryApp')
	.controller('AnimalSpeciesDeleteController', function($scope, $uibModalInstance, entity, AnimalSpecies) {

        $scope.animalSpecies = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            AnimalSpecies.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
