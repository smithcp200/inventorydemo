package com.ancestry.demoapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ancestry.demoapp.domain.Zoo;
import com.ancestry.demoapp.repository.ZooRepository;
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
 * REST controller for managing Zoo.
 */
@RestController
@RequestMapping("/api")
public class ZooResource {

    private final Logger log = LoggerFactory.getLogger(ZooResource.class);
        
    @Inject
    private ZooRepository zooRepository;
    
    /**
     * POST  /zoos -> Create a new zoo.
     */
    @RequestMapping(value = "/zoos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Zoo> createZoo(@Valid @RequestBody Zoo zoo) throws URISyntaxException {
        log.debug("REST request to save Zoo : {}", zoo);
        if (zoo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("zoo", "idexists", "A new zoo cannot already have an ID")).body(null);
        }
        Zoo result = zooRepository.save(zoo);
        return ResponseEntity.created(new URI("/api/zoos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("zoo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /zoos -> Updates an existing zoo.
     */
    @RequestMapping(value = "/zoos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Zoo> updateZoo(@Valid @RequestBody Zoo zoo) throws URISyntaxException {
        log.debug("REST request to update Zoo : {}", zoo);
        if (zoo.getId() == null) {
            return createZoo(zoo);
        }
        Zoo result = zooRepository.save(zoo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("zoo", zoo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /zoos -> get all the zoos.
     */
    @RequestMapping(value = "/zoos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Zoo> getAllZoos() {
        log.debug("REST request to get all Zoos");
        return zooRepository.findAll();
            }

    /**
     * GET  /zoos/:id -> get the "id" zoo.
     */
    @RequestMapping(value = "/zoos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Zoo> getZoo(@PathVariable Long id) {
        log.debug("REST request to get Zoo : {}", id);
        Zoo zoo = zooRepository.findOne(id);
        return Optional.ofNullable(zoo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /zoos/:id -> delete the "id" zoo.
     */
    @RequestMapping(value = "/zoos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteZoo(@PathVariable Long id) {
        log.debug("REST request to delete Zoo : {}", id);
        zooRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("zoo", id.toString())).build();
    }
}
