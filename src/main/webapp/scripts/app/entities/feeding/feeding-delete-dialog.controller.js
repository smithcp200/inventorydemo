'use strict';

angular.module('ancestryApp')
	.controller('FeedingDeleteController', function($scope, $uibModalInstance, entity, Feeding) {

        $scope.feeding = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Feeding.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
