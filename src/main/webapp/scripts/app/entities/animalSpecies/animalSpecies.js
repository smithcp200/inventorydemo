'use strict';

angular.module('ancestryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('animalSpecies', {
                parent: 'entity',
                url: '/animalSpeciess',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ancestryApp.animalSpecies.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/animalSpecies/animalSpeciess.html',
                        controller: 'AnimalSpeciesController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('animalSpecies');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('animalSpecies.detail', {
                parent: 'entity',
                url: '/animalSpecies/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ancestryApp.animalSpecies.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/animalSpecies/animalSpecies-detail.html',
                        controller: 'AnimalSpeciesDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('animalSpecies');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AnimalSpecies', function($stateParams, AnimalSpecies) {
                        return AnimalSpecies.get({id : $stateParams.id});
                    }]
                }
            })
            .state('animalSpecies.new', {
                parent: 'animalSpecies',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/animalSpecies/animalSpecies-dialog.html',
                        controller: 'AnimalSpeciesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    animalSpeciesName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('animalSpecies', null, { reload: true });
                    }, function() {
                        $state.go('animalSpecies');
                    })
                }]
            })
            .state('animalSpecies.edit', {
                parent: 'animalSpecies',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/animalSpecies/animalSpecies-dialog.html',
                        controller: 'AnimalSpeciesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AnimalSpecies', function(AnimalSpecies) {
                                return AnimalSpecies.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('animalSpecies', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('animalSpecies.delete', {
                parent: 'animalSpecies',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/animalSpecies/animalSpecies-delete-dialog.html',
                        controller: 'AnimalSpeciesDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['AnimalSpecies', function(AnimalSpecies) {
                                return AnimalSpecies.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('animalSpecies', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
