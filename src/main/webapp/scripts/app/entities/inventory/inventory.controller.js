'use strict';

angular.module('ancestryApp')
    .controller('InventoryController', function ($scope, $state, Inventory) {

        $scope.inventorys = [];
        $scope.loadAll = function() {
            Inventory.query(function(result) {
               $scope.inventorys = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.inventory = {
                inventoryDate: null,
                quantity: null,
                id: null
            };
        };
    });
