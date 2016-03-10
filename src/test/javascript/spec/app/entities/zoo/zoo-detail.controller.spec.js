'use strict';

describe('Controller Tests', function() {

    describe('Zoo Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockZoo, MockInventory, MockAnimal;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockZoo = jasmine.createSpy('MockZoo');
            MockInventory = jasmine.createSpy('MockInventory');
            MockAnimal = jasmine.createSpy('MockAnimal');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Zoo': MockZoo,
                'Inventory': MockInventory,
                'Animal': MockAnimal
            };
            createController = function() {
                $injector.get('$controller')("ZooDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ancestryApp:zooUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
