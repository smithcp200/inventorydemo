'use strict';

angular.module('ancestryApp').controller('InventoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Inventory', 'Zoo', 'Feeding',
        function($scope, $stateParams, $uibModalInstance, entity, Inventory, Zoo, Feeding) {

        $scope.inventory = entity;
        $scope.zoos = Zoo.query();
        $scope.feedings = Feeding.query();
        $scope.load = function(id) {
            Inventory.get({id : id}, function(result) {
                $scope.inventory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ancestryApp:inventoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.inventory.id != null) {
                Inventory.update($scope.inventory, onSaveSuccess, onSaveError);
            } else {
                Inventory.save($scope.inventory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForInventoryDate = {};

        $scope.datePickerForInventoryDate.status = {
            opened: false
        };

        $scope.datePickerForInventoryDateOpen = function($event) {
            $scope.datePickerForInventoryDate.status.opened = true;
        };
}]);
