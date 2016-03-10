'use strict';

angular.module('ancestryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('zoo', {
                parent: 'entity',
                url: '/zoos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ancestryApp.zoo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/zoo/zoos.html',
                        controller: 'ZooController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('zoo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('zoo.detail', {
                parent: 'entity',
                url: '/zoo/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ancestryApp.zoo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/zoo/zoo-detail.html',
                        controller: 'ZooDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('zoo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Zoo', function($stateParams, Zoo) {
                        return Zoo.get({id : $stateParams.id});
                    }]
                }
            })
            .state('zoo.new', {
                parent: 'zoo',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/zoo/zoo-dialog.html',
                        controller: 'ZooDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    zooName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('zoo', null, { reload: true });
                    }, function() {
                        $state.go('zoo');
                    })
                }]
            })
            .state('zoo.edit', {
                parent: 'zoo',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/zoo/zoo-dialog.html',
                        controller: 'ZooDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Zoo', function(Zoo) {
                                return Zoo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('zoo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('zoo.delete', {
                parent: 'zoo',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/zoo/zoo-delete-dialog.html',
                        controller: 'ZooDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Zoo', function(Zoo) {
                                return Zoo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('zoo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
