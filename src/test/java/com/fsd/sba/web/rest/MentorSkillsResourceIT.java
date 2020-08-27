package com.fsd.sba.web.rest;

import com.fsd.sba.UserApp;
import com.fsd.sba.domain.MentorSkills;
import com.fsd.sba.repository.MentorSkillsRepository;
import com.fsd.sba.service.MentorSkillsService;
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
import java.util.List;

import static com.fsd.sba.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MentorSkillsResource} REST controller.
 */
@SpringBootTest(classes = UserApp.class)
public class MentorSkillsResourceIT {

    private static final Long DEFAULT_M_ID = 1L;
    private static final Long UPDATED_M_ID = 2L;

    private static final Long DEFAULT_S_ID = 1L;
    private static final Long UPDATED_S_ID = 2L;

    private static final Float DEFAULT_SELF_RATING = 1F;
    private static final Float UPDATED_SELF_RATING = 2F;

    private static final Integer DEFAULT_YEARS_OF_EXP = 1;
    private static final Integer UPDATED_YEARS_OF_EXP = 2;

    private static final String DEFAULT_TRAININGS_DELIVERED = "AAAAAAAAAA";
    private static final String UPDATED_TRAININGS_DELIVERED = "BBBBBBBBBB";

    private static final String DEFAULT_FACILITIES_OFFERED = "AAAAAAAAAA";
    private static final String UPDATED_FACILITIES_OFFERED = "BBBBBBBBBB";

    @Autowired
    private MentorSkillsRepository mentorSkillsRepository;

    @Autowired
    private MentorSkillsService mentorSkillsService;

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

    private MockMvc restMentorSkillsMockMvc;

    private MentorSkills mentorSkills;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MentorSkillsResource mentorSkillsResource = new MentorSkillsResource(mentorSkillsService);
        this.restMentorSkillsMockMvc = MockMvcBuilders.standaloneSetup(mentorSkillsResource)
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
    public static MentorSkills createEntity(EntityManager em) {
        MentorSkills mentorSkills = new MentorSkills()
            .mId(DEFAULT_M_ID)
            .skillId(DEFAULT_S_ID)
            .selfRating(DEFAULT_SELF_RATING)
            .yearsOfExp(DEFAULT_YEARS_OF_EXP)
            .trainingsDelivered(DEFAULT_TRAININGS_DELIVERED)
            .facilitiesOffered(DEFAULT_FACILITIES_OFFERED);
        return mentorSkills;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MentorSkills createUpdatedEntity(EntityManager em) {
        MentorSkills mentorSkills = new MentorSkills()
            .mId(UPDATED_M_ID)
            .skillId(UPDATED_S_ID)
            .selfRating(UPDATED_SELF_RATING)
            .yearsOfExp(UPDATED_YEARS_OF_EXP)
            .trainingsDelivered(UPDATED_TRAININGS_DELIVERED)
            .facilitiesOffered(UPDATED_FACILITIES_OFFERED);
        return mentorSkills;
    }

    @BeforeEach
    public void initTest() {
        mentorSkills = createEntity(em);
    }

    @Test
    @Transactional
    public void createMentorSkills() throws Exception {
        int databaseSizeBeforeCreate = mentorSkillsRepository.findAll().size();

        // Create the MentorSkills
        restMentorSkillsMockMvc.perform(post("/api/mentor-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorSkills)))
            .andExpect(status().isCreated());

        // Validate the MentorSkills in the database
        List<MentorSkills> mentorSkillsList = mentorSkillsRepository.findAll();
        assertThat(mentorSkillsList).hasSize(databaseSizeBeforeCreate + 1);
        MentorSkills testMentorSkills = mentorSkillsList.get(mentorSkillsList.size() - 1);
        assertThat(testMentorSkills.getmId()).isEqualTo(DEFAULT_M_ID);
        assertThat(testMentorSkills.getSkillId()).isEqualTo(DEFAULT_S_ID);
        assertThat(testMentorSkills.getSelfRating()).isEqualTo(DEFAULT_SELF_RATING);
        assertThat(testMentorSkills.getYearsOfExp()).isEqualTo(DEFAULT_YEARS_OF_EXP);
        assertThat(testMentorSkills.getTrainingsDelivered()).isEqualTo(DEFAULT_TRAININGS_DELIVERED);
        assertThat(testMentorSkills.getFacilitiesOffered()).isEqualTo(DEFAULT_FACILITIES_OFFERED);
    }

    @Test
    @Transactional
    public void createMentorSkillsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mentorSkillsRepository.findAll().size();

        // Create the MentorSkills with an existing ID
        mentorSkills.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMentorSkillsMockMvc.perform(post("/api/mentor-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorSkills)))
            .andExpect(status().isBadRequest());

        // Validate the MentorSkills in the database
        List<MentorSkills> mentorSkillsList = mentorSkillsRepository.findAll();
        assertThat(mentorSkillsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkmIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentorSkillsRepository.findAll().size();
        // set the field null
        mentorSkills.setmId(null);

        // Create the MentorSkills, which fails.

        restMentorSkillsMockMvc.perform(post("/api/mentor-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorSkills)))
            .andExpect(status().isBadRequest());

        List<MentorSkills> mentorSkillsList = mentorSkillsRepository.findAll();
        assertThat(mentorSkillsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checksIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentorSkillsRepository.findAll().size();
        // set the field null
        mentorSkills.setsId(null);

        // Create the MentorSkills, which fails.

        restMentorSkillsMockMvc.perform(post("/api/mentor-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorSkills)))
            .andExpect(status().isBadRequest());

        List<MentorSkills> mentorSkillsList = mentorSkillsRepository.findAll();
        assertThat(mentorSkillsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMentorSkills() throws Exception {
        // Initialize the database
        mentorSkillsRepository.saveAndFlush(mentorSkills);

        // Get all the mentorSkillsList
        restMentorSkillsMockMvc.perform(get("/api/mentor-skills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mentorSkills.getId().intValue())))
            .andExpect(jsonPath("$.[*].mId").value(hasItem(DEFAULT_M_ID.intValue())))
            .andExpect(jsonPath("$.[*].sId").value(hasItem(DEFAULT_S_ID.intValue())))
            .andExpect(jsonPath("$.[*].selfRating").value(hasItem(DEFAULT_SELF_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].yearsOfExp").value(hasItem(DEFAULT_YEARS_OF_EXP)))
            .andExpect(jsonPath("$.[*].trainingsDelivered").value(hasItem(DEFAULT_TRAININGS_DELIVERED)))
            .andExpect(jsonPath("$.[*].facilitiesOffered").value(hasItem(DEFAULT_FACILITIES_OFFERED)));
    }
    
    @Test
    @Transactional
    public void getMentorSkills() throws Exception {
        // Initialize the database
        mentorSkillsRepository.saveAndFlush(mentorSkills);

        // Get the mentorSkills
        restMentorSkillsMockMvc.perform(get("/api/mentor-skills/{id}", mentorSkills.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mentorSkills.getId().intValue()))
            .andExpect(jsonPath("$.mId").value(DEFAULT_M_ID.intValue()))
            .andExpect(jsonPath("$.sId").value(DEFAULT_S_ID.intValue()))
            .andExpect(jsonPath("$.selfRating").value(DEFAULT_SELF_RATING.doubleValue()))
            .andExpect(jsonPath("$.yearsOfExp").value(DEFAULT_YEARS_OF_EXP))
            .andExpect(jsonPath("$.trainingsDelivered").value(DEFAULT_TRAININGS_DELIVERED))
            .andExpect(jsonPath("$.facilitiesOffered").value(DEFAULT_FACILITIES_OFFERED));
    }

    @Test
    @Transactional
    public void getNonExistingMentorSkills() throws Exception {
        // Get the mentorSkills
        restMentorSkillsMockMvc.perform(get("/api/mentor-skills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMentorSkills() throws Exception {
        // Initialize the database
        mentorSkillsService.save(mentorSkills);

        int databaseSizeBeforeUpdate = mentorSkillsRepository.findAll().size();

        // Update the mentorSkills
        MentorSkills updatedMentorSkills = mentorSkillsRepository.findById(mentorSkills.getId()).get();
        // Disconnect from session so that the updates on updatedMentorSkills are not directly saved in db
        em.detach(updatedMentorSkills);
        updatedMentorSkills
            .mId(UPDATED_M_ID)
            .skillId(UPDATED_S_ID)
            .selfRating(UPDATED_SELF_RATING)
            .yearsOfExp(UPDATED_YEARS_OF_EXP)
            .trainingsDelivered(UPDATED_TRAININGS_DELIVERED)
            .facilitiesOffered(UPDATED_FACILITIES_OFFERED);

        restMentorSkillsMockMvc.perform(put("/api/mentor-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMentorSkills)))
            .andExpect(status().isOk());

        // Validate the MentorSkills in the database
        List<MentorSkills> mentorSkillsList = mentorSkillsRepository.findAll();
        assertThat(mentorSkillsList).hasSize(databaseSizeBeforeUpdate);
        MentorSkills testMentorSkills = mentorSkillsList.get(mentorSkillsList.size() - 1);
        assertThat(testMentorSkills.getmId()).isEqualTo(UPDATED_M_ID);
        assertThat(testMentorSkills.getSkillId()).isEqualTo(UPDATED_S_ID);
        assertThat(testMentorSkills.getSelfRating()).isEqualTo(UPDATED_SELF_RATING);
        assertThat(testMentorSkills.getYearsOfExp()).isEqualTo(UPDATED_YEARS_OF_EXP);
        assertThat(testMentorSkills.getTrainingsDelivered()).isEqualTo(UPDATED_TRAININGS_DELIVERED);
        assertThat(testMentorSkills.getFacilitiesOffered()).isEqualTo(UPDATED_FACILITIES_OFFERED);
    }

    @Test
    @Transactional
    public void updateNonExistingMentorSkills() throws Exception {
        int databaseSizeBeforeUpdate = mentorSkillsRepository.findAll().size();

        // Create the MentorSkills

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentorSkillsMockMvc.perform(put("/api/mentor-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorSkills)))
            .andExpect(status().isBadRequest());

        // Validate the MentorSkills in the database
        List<MentorSkills> mentorSkillsList = mentorSkillsRepository.findAll();
        assertThat(mentorSkillsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMentorSkills() throws Exception {
        // Initialize the database
        mentorSkillsService.save(mentorSkills);

        int databaseSizeBeforeDelete = mentorSkillsRepository.findAll().size();

        // Delete the mentorSkills
        restMentorSkillsMockMvc.perform(delete("/api/mentor-skills/{id}", mentorSkills.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MentorSkills> mentorSkillsList = mentorSkillsRepository.findAll();
        assertThat(mentorSkillsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MentorSkills.class);
        MentorSkills mentorSkills1 = new MentorSkills();
        mentorSkills1.setId(1L);
        MentorSkills mentorSkills2 = new MentorSkills();
        mentorSkills2.setId(mentorSkills1.getId());
        assertThat(mentorSkills1).isEqualTo(mentorSkills2);
        mentorSkills2.setId(2L);
        assertThat(mentorSkills1).isNotEqualTo(mentorSkills2);
        mentorSkills1.setId(null);
        assertThat(mentorSkills1).isNotEqualTo(mentorSkills2);
    }
}
