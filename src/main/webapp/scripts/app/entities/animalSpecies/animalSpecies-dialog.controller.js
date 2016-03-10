'use strict';

angular.module('ancestryApp').controller('AnimalSpeciesDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'AnimalSpecies', 'Animal',
        function($scope, $stateParams, $uibModalInstance, entity, AnimalSpecies, Animal) {

        $scope.animalSpecies = entity;
        $scope.animals = Animal.query();
        $scope.load = function(id) {
            AnimalSpecies.get({id : id}, function(result) {
                $scope.animalSpecies = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ancestryApp:animalSpeciesUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.animalSpecies.id != null) {
                AnimalSpecies.update($scope.animalSpecies, onSaveSuccess, onSaveError);
            } else {
                AnimalSpecies.save($scope.animalSpecies, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
