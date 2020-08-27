package com.fsd.sba.web.rest;

import com.fsd.sba.UserApp;
import com.fsd.sba.domain.Mentor;
import com.fsd.sba.repository.MentorRepository;
import com.fsd.sba.service.MentorService;
import com.fsd.sba.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.fsd.sba.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MentorResource} REST controller.
 */
@SpringBootTest(classes = UserApp.class)
public class MentorResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDIN_URL = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDIN_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_REG_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REG_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REG_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEARS_OF_EXPERIENCE = 1;
    private static final Integer UPDATED_YEARS_OF_EXPERIENCE = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MentorService mentorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMentorMockMvc;

    private Mentor mentor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MentorResource mentorResource = new MentorResource(mentorService);
        this.restMentorMockMvc = MockMvcBuilders.standaloneSetup(mentorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mentor createEntity(EntityManager em) {
        Mentor mentor = new Mentor()
            .username(DEFAULT_USERNAME)
            .linkedinUrl(DEFAULT_LINKEDIN_URL)
            .regDatetime(DEFAULT_REG_DATETIME)
            .regCode(DEFAULT_REG_CODE)
            .yearsOfExperience(DEFAULT_YEARS_OF_EXPERIENCE)
            .active(DEFAULT_ACTIVE);
        return mentor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mentor createUpdatedEntity(EntityManager em) {
        Mentor mentor = new Mentor()
            .username(UPDATED_USERNAME)
            .linkedinUrl(UPDATED_LINKEDIN_URL)
            .regDatetime(UPDATED_REG_DATETIME)
            .regCode(UPDATED_REG_CODE)
            .yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE)
            .active(UPDATED_ACTIVE);
        return mentor;
    }

    @BeforeEach
    public void initTest() {
        mentor = createEntity(em);
    }

    @Test
    @Transactional
    public void createMentor() throws Exception {
        int databaseSizeBeforeCreate = mentorRepository.findAll().size();

        // Create the Mentor
        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentor)))
            .andExpect(status().isCreated());

        // Validate the Mentor in the database
        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeCreate + 1);
        Mentor testMentor = mentorList.get(mentorList.size() - 1);
        assertThat(testMentor.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testMentor.getLinkedinUrl()).isEqualTo(DEFAULT_LINKEDIN_URL);
        assertThat(testMentor.getRegDatetime()).isEqualTo(DEFAULT_REG_DATETIME);
        assertThat(testMentor.getRegCode()).isEqualTo(DEFAULT_REG_CODE);
        assertThat(testMentor.getYearsOfExperience()).isEqualTo(DEFAULT_YEARS_OF_EXPERIENCE);
        assertThat(testMentor.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createMentorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mentorRepository.findAll().size();

        // Create the Mentor with an existing ID
        mentor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentor)))
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentorRepository.findAll().size();
        // set the field null
        mentor.setUsername(null);

        // Create the Mentor, which fails.

        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentor)))
            .andExpect(status().isBadRequest());

        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLinkedinUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentorRepository.findAll().size();
        // set the field null
        mentor.setLinkedinUrl(null);

        // Create the Mentor, which fails.

        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentor)))
            .andExpect(status().isBadRequest());

        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegDatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentorRepository.findAll().size();
        // set the field null
        mentor.setRegDatetime(null);

        // Create the Mentor, which fails.

        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentor)))
            .andExpect(status().isBadRequest());

        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentorRepository.findAll().size();
        // set the field null
        mentor.setActive(null);

        // Create the Mentor, which fails.

        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentor)))
            .andExpect(status().isBadRequest());

        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMentors() throws Exception {
        // Initialize the database
        mentorRepository.saveAndFlush(mentor);

        // Get all the mentorList
        restMentorMockMvc.perform(get("/api/mentors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mentor.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].linkedinUrl").value(hasItem(DEFAULT_LINKEDIN_URL)))
            .andExpect(jsonPath("$.[*].regDatetime").value(hasItem(DEFAULT_REG_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].regCode").value(hasItem(DEFAULT_REG_CODE)))
            .andExpect(jsonPath("$.[*].yearsOfExperience").value(hasItem(DEFAULT_YEARS_OF_EXPERIENCE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMentor() throws Exception {
        // Initialize the database
        mentorRepository.saveAndFlush(mentor);

        // Get the mentor
        restMentorMockMvc.perform(get("/api/mentors/{id}", mentor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mentor.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.linkedinUrl").value(DEFAULT_LINKEDIN_URL))
            .andExpect(jsonPath("$.regDatetime").value(DEFAULT_REG_DATETIME.toString()))
            .andExpect(jsonPath("$.regCode").value(DEFAULT_REG_CODE))
            .andExpect(jsonPath("$.yearsOfExperience").value(DEFAULT_YEARS_OF_EXPERIENCE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMentor() throws Exception {
        // Get the mentor
        restMentorMockMvc.perform(get("/api/mentors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMentor() throws Exception {
        // Initialize the database
        mentorService.save(mentor);

        int databaseSizeBeforeUpdate = mentorRepository.findAll().size();

        // Update the mentor
        Mentor updatedMentor = mentorRepository.findById(mentor.getId()).get();
        // Disconnect from session so that the updates on updatedMentor are not directly saved in db
        em.detach(updatedMentor);
        updatedMentor
            .username(UPDATED_USERNAME)
            .linkedinUrl(UPDATED_LINKEDIN_URL)
            .regDatetime(UPDATED_REG_DATETIME)
            .regCode(UPDATED_REG_CODE)
            .yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE)
            .active(UPDATED_ACTIVE);

        restMentorMockMvc.perform(put("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMentor)))
            .andExpect(status().isOk());

        // Validate the Mentor in the database
        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeUpdate);
        Mentor testMentor = mentorList.get(mentorList.size() - 1);
        assertThat(testMentor.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testMentor.getLinkedinUrl()).isEqualTo(UPDATED_LINKEDIN_URL);
        assertThat(testMentor.getRegDatetime()).isEqualTo(UPDATED_REG_DATETIME);
        assertThat(testMentor.getRegCode()).isEqualTo(UPDATED_REG_CODE);
        assertThat(testMentor.getYearsOfExperience()).isEqualTo(UPDATED_YEARS_OF_EXPERIENCE);
        assertThat(testMentor.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingMentor() throws Exception {
        int databaseSizeBeforeUpdate = mentorRepository.findAll().size();

        // Create the Mentor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentorMockMvc.perform(put("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentor)))
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMentor() throws Exception {
        // Initialize the database
        mentorService.save(mentor);

        int databaseSizeBeforeDelete = mentorRepository.findAll().size();

        // Delete the mentor
        restMentorMockMvc.perform(delete("/api/mentors/{id}", mentor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mentor.class);
        Mentor mentor1 = new Mentor();
        mentor1.setId(1L);
        Mentor mentor2 = new Mentor();
        mentor2.setId(mentor1.getId());
        assertThat(mentor1).isEqualTo(mentor2);
        mentor2.setId(2L);
        assertThat(mentor1).isNotEqualTo(mentor2);
        mentor1.setId(null);
        assertThat(mentor1).isNotEqualTo(mentor2);
    }
}
