'use strict';

angular.module('ancestryApp')
	.controller('ZooDeleteController', function($scope, $uibModalInstance, entity, Zoo) {

        $scope.zoo = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Zoo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
