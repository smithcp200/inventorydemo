package com.ancestry.demoapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ancestry.demoapp.domain.Feeding;
import com.ancestry.demoapp.repository.FeedingRepository;
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
 * REST controller for managing Feeding.
 */
@RestController
@RequestMapping("/api")
public class FeedingResource {

    private final Logger log = LoggerFactory.getLogger(FeedingResource.class);
        
    @Inject
    private FeedingRepository feedingRepository;
    
    /**
     * POST  /feedings -> Create a new feeding.
     */
    @RequestMapping(value = "/feedings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Feeding> createFeeding(@Valid @RequestBody Feeding feeding) throws URISyntaxException {
        log.debug("REST request to save Feeding : {}", feeding);
        if (feeding.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("feeding", "idexists", "A new feeding cannot already have an ID")).body(null);
        }
        Feeding result = feedingRepository.save(feeding);
        return ResponseEntity.created(new URI("/api/feedings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("feeding", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /feedings -> Updates an existing feeding.
     */
    @RequestMapping(value = "/feedings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Feeding> updateFeeding(@Valid @RequestBody Feeding feeding) throws URISyntaxException {
        log.debug("REST request to update Feeding : {}", feeding);
        if (feeding.getId() == null) {
            return createFeeding(feeding);
        }
        Feeding result = feedingRepository.save(feeding);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("feeding", feeding.getId().toString()))
            .body(result);
    }

    /**
     * GET  /feedings -> get all the feedings.
     */
    @RequestMapping(value = "/feedings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Feeding> getAllFeedings() {
        log.debug("REST request to get all Feedings");
        return feedingRepository.findAll();
            }

    /**
     * GET  /feedings/:id -> get the "id" feeding.
     */
    @RequestMapping(value = "/feedings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Feeding> getFeeding(@PathVariable Long id) {
        log.debug("REST request to get Feeding : {}", id);
        Feeding feeding = feedingRepository.findOne(id);
        return Optional.ofNullable(feeding)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /feedings/:id -> delete the "id" feeding.
     */
    @RequestMapping(value = "/feedings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFeeding(@PathVariable Long id) {
        log.debug("REST request to delete Feeding : {}", id);
        feedingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("feeding", id.toString())).build();
    }
}
