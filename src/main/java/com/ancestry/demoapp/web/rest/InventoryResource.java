package com.ancestry.demoapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ancestry.demoapp.domain.Inventory;
import com.ancestry.demoapp.repository.InventoryRepository;
import com.ancestry.demoapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Inventory.
 */
@RestController
@RequestMapping("/api")
public class InventoryResource {

    private final Logger log = LoggerFactory.getLogger(InventoryResource.class);
        
    @Inject
    private InventoryRepository inventoryRepository;
    
    /**
     * POST  /inventorys -> Create a new inventory.
     */
    @RequestMapping(value = "/inventorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody Inventory inventory) throws URISyntaxException {
        log.debug("REST request to save Inventory : {}", inventory);
        if (inventory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inventory", "idexists", "A new inventory cannot already have an ID")).body(null);
        }
        Inventory result = inventoryRepository.save(inventory);
        return ResponseEntity.created(new URI("/api/inventorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inventory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inventorys -> Updates an existing inventory.
     */
    @RequestMapping(value = "/inventorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inventory> updateInventory(@Valid @RequestBody Inventory inventory) throws URISyntaxException {
        log.debug("REST request to update Inventory : {}", inventory);
        if (inventory.getId() == null) {
            return createInventory(inventory);
        }
        Inventory result = inventoryRepository.save(inventory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inventory", inventory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inventorys -> get all the inventorys.
     */
    @RequestMapping(value = "/inventorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Inventory> getAllInventorys() {
        log.debug("REST request to get all Inventorys");
        return inventoryRepository.findAll();
            }

    /**
     * GET  /inventorys/:id -> get the "id" inventory.
     */
    @RequestMapping(value = "/inventorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inventory> getInventory(@PathVariable Long id) {
        log.debug("REST request to get Inventory : {}", id);
        Inventory inventory = inventoryRepository.findOne(id);
        return Optional.ofNullable(inventory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /inventorys/:id -> delete the "id" inventory.
     */
    @RequestMapping(value = "/inventorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        log.debug("REST request to delete Inventory : {}", id);
        inventoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inventory", id.toString())).build();
    }
}
