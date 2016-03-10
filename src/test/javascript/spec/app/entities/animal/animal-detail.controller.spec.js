'use strict';

describe('Controller Tests', function() {

    describe('Animal Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAnimal, MockAnimalSpecies, MockZoo, MockFeeding;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAnimal = jasmine.createSpy('MockAnimal');
            MockAnimalSpecies = jasmine.createSpy('MockAnimalSpecies');
            MockZoo = jasmine.createSpy('MockZoo');
            MockFeeding = jasmine.createSpy('MockFeeding');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Animal': MockAnimal,
                'AnimalSpecies': MockAnimalSpecies,
                'Zoo': MockZoo,
                'Feeding': MockFeeding
            };
            createController = function() {
                $injector.get('$controller')("AnimalDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ancestryApp:animalUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
