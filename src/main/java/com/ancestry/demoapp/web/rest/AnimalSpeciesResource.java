package com.ancestry.demoapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ancestry.demoapp.domain.AnimalSpecies;
import com.ancestry.demoapp.repository.AnimalSpeciesRepository;
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
 * REST controller for managing AnimalSpecies.
 */
@RestController
@RequestMapping("/api")
public class AnimalSpeciesResource {

    private final Logger log = LoggerFactory.getLogger(AnimalSpeciesResource.class);
        
    @Inject
    private AnimalSpeciesRepository animalSpeciesRepository;
    
    /**
     * POST  /animalSpeciess -> Create a new animalSpecies.
     */
    @RequestMapping(value = "/animalSpeciess",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AnimalSpecies> createAnimalSpecies(@Valid @RequestBody AnimalSpecies animalSpecies) throws URISyntaxException {
        log.debug("REST request to save AnimalSpecies : {}", animalSpecies);
        if (animalSpecies.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("animalSpecies", "idexists", "A new animalSpecies cannot already have an ID")).body(null);
        }
        AnimalSpecies result = animalSpeciesRepository.save(animalSpecies);
        return ResponseEntity.created(new URI("/api/animalSpeciess/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("animalSpecies", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /animalSpeciess -> Updates an existing animalSpecies.
     */
    @RequestMapping(value = "/animalSpeciess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AnimalSpecies> updateAnimalSpecies(@Valid @RequestBody AnimalSpecies animalSpecies) throws URISyntaxException {
        log.debug("REST request to update AnimalSpecies : {}", animalSpecies);
        if (animalSpecies.getId() == null) {
            return createAnimalSpecies(animalSpecies);
        }
        AnimalSpecies result = animalSpeciesRepository.save(animalSpecies);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("animalSpecies", animalSpecies.getId().toString()))
            .body(result);
    }

    /**
     * GET  /animalSpeciess -> get all the animalSpeciess.
     */
    @RequestMapping(value = "/animalSpeciess",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AnimalSpecies> getAllAnimalSpeciess() {
        log.debug("REST request to get all AnimalSpeciess");
        return animalSpeciesRepository.findAll();
            }

    /**
     * GET  /animalSpeciess/:id -> get the "id" animalSpecies.
     */
    @RequestMapping(value = "/animalSpeciess/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AnimalSpecies> getAnimalSpecies(@PathVariable Long id) {
        log.debug("REST request to get AnimalSpecies : {}", id);
        AnimalSpecies animalSpecies = animalSpeciesRepository.findOne(id);
        return Optional.ofNullable(animalSpecies)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /animalSpeciess/:id -> delete the "id" animalSpecies.
     */
    @RequestMapping(value = "/animalSpeciess/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAnimalSpecies(@PathVariable Long id) {
        log.debug("REST request to delete AnimalSpecies : {}", id);
        animalSpeciesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("animalSpecies", id.toString())).build();
    }
}
