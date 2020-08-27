package com.fsd.sba.web.rest;

import com.fsd.sba.domain.MentorCalendar;
import com.fsd.sba.service.MentorCalendarService;
import com.fsd.sba.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.fsd.sba.domain.MentorCalendar}.
 */
@RestController
@RequestMapping("/api")
public class MentorCalendarResource {

    private final Logger log = LoggerFactory.getLogger(MentorCalendarResource.class);

    private static final String ENTITY_NAME = "userMentorCalendar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MentorCalendarService mentorCalendarService;

    public MentorCalendarResource(MentorCalendarService mentorCalendarService) {
        this.mentorCalendarService = mentorCalendarService;
    }

    /**
     * {@code POST  /mentor-calendars} : Create a new mentorCalendar.
     *
     * @param mentorCalendar the mentorCalendar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mentorCalendar, or with status {@code 400 (Bad Request)} if the mentorCalendar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mentor-calendars")
    public ResponseEntity<MentorCalendar> createMentorCalendar(@Valid @RequestBody MentorCalendar mentorCalendar) throws URISyntaxException {
        log.debug("REST request to save MentorCalendar : {}", mentorCalendar);
        if (mentorCalendar.getId() != null) {
            throw new BadRequestAlertException("A new mentorCalendar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MentorCalendar result = mentorCalendarService.save(mentorCalendar);
        return ResponseEntity.created(new URI("/api/mentor-calendars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mentor-calendars} : Updates an existing mentorCalendar.
     *
     * @param mentorCalendar the mentorCalendar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mentorCalendar,
     * or with status {@code 400 (Bad Request)} if the mentorCalendar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mentorCalendar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mentor-calendars")
    public ResponseEntity<MentorCalendar> updateMentorCalendar(@Valid @RequestBody MentorCalendar mentorCalendar) throws URISyntaxException {
        log.debug("REST request to update MentorCalendar : {}", mentorCalendar);
        if (mentorCalendar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MentorCalendar result = mentorCalendarService.save(mentorCalendar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mentorCalendar.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mentor-calendars} : get all the mentorCalendars.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mentorCalendars in body.
     */
    @GetMapping("/mentor-calendars")
    public ResponseEntity<List<MentorCalendar>> getAllMentorCalendars(Pageable pageable) {
        log.debug("REST request to get a page of MentorCalendars");
        Page<MentorCalendar> page = mentorCalendarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mentor-calendars/:id} : get the "id" mentorCalendar.
     *
     * @param id the id of the mentorCalendar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mentorCalendar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mentor-calendars/{id}")
    public ResponseEntity<MentorCalendar> getMentorCalendar(@PathVariable Long id) {
        log.debug("REST request to get MentorCalendar : {}", id);
        Optional<MentorCalendar> mentorCalendar = mentorCalendarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mentorCalendar);
    }

    /**
     * {@code DELETE  /mentor-calendars/:id} : delete the "id" mentorCalendar.
     *
     * @param id the id of the mentorCalendar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mentor-calendars/{id}")
    public ResponseEntity<Void> deleteMentorCalendar(@PathVariable Long id) {
        log.debug("REST request to delete MentorCalendar : {}", id);
        mentorCalendarService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
