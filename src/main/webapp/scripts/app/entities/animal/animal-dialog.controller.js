'use strict';

angular.module('ancestryApp').controller('AnimalDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Animal', 'AnimalSpecies', 'Zoo', 'Feeding',
        function($scope, $stateParams, $uibModalInstance, entity, Animal, AnimalSpecies, Zoo, Feeding) {

        $scope.animal = entity;
        $scope.animalspeciess = AnimalSpecies.query();
        $scope.zoos = Zoo.query();
        $scope.feedings = Feeding.query();
        $scope.load = function(id) {
            Animal.get({id : id}, function(result) {
                $scope.animal = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ancestryApp:animalUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.animal.id != null) {
                Animal.update($scope.animal, onSaveSuccess, onSaveError);
            } else {
                Animal.save($scope.animal, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
