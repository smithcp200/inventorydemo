package com.ancestry.demoapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ancestry.demoapp.domain.Animal;
import com.ancestry.demoapp.repository.AnimalRepository;
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
 * REST controller for managing Animal.
 */
@RestController
@RequestMapping("/api")
public class AnimalResource {

    private final Logger log = LoggerFactory.getLogger(AnimalResource.class);
        
    @Inject
    private AnimalRepository animalRepository;
    
    /**
     * POST  /animals -> Create a new animal.
     */
    @RequestMapping(value = "/animals",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Animal> createAnimal(@Valid @RequestBody Animal animal) throws URISyntaxException {
        log.debug("REST request to save Animal : {}", animal);
        if (animal.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("animal", "idexists", "A new animal cannot already have an ID")).body(null);
        }
        Animal result = animalRepository.save(animal);
        return ResponseEntity.created(new URI("/api/animals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("animal", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /animals -> Updates an existing animal.
     */
    @RequestMapping(value = "/animals",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Animal> updateAnimal(@Valid @RequestBody Animal animal) throws URISyntaxException {
        log.debug("REST request to update Animal : {}", animal);
        if (animal.getId() == null) {
            return createAnimal(animal);
        }
        Animal result = animalRepository.save(animal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("animal", animal.getId().toString()))
            .body(result);
    }

    /**
     * GET  /animals -> get all the animals.
     */
    @RequestMapping(value = "/animals",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Animal> getAllAnimals() {
        log.debug("REST request to get all Animals");
        return animalRepository.findAll();
            }

    /**
     * GET  /animals/:id -> get the "id" animal.
     */
    @RequestMapping(value = "/animals/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Animal> getAnimal(@PathVariable Long id) {
        log.debug("REST request to get Animal : {}", id);
        Animal animal = animalRepository.findOne(id);
        return Optional.ofNullable(animal)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /animals/:id -> delete the "id" animal.
     */
    @RequestMapping(value = "/animals/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        log.debug("REST request to delete Animal : {}", id);
        animalRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("animal", id.toString())).build();
    }
}
