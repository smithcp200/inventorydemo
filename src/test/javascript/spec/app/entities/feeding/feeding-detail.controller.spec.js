'use strict';

describe('Controller Tests', function() {

    describe('Feeding Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFeeding, MockAnimal, MockInventory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFeeding = jasmine.createSpy('MockFeeding');
            MockAnimal = jasmine.createSpy('MockAnimal');
            MockInventory = jasmine.createSpy('MockInventory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Feeding': MockFeeding,
                'Animal': MockAnimal,
                'Inventory': MockInventory
            };
            createController = function() {
                $injector.get('$controller')("FeedingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ancestryApp:feedingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
