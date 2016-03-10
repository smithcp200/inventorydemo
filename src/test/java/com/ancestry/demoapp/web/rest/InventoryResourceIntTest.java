package com.ancestry.demoapp.web.rest;

import com.ancestry.demoapp.Application;
import com.ancestry.demoapp.domain.Inventory;
import com.ancestry.demoapp.repository.InventoryRepository;

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
 * Test class for the InventoryResource REST controller.
 *
 * @see InventoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InventoryResourceIntTest {


    private static final LocalDate DEFAULT_INVENTORY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVENTORY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Inject
    private InventoryRepository inventoryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInventoryMockMvc;

    private Inventory inventory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InventoryResource inventoryResource = new InventoryResource();
        ReflectionTestUtils.setField(inventoryResource, "inventoryRepository", inventoryRepository);
        this.restInventoryMockMvc = MockMvcBuilders.standaloneSetup(inventoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        inventory = new Inventory();
        inventory.setInventoryDate(DEFAULT_INVENTORY_DATE);
        inventory.setQuantity(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createInventory() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // Create the Inventory

        restInventoryMockMvc.perform(post("/api/inventorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inventory)))
                .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventorys = inventoryRepository.findAll();
        assertThat(inventorys).hasSize(databaseSizeBeforeCreate + 1);
        Inventory testInventory = inventorys.get(inventorys.size() - 1);
        assertThat(testInventory.getInventoryDate()).isEqualTo(DEFAULT_INVENTORY_DATE);
        assertThat(testInventory.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void checkInventoryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setInventoryDate(null);

        // Create the Inventory, which fails.

        restInventoryMockMvc.perform(post("/api/inventorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inventory)))
                .andExpect(status().isBadRequest());

        List<Inventory> inventorys = inventoryRepository.findAll();
        assertThat(inventorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().size();
        // set the field null
        inventory.setQuantity(null);

        // Create the Inventory, which fails.

        restInventoryMockMvc.perform(post("/api/inventorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inventory)))
                .andExpect(status().isBadRequest());

        List<Inventory> inventorys = inventoryRepository.findAll();
        assertThat(inventorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInventorys() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventorys
        restInventoryMockMvc.perform(get("/api/inventorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())))
                .andExpect(jsonPath("$.[*].inventoryDate").value(hasItem(DEFAULT_INVENTORY_DATE.toString())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get the inventory
        restInventoryMockMvc.perform(get("/api/inventorys/{id}", inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(inventory.getId().intValue()))
            .andExpect(jsonPath("$.inventoryDate").value(DEFAULT_INVENTORY_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingInventory() throws Exception {
        // Get the inventory
        restInventoryMockMvc.perform(get("/api/inventorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

		int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory
        inventory.setInventoryDate(UPDATED_INVENTORY_DATE);
        inventory.setQuantity(UPDATED_QUANTITY);

        restInventoryMockMvc.perform(put("/api/inventorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inventory)))
                .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventorys = inventoryRepository.findAll();
        assertThat(inventorys).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventorys.get(inventorys.size() - 1);
        assertThat(testInventory.getInventoryDate()).isEqualTo(UPDATED_INVENTORY_DATE);
        assertThat(testInventory.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void deleteInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

		int databaseSizeBeforeDelete = inventoryRepository.findAll().size();

        // Get the inventory
        restInventoryMockMvc.perform(delete("/api/inventorys/{id}", inventory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Inventory> inventorys = inventoryRepository.findAll();
        assertThat(inventorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
