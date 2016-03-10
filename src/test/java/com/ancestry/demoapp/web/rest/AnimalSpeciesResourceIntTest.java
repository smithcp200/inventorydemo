package com.ancestry.demoapp.web.rest;

import com.ancestry.demoapp.Application;
import com.ancestry.demoapp.domain.AnimalSpecies;
import com.ancestry.demoapp.repository.AnimalSpeciesRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AnimalSpeciesResource REST controller.
 *
 * @see AnimalSpeciesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AnimalSpeciesResourceIntTest {

    private static final String DEFAULT_ANIMAL_SPECIES_NAME = "AAAAA";
    private static final String UPDATED_ANIMAL_SPECIES_NAME = "BBBBB";

    @Inject
    private AnimalSpeciesRepository animalSpeciesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAnimalSpeciesMockMvc;

    private AnimalSpecies animalSpecies;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnimalSpeciesResource animalSpeciesResource = new AnimalSpeciesResource();
        ReflectionTestUtils.setField(animalSpeciesResource, "animalSpeciesRepository", animalSpeciesRepository);
        this.restAnimalSpeciesMockMvc = MockMvcBuilders.standaloneSetup(animalSpeciesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        animalSpecies = new AnimalSpecies();
        animalSpecies.setAnimalSpeciesName(DEFAULT_ANIMAL_SPECIES_NAME);
    }

    @Test
    @Transactional
    public void createAnimalSpecies() throws Exception {
        int databaseSizeBeforeCreate = animalSpeciesRepository.findAll().size();

        // Create the AnimalSpecies

        restAnimalSpeciesMockMvc.perform(post("/api/animalSpeciess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(animalSpecies)))
                .andExpect(status().isCreated());

        // Validate the AnimalSpecies in the database
        List<AnimalSpecies> animalSpeciess = animalSpeciesRepository.findAll();
        assertThat(animalSpeciess).hasSize(databaseSizeBeforeCreate + 1);
        AnimalSpecies testAnimalSpecies = animalSpeciess.get(animalSpeciess.size() - 1);
        assertThat(testAnimalSpecies.getAnimalSpeciesName()).isEqualTo(DEFAULT_ANIMAL_SPECIES_NAME);
    }

    @Test
    @Transactional
    public void checkAnimalSpeciesNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalSpeciesRepository.findAll().size();
        // set the field null
        animalSpecies.setAnimalSpeciesName(null);

        // Create the AnimalSpecies, which fails.

        restAnimalSpeciesMockMvc.perform(post("/api/animalSpeciess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(animalSpecies)))
                .andExpect(status().isBadRequest());

        List<AnimalSpecies> animalSpeciess = animalSpeciesRepository.findAll();
        assertThat(animalSpeciess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnimalSpeciess() throws Exception {
        // Initialize the database
        animalSpeciesRepository.saveAndFlush(animalSpecies);

        // Get all the animalSpeciess
        restAnimalSpeciesMockMvc.perform(get("/api/animalSpeciess?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(animalSpecies.getId().intValue())))
                .andExpect(jsonPath("$.[*].animalSpeciesName").value(hasItem(DEFAULT_ANIMAL_SPECIES_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAnimalSpecies() throws Exception {
        // Initialize the database
        animalSpeciesRepository.saveAndFlush(animalSpecies);

        // Get the animalSpecies
        restAnimalSpeciesMockMvc.perform(get("/api/animalSpeciess/{id}", animalSpecies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(animalSpecies.getId().intValue()))
            .andExpect(jsonPath("$.animalSpeciesName").value(DEFAULT_ANIMAL_SPECIES_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnimalSpecies() throws Exception {
        // Get the animalSpecies
        restAnimalSpeciesMockMvc.perform(get("/api/animalSpeciess/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimalSpecies() throws Exception {
        // Initialize the database
        animalSpeciesRepository.saveAndFlush(animalSpecies);

		int databaseSizeBeforeUpdate = animalSpeciesRepository.findAll().size();

        // Update the animalSpecies
        animalSpecies.setAnimalSpeciesName(UPDATED_ANIMAL_SPECIES_NAME);

        restAnimalSpeciesMockMvc.perform(put("/api/animalSpeciess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(animalSpecies)))
                .andExpect(status().isOk());

        // Validate the AnimalSpecies in the database
        List<AnimalSpecies> animalSpeciess = animalSpeciesRepository.findAll();
        assertThat(animalSpeciess).hasSize(databaseSizeBeforeUpdate);
        AnimalSpecies testAnimalSpecies = animalSpeciess.get(animalSpeciess.size() - 1);
        assertThat(testAnimalSpecies.getAnimalSpeciesName()).isEqualTo(UPDATED_ANIMAL_SPECIES_NAME);
    }

    @Test
    @Transactional
    public void deleteAnimalSpecies() throws Exception {
        // Initialize the database
        animalSpeciesRepository.saveAndFlush(animalSpecies);

		int databaseSizeBeforeDelete = animalSpeciesRepository.findAll().size();

        // Get the animalSpecies
        restAnimalSpeciesMockMvc.perform(delete("/api/animalSpeciess/{id}", animalSpecies.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AnimalSpecies> animalSpeciess = animalSpeciesRepository.findAll();
        assertThat(animalSpeciess).hasSize(databaseSizeBeforeDelete - 1);
    }
}
