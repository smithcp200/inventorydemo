'use strict';

angular.module('ancestryApp')
    .controller('ZooDetailController', function ($scope, $rootScope, $stateParams, entity, Zoo, Inventory, Animal) {
        $scope.zoo = entity;
        $scope.load = function (id) {
            Zoo.get({id: id}, function(result) {
                $scope.zoo = result;
            });
        };
        var unsubscribe = $rootScope.$on('ancestryApp:zooUpdate', function(event, result) {
            $scope.zoo = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
