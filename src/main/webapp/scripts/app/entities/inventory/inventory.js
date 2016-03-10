'use strict';

angular.module('ancestryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('inventory', {
                parent: 'entity',
                url: '/inventorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ancestryApp.inventory.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/inventory/inventorys.html',
                        controller: 'InventoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('inventory');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('inventory.detail', {
                parent: 'entity',
                url: '/inventory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ancestryApp.inventory.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/inventory/inventory-detail.html',
                        controller: 'InventoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('inventory');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Inventory', function($stateParams, Inventory) {
                        return Inventory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('inventory.new', {
                parent: 'inventory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/inventory/inventory-dialog.html',
                        controller: 'InventoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    inventoryDate: null,
                                    quantity: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('inventory', null, { reload: true });
                    }, function() {
                        $state.go('inventory');
                    })
                }]
            })
            .state('inventory.edit', {
                parent: 'inventory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/inventory/inventory-dialog.html',
                        controller: 'InventoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Inventory', function(Inventory) {
                                return Inventory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('inventory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('inventory.delete', {
                parent: 'inventory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/inventory/inventory-delete-dialog.html',
                        controller: 'InventoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Inventory', function(Inventory) {
                                return Inventory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('inventory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
