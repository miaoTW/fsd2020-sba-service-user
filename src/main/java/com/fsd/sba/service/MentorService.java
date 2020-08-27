package com.fsd.sba.service;

import com.fsd.sba.domain.Mentor;
import com.fsd.sba.repository.MentorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Mentor}.
 */
@Service
@Transactional
public class MentorService {

    private final Logger log = LoggerFactory.getLogger(MentorService.class);

    private final MentorRepository mentorRepository;

    public MentorService(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    /**
     * Save a mentor.
     *
     * @param mentor the entity to save.
     * @return the persisted entity.
     */
    public Mentor save(Mentor mentor) {
        log.debug("Request to save Mentor : {}", mentor);
        return mentorRepository.save(mentor);
    }

    /**
     * Get all the mentors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Mentor> findAll(Pageable pageable) {
        return mentorRepository.findAll(pageable);
    }

    public Page<Mentor> findByUsernameContaining(Pageable pageable,String username) {
        return mentorRepository.findByUsernameContaining(pageable,username);
    }

    /**
     * Get one mentor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Mentor> findOne(Long id) {
        log.debug("Request to get Mentor : {}", id);
        return mentorRepository.findById(id);
    }

    /**
     * Delete the mentor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Mentor : {}", id);
        mentorRepository.deleteById(id);
    }
}
