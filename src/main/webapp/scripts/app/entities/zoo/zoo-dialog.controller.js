'use strict';

angular.module('ancestryApp').controller('ZooDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Zoo', 'Inventory', 'Animal',
        function($scope, $stateParams, $uibModalInstance, entity, Zoo, Inventory, Animal) {

        $scope.zoo = entity;
        $scope.inventorys = Inventory.query();
        $scope.animals = Animal.query();
        $scope.load = function(id) {
            Zoo.get({id : id}, function(result) {
                $scope.zoo = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ancestryApp:zooUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.zoo.id != null) {
                Zoo.update($scope.zoo, onSaveSuccess, onSaveError);
            } else {
                Zoo.save($scope.zoo, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
