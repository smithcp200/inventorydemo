'use strict';

angular.module('ancestryApp')
    .controller('InventoryDetailController', function ($scope, $rootScope, $stateParams, entity, Inventory, Zoo, Feeding) {
        $scope.inventory = entity;
        $scope.load = function (id) {
            Inventory.get({id: id}, function(result) {
                $scope.inventory = result;
            });
        };
        var unsubscribe = $rootScope.$on('ancestryApp:inventoryUpdate', function(event, result) {
            $scope.inventory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
