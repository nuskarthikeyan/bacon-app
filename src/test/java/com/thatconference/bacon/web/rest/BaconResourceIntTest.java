package com.thatconference.bacon.web.rest;

import com.thatconference.bacon.BaconApp;

import com.thatconference.bacon.domain.Bacon;
import com.thatconference.bacon.repository.BaconRepository;
import com.thatconference.bacon.service.BaconService;
import com.thatconference.bacon.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BaconResource REST controller.
 *
 * @see BaconResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BaconApp.class)
public class BaconResourceIntTest {

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    @Autowired
    private BaconRepository baconRepository;

    @Autowired
    private BaconService baconService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBaconMockMvc;

    private Bacon bacon;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BaconResource baconResource = new BaconResource(baconService);
        this.restBaconMockMvc = MockMvcBuilders.standaloneSetup(baconResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bacon createEntity(EntityManager em) {
        Bacon bacon = new Bacon()
            .brand(DEFAULT_BRAND);
        return bacon;
    }

    @Before
    public void initTest() {
        bacon = createEntity(em);
    }

    @Test
    @Transactional
    public void createBacon() throws Exception {
        int databaseSizeBeforeCreate = baconRepository.findAll().size();

        // Create the Bacon
        restBaconMockMvc.perform(post("/api/bacons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bacon)))
            .andExpect(status().isCreated());

        // Validate the Bacon in the database
        List<Bacon> baconList = baconRepository.findAll();
        assertThat(baconList).hasSize(databaseSizeBeforeCreate + 1);
        Bacon testBacon = baconList.get(baconList.size() - 1);
        assertThat(testBacon.getBrand()).isEqualTo(DEFAULT_BRAND);
    }

    @Test
    @Transactional
    public void createBaconWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = baconRepository.findAll().size();

        // Create the Bacon with an existing ID
        bacon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaconMockMvc.perform(post("/api/bacons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bacon)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Bacon> baconList = baconRepository.findAll();
        assertThat(baconList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBrandIsRequired() throws Exception {
        int databaseSizeBeforeTest = baconRepository.findAll().size();
        // set the field null
        bacon.setBrand(null);

        // Create the Bacon, which fails.

        restBaconMockMvc.perform(post("/api/bacons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bacon)))
            .andExpect(status().isBadRequest());

        List<Bacon> baconList = baconRepository.findAll();
        assertThat(baconList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBacons() throws Exception {
        // Initialize the database
        baconRepository.saveAndFlush(bacon);

        // Get all the baconList
        restBaconMockMvc.perform(get("/api/bacons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bacon.getId().intValue())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())));
    }

    @Test
    @Transactional
    public void getBacon() throws Exception {
        // Initialize the database
        baconRepository.saveAndFlush(bacon);

        // Get the bacon
        restBaconMockMvc.perform(get("/api/bacons/{id}", bacon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bacon.getId().intValue()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBacon() throws Exception {
        // Get the bacon
        restBaconMockMvc.perform(get("/api/bacons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBacon() throws Exception {
        // Initialize the database
        baconService.save(bacon);

        int databaseSizeBeforeUpdate = baconRepository.findAll().size();

        // Update the bacon
        Bacon updatedBacon = baconRepository.findOne(bacon.getId());
        updatedBacon
            .brand(UPDATED_BRAND);

        restBaconMockMvc.perform(put("/api/bacons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBacon)))
            .andExpect(status().isOk());

        // Validate the Bacon in the database
        List<Bacon> baconList = baconRepository.findAll();
        assertThat(baconList).hasSize(databaseSizeBeforeUpdate);
        Bacon testBacon = baconList.get(baconList.size() - 1);
        assertThat(testBacon.getBrand()).isEqualTo(UPDATED_BRAND);
    }

    @Test
    @Transactional
    public void updateNonExistingBacon() throws Exception {
        int databaseSizeBeforeUpdate = baconRepository.findAll().size();

        // Create the Bacon

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBaconMockMvc.perform(put("/api/bacons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bacon)))
            .andExpect(status().isCreated());

        // Validate the Bacon in the database
        List<Bacon> baconList = baconRepository.findAll();
        assertThat(baconList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBacon() throws Exception {
        // Initialize the database
        baconService.save(bacon);

        int databaseSizeBeforeDelete = baconRepository.findAll().size();

        // Get the bacon
        restBaconMockMvc.perform(delete("/api/bacons/{id}", bacon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Bacon> baconList = baconRepository.findAll();
        assertThat(baconList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bacon.class);
        Bacon bacon1 = new Bacon();
        bacon1.setId(1L);
        Bacon bacon2 = new Bacon();
        bacon2.setId(bacon1.getId());
        assertThat(bacon1).isEqualTo(bacon2);
        bacon2.setId(2L);
        assertThat(bacon1).isNotEqualTo(bacon2);
        bacon1.setId(null);
        assertThat(bacon1).isNotEqualTo(bacon2);
    }
}
