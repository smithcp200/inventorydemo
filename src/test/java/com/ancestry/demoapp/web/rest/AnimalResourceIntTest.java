package com.ancestry.demoapp.web.rest;

import com.ancestry.demoapp.Application;
import com.ancestry.demoapp.domain.Animal;
import com.ancestry.demoapp.repository.AnimalRepository;

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
 * Test class for the AnimalResource REST controller.
 *
 * @see AnimalResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AnimalResourceIntTest {

    private static final String DEFAULT_ANIMAL_NAME = "AAAAA";
    private static final String UPDATED_ANIMAL_NAME = "BBBBB";

    @Inject
    private AnimalRepository animalRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAnimalMockMvc;

    private Animal animal;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnimalResource animalResource = new AnimalResource();
        ReflectionTestUtils.setField(animalResource, "animalRepository", animalRepository);
        this.restAnimalMockMvc = MockMvcBuilders.standaloneSetup(animalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        animal = new Animal();
        animal.setAnimalName(DEFAULT_ANIMAL_NAME);
    }

    @Test
    @Transactional
    public void createAnimal() throws Exception {
        int databaseSizeBeforeCreate = animalRepository.findAll().size();

        // Create the Animal

        restAnimalMockMvc.perform(post("/api/animals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(animal)))
                .andExpect(status().isCreated());

        // Validate the Animal in the database
        List<Animal> animals = animalRepository.findAll();
        assertThat(animals).hasSize(databaseSizeBeforeCreate + 1);
        Animal testAnimal = animals.get(animals.size() - 1);
        assertThat(testAnimal.getAnimalName()).isEqualTo(DEFAULT_ANIMAL_NAME);
    }

    @Test
    @Transactional
    public void checkAnimalNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalRepository.findAll().size();
        // set the field null
        animal.setAnimalName(null);

        // Create the Animal, which fails.

        restAnimalMockMvc.perform(post("/api/animals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(animal)))
                .andExpect(status().isBadRequest());

        List<Animal> animals = animalRepository.findAll();
        assertThat(animals).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnimals() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animals
        restAnimalMockMvc.perform(get("/api/animals?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(animal.getId().intValue())))
                .andExpect(jsonPath("$.[*].animalName").value(hasItem(DEFAULT_ANIMAL_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAnimal() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get the animal
        restAnimalMockMvc.perform(get("/api/animals/{id}", animal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(animal.getId().intValue()))
            .andExpect(jsonPath("$.animalName").value(DEFAULT_ANIMAL_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnimal() throws Exception {
        // Get the animal
        restAnimalMockMvc.perform(get("/api/animals/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimal() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

		int databaseSizeBeforeUpdate = animalRepository.findAll().size();

        // Update the animal
        animal.setAnimalName(UPDATED_ANIMAL_NAME);

        restAnimalMockMvc.perform(put("/api/animals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(animal)))
                .andExpect(status().isOk());

        // Validate the Animal in the database
        List<Animal> animals = animalRepository.findAll();
        assertThat(animals).hasSize(databaseSizeBeforeUpdate);
        Animal testAnimal = animals.get(animals.size() - 1);
        assertThat(testAnimal.getAnimalName()).isEqualTo(UPDATED_ANIMAL_NAME);
    }

    @Test
    @Transactional
    public void deleteAnimal() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

		int databaseSizeBeforeDelete = animalRepository.findAll().size();

        // Get the animal
        restAnimalMockMvc.perform(delete("/api/animals/{id}", animal.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Animal> animals = animalRepository.findAll();
        assertThat(animals).hasSize(databaseSizeBeforeDelete - 1);
    }
}
