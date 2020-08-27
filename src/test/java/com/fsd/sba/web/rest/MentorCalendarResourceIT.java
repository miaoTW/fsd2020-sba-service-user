package com.fsd.sba.web.rest;

import com.fsd.sba.UserApp;
import com.fsd.sba.domain.MentorCalendar;
import com.fsd.sba.repository.MentorCalendarRepository;
import com.fsd.sba.service.MentorCalendarService;
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
 * Integration tests for the {@link MentorCalendarResource} REST controller.
 */
@SpringBootTest(classes = UserApp.class)
public class MentorCalendarResourceIT {

    private static final Long DEFAULT_M_ID = 1L;
    private static final Long UPDATED_M_ID = 2L;

    private static final Instant DEFAULT_START_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MentorCalendarRepository mentorCalendarRepository;

    @Autowired
    private MentorCalendarService mentorCalendarService;

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

    private MockMvc restMentorCalendarMockMvc;

    private MentorCalendar mentorCalendar;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MentorCalendarResource mentorCalendarResource = new MentorCalendarResource(mentorCalendarService);
        this.restMentorCalendarMockMvc = MockMvcBuilders.standaloneSetup(mentorCalendarResource)
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
    public static MentorCalendar createEntity(EntityManager em) {
        MentorCalendar mentorCalendar = new MentorCalendar()
            .mentorId(DEFAULT_M_ID)
            .startDatetime(DEFAULT_START_DATETIME)
            .endDatetime(DEFAULT_END_DATETIME);
        return mentorCalendar;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MentorCalendar createUpdatedEntity(EntityManager em) {
        MentorCalendar mentorCalendar = new MentorCalendar()
            .mentorId(UPDATED_M_ID)
            .startDatetime(UPDATED_START_DATETIME)
            .endDatetime(UPDATED_END_DATETIME);
        return mentorCalendar;
    }

    @BeforeEach
    public void initTest() {
        mentorCalendar = createEntity(em);
    }

    @Test
    @Transactional
    public void createMentorCalendar() throws Exception {
        int databaseSizeBeforeCreate = mentorCalendarRepository.findAll().size();

        // Create the MentorCalendar
        restMentorCalendarMockMvc.perform(post("/api/mentor-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorCalendar)))
            .andExpect(status().isCreated());

        // Validate the MentorCalendar in the database
        List<MentorCalendar> mentorCalendarList = mentorCalendarRepository.findAll();
        assertThat(mentorCalendarList).hasSize(databaseSizeBeforeCreate + 1);
        MentorCalendar testMentorCalendar = mentorCalendarList.get(mentorCalendarList.size() - 1);
        assertThat(testMentorCalendar.getMentorId()).isEqualTo(DEFAULT_M_ID);
        assertThat(testMentorCalendar.getStartDatetime()).isEqualTo(DEFAULT_START_DATETIME);
        assertThat(testMentorCalendar.getEndDatetime()).isEqualTo(DEFAULT_END_DATETIME);
    }

    @Test
    @Transactional
    public void createMentorCalendarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mentorCalendarRepository.findAll().size();

        // Create the MentorCalendar with an existing ID
        mentorCalendar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMentorCalendarMockMvc.perform(post("/api/mentor-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorCalendar)))
            .andExpect(status().isBadRequest());

        // Validate the MentorCalendar in the database
        List<MentorCalendar> mentorCalendarList = mentorCalendarRepository.findAll();
        assertThat(mentorCalendarList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkmIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentorCalendarRepository.findAll().size();
        // set the field null
        mentorCalendar.setMentorId(null);

        // Create the MentorCalendar, which fails.

        restMentorCalendarMockMvc.perform(post("/api/mentor-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorCalendar)))
            .andExpect(status().isBadRequest());

        List<MentorCalendar> mentorCalendarList = mentorCalendarRepository.findAll();
        assertThat(mentorCalendarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMentorCalendars() throws Exception {
        // Initialize the database
        mentorCalendarRepository.saveAndFlush(mentorCalendar);

        // Get all the mentorCalendarList
        restMentorCalendarMockMvc.perform(get("/api/mentor-calendars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mentorCalendar.getId().intValue())))
            .andExpect(jsonPath("$.[*].mId").value(hasItem(DEFAULT_M_ID.intValue())))
            .andExpect(jsonPath("$.[*].startDatetime").value(hasItem(DEFAULT_START_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].endDatetime").value(hasItem(DEFAULT_END_DATETIME.toString())));
    }
    
    @Test
    @Transactional
    public void getMentorCalendar() throws Exception {
        // Initialize the database
        mentorCalendarRepository.saveAndFlush(mentorCalendar);

        // Get the mentorCalendar
        restMentorCalendarMockMvc.perform(get("/api/mentor-calendars/{id}", mentorCalendar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mentorCalendar.getId().intValue()))
            .andExpect(jsonPath("$.mId").value(DEFAULT_M_ID.intValue()))
            .andExpect(jsonPath("$.startDatetime").value(DEFAULT_START_DATETIME.toString()))
            .andExpect(jsonPath("$.endDatetime").value(DEFAULT_END_DATETIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMentorCalendar() throws Exception {
        // Get the mentorCalendar
        restMentorCalendarMockMvc.perform(get("/api/mentor-calendars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMentorCalendar() throws Exception {
        // Initialize the database
        mentorCalendarService.save(mentorCalendar);

        int databaseSizeBeforeUpdate = mentorCalendarRepository.findAll().size();

        // Update the mentorCalendar
        MentorCalendar updatedMentorCalendar = mentorCalendarRepository.findById(mentorCalendar.getId()).get();
        
        // Disconnect from session so that the updates on updatedMentorCalendar are not directly saved in db
        em.detach(updatedMentorCalendar);
        updatedMentorCalendar
            .mentorId(UPDATED_M_ID)
            .startDatetime(UPDATED_START_DATETIME)
            .endDatetime(UPDATED_END_DATETIME);

        restMentorCalendarMockMvc.perform(put("/api/mentor-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMentorCalendar)))
            .andExpect(status().isOk());

        // Validate the MentorCalendar in the database
        List<MentorCalendar> mentorCalendarList = mentorCalendarRepository.findAll();
        assertThat(mentorCalendarList).hasSize(databaseSizeBeforeUpdate);
        MentorCalendar testMentorCalendar = mentorCalendarList.get(mentorCalendarList.size() - 1);
        assertThat(testMentorCalendar.getMentorId()).isEqualTo(UPDATED_M_ID);
        assertThat(testMentorCalendar.getStartDatetime()).isEqualTo(UPDATED_START_DATETIME);
        assertThat(testMentorCalendar.getEndDatetime()).isEqualTo(UPDATED_END_DATETIME);
    }

    @Test
    @Transactional
    public void updateNonExistingMentorCalendar() throws Exception {
        int databaseSizeBeforeUpdate = mentorCalendarRepository.findAll().size();

        // Create the MentorCalendar

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentorCalendarMockMvc.perform(put("/api/mentor-calendars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorCalendar)))
            .andExpect(status().isBadRequest());

        // Validate the MentorCalendar in the database
        List<MentorCalendar> mentorCalendarList = mentorCalendarRepository.findAll();
        assertThat(mentorCalendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMentorCalendar() throws Exception {
        // Initialize the database
        mentorCalendarService.save(mentorCalendar);

        int databaseSizeBeforeDelete = mentorCalendarRepository.findAll().size();

        // Delete the mentorCalendar
        restMentorCalendarMockMvc.perform(delete("/api/mentor-calendars/{id}", mentorCalendar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MentorCalendar> mentorCalendarList = mentorCalendarRepository.findAll();
        assertThat(mentorCalendarList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MentorCalendar.class);
        MentorCalendar mentorCalendar1 = new MentorCalendar();
        mentorCalendar1.setId(1L);
        MentorCalendar mentorCalendar2 = new MentorCalendar();
        mentorCalendar2.setId(mentorCalendar1.getId());
        assertThat(mentorCalendar1).isEqualTo(mentorCalendar2);
        mentorCalendar2.setId(2L);
        assertThat(mentorCalendar1).isNotEqualTo(mentorCalendar2);
        mentorCalendar1.setId(null);
        assertThat(mentorCalendar1).isNotEqualTo(mentorCalendar2);
    }
}
