'use strict';

angular.module('ancestryApp')
	.controller('InventoryDeleteController', function($scope, $uibModalInstance, entity, Inventory) {

        $scope.inventory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Inventory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
