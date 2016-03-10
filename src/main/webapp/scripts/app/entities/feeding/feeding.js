'use strict';

angular.module('ancestryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('feeding', {
                parent: 'entity',
                url: '/feedings',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ancestryApp.feeding.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/feeding/feedings.html',
                        controller: 'FeedingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('feeding');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('feeding.detail', {
                parent: 'entity',
                url: '/feeding/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ancestryApp.feeding.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/feeding/feeding-detail.html',
                        controller: 'FeedingDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('feeding');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Feeding', function($stateParams, Feeding) {
                        return Feeding.get({id : $stateParams.id});
                    }]
                }
            })
            .state('feeding.new', {
                parent: 'feeding',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/feeding/feeding-dialog.html',
                        controller: 'FeedingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    feedingDate: null,
                                    quantity: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('feeding', null, { reload: true });
                    }, function() {
                        $state.go('feeding');
                    })
                }]
            })
            .state('feeding.edit', {
                parent: 'feeding',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/feeding/feeding-dialog.html',
                        controller: 'FeedingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Feeding', function(Feeding) {
                                return Feeding.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('feeding', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('feeding.delete', {
                parent: 'feeding',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/feeding/feeding-delete-dialog.html',
                        controller: 'FeedingDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Feeding', function(Feeding) {
                                return Feeding.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('feeding', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
