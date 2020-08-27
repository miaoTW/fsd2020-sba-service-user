package com.fsd.sba.service;

import com.fsd.sba.domain.MentorCalendar;
import com.fsd.sba.repository.MentorCalendarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MentorCalendar}.
 */
@Service
@Transactional
public class MentorCalendarService {

    private final Logger log = LoggerFactory.getLogger(MentorCalendarService.class);

    private final MentorCalendarRepository mentorCalendarRepository;

    public MentorCalendarService(MentorCalendarRepository mentorCalendarRepository) {
        this.mentorCalendarRepository = mentorCalendarRepository;
    }

    /**
     * Save a mentorCalendar.
     *
     * @param mentorCalendar the entity to save.
     * @return the persisted entity.
     */
    public MentorCalendar save(MentorCalendar mentorCalendar) {
        log.debug("Request to save MentorCalendar : {}", mentorCalendar);
        return mentorCalendarRepository.save(mentorCalendar);
    }

    /**
     * Get all the mentorCalendars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MentorCalendar> findAll(Pageable pageable) {
        log.debug("Request to get all MentorCalendars");
        return mentorCalendarRepository.findAll(pageable);
    }


    /**
     * Get one mentorCalendar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MentorCalendar> findOne(Long id) {
        log.debug("Request to get MentorCalendar : {}", id);
        return mentorCalendarRepository.findById(id);
    }

    /**
     * Delete the mentorCalendar by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MentorCalendar : {}", id);
        mentorCalendarRepository.deleteById(id);
    }
}
