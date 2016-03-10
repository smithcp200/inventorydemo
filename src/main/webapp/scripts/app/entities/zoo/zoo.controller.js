'use strict';

angular.module('ancestryApp')
    .controller('ZooController', function ($scope, $state, Zoo) {

        $scope.zoos = [];
        $scope.loadAll = function() {
            Zoo.query(function(result) {
               $scope.zoos = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.zoo = {
                zooName: null,
                id: null
            };
        };
    });
