package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.Take;
import com.mycompany.myapp.repository.TakeRepository;
import com.mycompany.myapp.repository.search.TakeSearchRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

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

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TakeResource REST controller.
 *
 * @see TakeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class TakeResourceIntTest {

    private static final Double DEFAULT_GRADE = 1D;
    private static final Double UPDATED_GRADE = 2D;

    @Autowired
    private TakeRepository takeRepository;

    @Autowired
    private TakeSearchRepository takeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTakeMockMvc;

    private Take take;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TakeResource takeResource = new TakeResource(takeRepository, takeSearchRepository);
        this.restTakeMockMvc = MockMvcBuilders.standaloneSetup(takeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Take createEntity(EntityManager em) {
        Take take = new Take()
            .grade(DEFAULT_GRADE);
        return take;
    }

    @Before
    public void initTest() {
        takeSearchRepository.deleteAll();
        take = createEntity(em);
    }

    @Test
    @Transactional
    public void createTake() throws Exception {
        int databaseSizeBeforeCreate = takeRepository.findAll().size();

        // Create the Take
        restTakeMockMvc.perform(post("/api/takes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(take)))
            .andExpect(status().isCreated());

        // Validate the Take in the database
        List<Take> takeList = takeRepository.findAll();
        assertThat(takeList).hasSize(databaseSizeBeforeCreate + 1);
        Take testTake = takeList.get(takeList.size() - 1);
        assertThat(testTake.getGrade()).isEqualTo(DEFAULT_GRADE);

        // Validate the Take in Elasticsearch
        Take takeEs = takeSearchRepository.findOne(testTake.getId());
        assertThat(takeEs).isEqualToIgnoringGivenFields(testTake);
    }

    @Test
    @Transactional
    public void createTakeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = takeRepository.findAll().size();

        // Create the Take with an existing ID
        take.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTakeMockMvc.perform(post("/api/takes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(take)))
            .andExpect(status().isBadRequest());

        // Validate the Take in the database
        List<Take> takeList = takeRepository.findAll();
        assertThat(takeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTakes() throws Exception {
        // Initialize the database
        takeRepository.saveAndFlush(take);

        // Get all the takeList
        restTakeMockMvc.perform(get("/api/takes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(take.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.doubleValue())));
    }

    @Test
    @Transactional
    public void getTake() throws Exception {
        // Initialize the database
        takeRepository.saveAndFlush(take);

        // Get the take
        restTakeMockMvc.perform(get("/api/takes/{id}", take.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(take.getId().intValue()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTake() throws Exception {
        // Get the take
        restTakeMockMvc.perform(get("/api/takes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTake() throws Exception {
        // Initialize the database
        takeRepository.saveAndFlush(take);
        takeSearchRepository.save(take);
        int databaseSizeBeforeUpdate = takeRepository.findAll().size();

        // Update the take
        Take updatedTake = takeRepository.findOne(take.getId());
        // Disconnect from session so that the updates on updatedTake are not directly saved in db
        em.detach(updatedTake);
        updatedTake
            .grade(UPDATED_GRADE);

        restTakeMockMvc.perform(put("/api/takes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTake)))
            .andExpect(status().isOk());

        // Validate the Take in the database
        List<Take> takeList = takeRepository.findAll();
        assertThat(takeList).hasSize(databaseSizeBeforeUpdate);
        Take testTake = takeList.get(takeList.size() - 1);
        assertThat(testTake.getGrade()).isEqualTo(UPDATED_GRADE);

        // Validate the Take in Elasticsearch
        Take takeEs = takeSearchRepository.findOne(testTake.getId());
        assertThat(takeEs).isEqualToIgnoringGivenFields(testTake);
    }

    @Test
    @Transactional
    public void updateNonExistingTake() throws Exception {
        int databaseSizeBeforeUpdate = takeRepository.findAll().size();

        // Create the Take

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTakeMockMvc.perform(put("/api/takes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(take)))
            .andExpect(status().isCreated());

        // Validate the Take in the database
        List<Take> takeList = takeRepository.findAll();
        assertThat(takeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTake() throws Exception {
        // Initialize the database
        takeRepository.saveAndFlush(take);
        takeSearchRepository.save(take);
        int databaseSizeBeforeDelete = takeRepository.findAll().size();

        // Get the take
        restTakeMockMvc.perform(delete("/api/takes/{id}", take.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean takeExistsInEs = takeSearchRepository.exists(take.getId());
        assertThat(takeExistsInEs).isFalse();

        // Validate the database is empty
        List<Take> takeList = takeRepository.findAll();
        assertThat(takeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTake() throws Exception {
        // Initialize the database
        takeRepository.saveAndFlush(take);
        takeSearchRepository.save(take);

        // Search the take
        restTakeMockMvc.perform(get("/api/_search/takes?query=id:" + take.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(take.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Take.class);
        Take take1 = new Take();
        take1.setId(1L);
        Take take2 = new Take();
        take2.setId(take1.getId());
        assertThat(take1).isEqualTo(take2);
        take2.setId(2L);
        assertThat(take1).isNotEqualTo(take2);
        take1.setId(null);
        assertThat(take1).isNotEqualTo(take2);
    }
}
