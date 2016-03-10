package com.ancestry.demoapp.web.rest;

import com.ancestry.demoapp.Application;
import com.ancestry.demoapp.domain.Feeding;
import com.ancestry.demoapp.repository.FeedingRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FeedingResource REST controller.
 *
 * @see FeedingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FeedingResourceIntTest {


    private static final LocalDate DEFAULT_FEEDING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FEEDING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Inject
    private FeedingRepository feedingRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFeedingMockMvc;

    private Feeding feeding;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FeedingResource feedingResource = new FeedingResource();
        ReflectionTestUtils.setField(feedingResource, "feedingRepository", feedingRepository);
        this.restFeedingMockMvc = MockMvcBuilders.standaloneSetup(feedingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        feeding = new Feeding();
        feeding.setFeedingDate(DEFAULT_FEEDING_DATE);
        feeding.setQuantity(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createFeeding() throws Exception {
        int databaseSizeBeforeCreate = feedingRepository.findAll().size();

        // Create the Feeding

        restFeedingMockMvc.perform(post("/api/feedings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feeding)))
                .andExpect(status().isCreated());

        // Validate the Feeding in the database
        List<Feeding> feedings = feedingRepository.findAll();
        assertThat(feedings).hasSize(databaseSizeBeforeCreate + 1);
        Feeding testFeeding = feedings.get(feedings.size() - 1);
        assertThat(testFeeding.getFeedingDate()).isEqualTo(DEFAULT_FEEDING_DATE);
        assertThat(testFeeding.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void checkFeedingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedingRepository.findAll().size();
        // set the field null
        feeding.setFeedingDate(null);

        // Create the Feeding, which fails.

        restFeedingMockMvc.perform(post("/api/feedings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feeding)))
                .andExpect(status().isBadRequest());

        List<Feeding> feedings = feedingRepository.findAll();
        assertThat(feedings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedingRepository.findAll().size();
        // set the field null
        feeding.setQuantity(null);

        // Create the Feeding, which fails.

        restFeedingMockMvc.perform(post("/api/feedings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feeding)))
                .andExpect(status().isBadRequest());

        List<Feeding> feedings = feedingRepository.findAll();
        assertThat(feedings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFeedings() throws Exception {
        // Initialize the database
        feedingRepository.saveAndFlush(feeding);

        // Get all the feedings
        restFeedingMockMvc.perform(get("/api/feedings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(feeding.getId().intValue())))
                .andExpect(jsonPath("$.[*].feedingDate").value(hasItem(DEFAULT_FEEDING_DATE.toString())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getFeeding() throws Exception {
        // Initialize the database
        feedingRepository.saveAndFlush(feeding);

        // Get the feeding
        restFeedingMockMvc.perform(get("/api/feedings/{id}", feeding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(feeding.getId().intValue()))
            .andExpect(jsonPath("$.feedingDate").value(DEFAULT_FEEDING_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingFeeding() throws Exception {
        // Get the feeding
        restFeedingMockMvc.perform(get("/api/feedings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeeding() throws Exception {
        // Initialize the database
        feedingRepository.saveAndFlush(feeding);

		int databaseSizeBeforeUpdate = feedingRepository.findAll().size();

        // Update the feeding
        feeding.setFeedingDate(UPDATED_FEEDING_DATE);
        feeding.setQuantity(UPDATED_QUANTITY);

        restFeedingMockMvc.perform(put("/api/feedings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feeding)))
                .andExpect(status().isOk());

        // Validate the Feeding in the database
        List<Feeding> feedings = feedingRepository.findAll();
        assertThat(feedings).hasSize(databaseSizeBeforeUpdate);
        Feeding testFeeding = feedings.get(feedings.size() - 1);
        assertThat(testFeeding.getFeedingDate()).isEqualTo(UPDATED_FEEDING_DATE);
        assertThat(testFeeding.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void deleteFeeding() throws Exception {
        // Initialize the database
        feedingRepository.saveAndFlush(feeding);

		int databaseSizeBeforeDelete = feedingRepository.findAll().size();

        // Get the feeding
        restFeedingMockMvc.perform(delete("/api/feedings/{id}", feeding.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Feeding> feedings = feedingRepository.findAll();
        assertThat(feedings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
