'use strict';

describe('Controller Tests', function() {

    describe('Inventory Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInventory, MockZoo, MockFeeding;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInventory = jasmine.createSpy('MockInventory');
            MockZoo = jasmine.createSpy('MockZoo');
            MockFeeding = jasmine.createSpy('MockFeeding');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Inventory': MockInventory,
                'Zoo': MockZoo,
                'Feeding': MockFeeding
            };
            createController = function() {
                $injector.get('$controller')("InventoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ancestryApp:inventoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
