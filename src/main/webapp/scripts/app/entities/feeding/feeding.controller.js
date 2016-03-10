'use strict';

angular.module('ancestryApp')
    .controller('FeedingController', function ($scope, $state, Feeding) {

        $scope.feedings = [];
        $scope.loadAll = function() {
            Feeding.query(function(result) {
               $scope.feedings = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.feeding = {
                feedingDate: null,
                quantity: null,
                id: null
            };
        };
    });
