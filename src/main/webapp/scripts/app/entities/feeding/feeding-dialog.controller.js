'use strict';

angular.module('ancestryApp').controller('FeedingDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Feeding', 'Animal', 'Inventory',
        function($scope, $stateParams, $uibModalInstance, entity, Feeding, Animal, Inventory) {

        $scope.feeding = entity;
        $scope.animals = Animal.query();
        $scope.inventorys = Inventory.query();
        $scope.load = function(id) {
            Feeding.get({id : id}, function(result) {
                $scope.feeding = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ancestryApp:feedingUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.feeding.id != null) {
                Feeding.update($scope.feeding, onSaveSuccess, onSaveError);
            } else {
                Feeding.save($scope.feeding, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForFeedingDate = {};

        $scope.datePickerForFeedingDate.status = {
            opened: false
        };

        $scope.datePickerForFeedingDateOpen = function($event) {
            $scope.datePickerForFeedingDate.status.opened = true;
        };
}]);
