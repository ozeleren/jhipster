package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.Instructor;
import com.mycompany.myapp.repository.InstructorRepository;
import com.mycompany.myapp.repository.search.InstructorSearchRepository;
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
 * Test class for the InstructorResource REST controller.
 *
 * @see InstructorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class InstructorResourceIntTest {

    private static final String DEFAULT_FNAME = "AAAAAAAAAA";
    private static final String UPDATED_FNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LNAME = "AAAAAAAAAA";
    private static final String UPDATED_LNAME = "BBBBBBBBBB";

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private InstructorSearchRepository instructorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInstructorMockMvc;

    private Instructor instructor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InstructorResource instructorResource = new InstructorResource(instructorRepository, instructorSearchRepository);
        this.restInstructorMockMvc = MockMvcBuilders.standaloneSetup(instructorResource)
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
    public static Instructor createEntity(EntityManager em) {
        Instructor instructor = new Instructor()
            .fname(DEFAULT_FNAME)
            .lname(DEFAULT_LNAME);
        return instructor;
    }

    @Before
    public void initTest() {
        instructorSearchRepository.deleteAll();
        instructor = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstructor() throws Exception {
        int databaseSizeBeforeCreate = instructorRepository.findAll().size();

        // Create the Instructor
        restInstructorMockMvc.perform(post("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isCreated());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate + 1);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getFname()).isEqualTo(DEFAULT_FNAME);
        assertThat(testInstructor.getLname()).isEqualTo(DEFAULT_LNAME);

        // Validate the Instructor in Elasticsearch
        Instructor instructorEs = instructorSearchRepository.findOne(testInstructor.getId());
        assertThat(instructorEs).isEqualToIgnoringGivenFields(testInstructor);
    }

    @Test
    @Transactional
    public void createInstructorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = instructorRepository.findAll().size();

        // Create the Instructor with an existing ID
        instructor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstructorMockMvc.perform(post("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isBadRequest());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInstructors() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        // Get all the instructorList
        restInstructorMockMvc.perform(get("/api/instructors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instructor.getId().intValue())))
            .andExpect(jsonPath("$.[*].fname").value(hasItem(DEFAULT_FNAME.toString())))
            .andExpect(jsonPath("$.[*].lname").value(hasItem(DEFAULT_LNAME.toString())));
    }

    @Test
    @Transactional
    public void getInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        // Get the instructor
        restInstructorMockMvc.perform(get("/api/instructors/{id}", instructor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(instructor.getId().intValue()))
            .andExpect(jsonPath("$.fname").value(DEFAULT_FNAME.toString()))
            .andExpect(jsonPath("$.lname").value(DEFAULT_LNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstructor() throws Exception {
        // Get the instructor
        restInstructorMockMvc.perform(get("/api/instructors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        instructorSearchRepository.save(instructor);
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();

        // Update the instructor
        Instructor updatedInstructor = instructorRepository.findOne(instructor.getId());
        // Disconnect from session so that the updates on updatedInstructor are not directly saved in db
        em.detach(updatedInstructor);
        updatedInstructor
            .fname(UPDATED_FNAME)
            .lname(UPDATED_LNAME);

        restInstructorMockMvc.perform(put("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInstructor)))
            .andExpect(status().isOk());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getFname()).isEqualTo(UPDATED_FNAME);
        assertThat(testInstructor.getLname()).isEqualTo(UPDATED_LNAME);

        // Validate the Instructor in Elasticsearch
        Instructor instructorEs = instructorSearchRepository.findOne(testInstructor.getId());
        assertThat(instructorEs).isEqualToIgnoringGivenFields(testInstructor);
    }

    @Test
    @Transactional
    public void updateNonExistingInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();

        // Create the Instructor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInstructorMockMvc.perform(put("/api/instructors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isCreated());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        instructorSearchRepository.save(instructor);
        int databaseSizeBeforeDelete = instructorRepository.findAll().size();

        // Get the instructor
        restInstructorMockMvc.perform(delete("/api/instructors/{id}", instructor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean instructorExistsInEs = instructorSearchRepository.exists(instructor.getId());
        assertThat(instructorExistsInEs).isFalse();

        // Validate the database is empty
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        instructorSearchRepository.save(instructor);

        // Search the instructor
        restInstructorMockMvc.perform(get("/api/_search/instructors?query=id:" + instructor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instructor.getId().intValue())))
            .andExpect(jsonPath("$.[*].fname").value(hasItem(DEFAULT_FNAME.toString())))
            .andExpect(jsonPath("$.[*].lname").value(hasItem(DEFAULT_LNAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Instructor.class);
        Instructor instructor1 = new Instructor();
        instructor1.setId(1L);
        Instructor instructor2 = new Instructor();
        instructor2.setId(instructor1.getId());
        assertThat(instructor1).isEqualTo(instructor2);
        instructor2.setId(2L);
        assertThat(instructor1).isNotEqualTo(instructor2);
        instructor1.setId(null);
        assertThat(instructor1).isNotEqualTo(instructor2);
    }
}
