'use strict';

describe('Controller Tests', function() {

    describe('AnimalSpecies Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAnimalSpecies, MockAnimal;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAnimalSpecies = jasmine.createSpy('MockAnimalSpecies');
            MockAnimal = jasmine.createSpy('MockAnimal');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'AnimalSpecies': MockAnimalSpecies,
                'Animal': MockAnimal
            };
            createController = function() {
                $injector.get('$controller')("AnimalSpeciesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ancestryApp:animalSpeciesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
