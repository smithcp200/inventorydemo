package com.ancestry.demoapp.web.rest;

import com.ancestry.demoapp.Application;
import com.ancestry.demoapp.domain.Zoo;
import com.ancestry.demoapp.repository.ZooRepository;

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
 * Test class for the ZooResource REST controller.
 *
 * @see ZooResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ZooResourceIntTest {

    private static final String DEFAULT_ZOO_NAME = "AAAAA";
    private static final String UPDATED_ZOO_NAME = "BBBBB";

    @Inject
    private ZooRepository zooRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restZooMockMvc;

    private Zoo zoo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ZooResource zooResource = new ZooResource();
        ReflectionTestUtils.setField(zooResource, "zooRepository", zooRepository);
        this.restZooMockMvc = MockMvcBuilders.standaloneSetup(zooResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        zoo = new Zoo();
        zoo.setZooName(DEFAULT_ZOO_NAME);
    }

    @Test
    @Transactional
    public void createZoo() throws Exception {
        int databaseSizeBeforeCreate = zooRepository.findAll().size();

        // Create the Zoo

        restZooMockMvc.perform(post("/api/zoos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(zoo)))
                .andExpect(status().isCreated());

        // Validate the Zoo in the database
        List<Zoo> zoos = zooRepository.findAll();
        assertThat(zoos).hasSize(databaseSizeBeforeCreate + 1);
        Zoo testZoo = zoos.get(zoos.size() - 1);
        assertThat(testZoo.getZooName()).isEqualTo(DEFAULT_ZOO_NAME);
    }

    @Test
    @Transactional
    public void checkZooNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = zooRepository.findAll().size();
        // set the field null
        zoo.setZooName(null);

        // Create the Zoo, which fails.

        restZooMockMvc.perform(post("/api/zoos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(zoo)))
                .andExpect(status().isBadRequest());

        List<Zoo> zoos = zooRepository.findAll();
        assertThat(zoos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllZoos() throws Exception {
        // Initialize the database
        zooRepository.saveAndFlush(zoo);

        // Get all the zoos
        restZooMockMvc.perform(get("/api/zoos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(zoo.getId().intValue())))
                .andExpect(jsonPath("$.[*].zooName").value(hasItem(DEFAULT_ZOO_NAME.toString())));
    }

    @Test
    @Transactional
    public void getZoo() throws Exception {
        // Initialize the database
        zooRepository.saveAndFlush(zoo);

        // Get the zoo
        restZooMockMvc.perform(get("/api/zoos/{id}", zoo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(zoo.getId().intValue()))
            .andExpect(jsonPath("$.zooName").value(DEFAULT_ZOO_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingZoo() throws Exception {
        // Get the zoo
        restZooMockMvc.perform(get("/api/zoos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateZoo() throws Exception {
        // Initialize the database
        zooRepository.saveAndFlush(zoo);

		int databaseSizeBeforeUpdate = zooRepository.findAll().size();

        // Update the zoo
        zoo.setZooName(UPDATED_ZOO_NAME);

        restZooMockMvc.perform(put("/api/zoos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(zoo)))
                .andExpect(status().isOk());

        // Validate the Zoo in the database
        List<Zoo> zoos = zooRepository.findAll();
        assertThat(zoos).hasSize(databaseSizeBeforeUpdate);
        Zoo testZoo = zoos.get(zoos.size() - 1);
        assertThat(testZoo.getZooName()).isEqualTo(UPDATED_ZOO_NAME);
    }

    @Test
    @Transactional
    public void deleteZoo() throws Exception {
        // Initialize the database
        zooRepository.saveAndFlush(zoo);

		int databaseSizeBeforeDelete = zooRepository.findAll().size();

        // Get the zoo
        restZooMockMvc.perform(delete("/api/zoos/{id}", zoo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Zoo> zoos = zooRepository.findAll();
        assertThat(zoos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
