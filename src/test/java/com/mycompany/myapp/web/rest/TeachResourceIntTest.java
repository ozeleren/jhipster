package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.Teach;
import com.mycompany.myapp.repository.TeachRepository;
import com.mycompany.myapp.repository.search.TeachSearchRepository;
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
 * Test class for the TeachResource REST controller.
 *
 * @see TeachResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class TeachResourceIntTest {

    @Autowired
    private TeachRepository teachRepository;

    @Autowired
    private TeachSearchRepository teachSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTeachMockMvc;

    private Teach teach;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeachResource teachResource = new TeachResource(teachRepository, teachSearchRepository);
        this.restTeachMockMvc = MockMvcBuilders.standaloneSetup(teachResource)
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
    public static Teach createEntity(EntityManager em) {
        Teach teach = new Teach();
        return teach;
    }

    @Before
    public void initTest() {
        teachSearchRepository.deleteAll();
        teach = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeach() throws Exception {
        int databaseSizeBeforeCreate = teachRepository.findAll().size();

        // Create the Teach
        restTeachMockMvc.perform(post("/api/teaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teach)))
            .andExpect(status().isCreated());

        // Validate the Teach in the database
        List<Teach> teachList = teachRepository.findAll();
        assertThat(teachList).hasSize(databaseSizeBeforeCreate + 1);
        Teach testTeach = teachList.get(teachList.size() - 1);

        // Validate the Teach in Elasticsearch
        Teach teachEs = teachSearchRepository.findOne(testTeach.getId());
        assertThat(teachEs).isEqualToIgnoringGivenFields(testTeach);
    }

    @Test
    @Transactional
    public void createTeachWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teachRepository.findAll().size();

        // Create the Teach with an existing ID
        teach.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeachMockMvc.perform(post("/api/teaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teach)))
            .andExpect(status().isBadRequest());

        // Validate the Teach in the database
        List<Teach> teachList = teachRepository.findAll();
        assertThat(teachList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTeaches() throws Exception {
        // Initialize the database
        teachRepository.saveAndFlush(teach);

        // Get all the teachList
        restTeachMockMvc.perform(get("/api/teaches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teach.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTeach() throws Exception {
        // Initialize the database
        teachRepository.saveAndFlush(teach);

        // Get the teach
        restTeachMockMvc.perform(get("/api/teaches/{id}", teach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teach.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTeach() throws Exception {
        // Get the teach
        restTeachMockMvc.perform(get("/api/teaches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeach() throws Exception {
        // Initialize the database
        teachRepository.saveAndFlush(teach);
        teachSearchRepository.save(teach);
        int databaseSizeBeforeUpdate = teachRepository.findAll().size();

        // Update the teach
        Teach updatedTeach = teachRepository.findOne(teach.getId());
        // Disconnect from session so that the updates on updatedTeach are not directly saved in db
        em.detach(updatedTeach);

        restTeachMockMvc.perform(put("/api/teaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeach)))
            .andExpect(status().isOk());

        // Validate the Teach in the database
        List<Teach> teachList = teachRepository.findAll();
        assertThat(teachList).hasSize(databaseSizeBeforeUpdate);
        Teach testTeach = teachList.get(teachList.size() - 1);

        // Validate the Teach in Elasticsearch
        Teach teachEs = teachSearchRepository.findOne(testTeach.getId());
        assertThat(teachEs).isEqualToIgnoringGivenFields(testTeach);
    }

    @Test
    @Transactional
    public void updateNonExistingTeach() throws Exception {
        int databaseSizeBeforeUpdate = teachRepository.findAll().size();

        // Create the Teach

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeachMockMvc.perform(put("/api/teaches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teach)))
            .andExpect(status().isCreated());

        // Validate the Teach in the database
        List<Teach> teachList = teachRepository.findAll();
        assertThat(teachList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTeach() throws Exception {
        // Initialize the database
        teachRepository.saveAndFlush(teach);
        teachSearchRepository.save(teach);
        int databaseSizeBeforeDelete = teachRepository.findAll().size();

        // Get the teach
        restTeachMockMvc.perform(delete("/api/teaches/{id}", teach.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean teachExistsInEs = teachSearchRepository.exists(teach.getId());
        assertThat(teachExistsInEs).isFalse();

        // Validate the database is empty
        List<Teach> teachList = teachRepository.findAll();
        assertThat(teachList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTeach() throws Exception {
        // Initialize the database
        teachRepository.saveAndFlush(teach);
        teachSearchRepository.save(teach);

        // Search the teach
        restTeachMockMvc.perform(get("/api/_search/teaches?query=id:" + teach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teach.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Teach.class);
        Teach teach1 = new Teach();
        teach1.setId(1L);
        Teach teach2 = new Teach();
        teach2.setId(teach1.getId());
        assertThat(teach1).isEqualTo(teach2);
        teach2.setId(2L);
        assertThat(teach1).isNotEqualTo(teach2);
        teach1.setId(null);
        assertThat(teach1).isNotEqualTo(teach2);
    }
}
