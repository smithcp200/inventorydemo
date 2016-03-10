'use strict';

angular.module('ancestryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('animal', {
                parent: 'entity',
                url: '/animals',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ancestryApp.animal.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/animal/animals.html',
                        controller: 'AnimalController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('animal');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('animal.detail', {
                parent: 'entity',
                url: '/animal/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ancestryApp.animal.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/animal/animal-detail.html',
                        controller: 'AnimalDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('animal');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Animal', function($stateParams, Animal) {
                        return Animal.get({id : $stateParams.id});
                    }]
                }
            })
            .state('animal.new', {
                parent: 'animal',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/animal/animal-dialog.html',
                        controller: 'AnimalDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    animalName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('animal', null, { reload: true });
                    }, function() {
                        $state.go('animal');
                    })
                }]
            })
            .state('animal.edit', {
                parent: 'animal',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/animal/animal-dialog.html',
                        controller: 'AnimalDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Animal', function(Animal) {
                                return Animal.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('animal', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('animal.delete', {
                parent: 'animal',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/animal/animal-delete-dialog.html',
                        controller: 'AnimalDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Animal', function(Animal) {
                                return Animal.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('animal', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
