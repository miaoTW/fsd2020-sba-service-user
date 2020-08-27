package com.fsd.sba.web.rest;

import com.fsd.sba.domain.MentorSkills;
import com.fsd.sba.service.MentorSkillsService;
import com.fsd.sba.web.rest.errors.BadRequestAlertException;
import com.fsd.sba.web.rest.vm.user.MentorSkillVM;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.fsd.sba.domain.MentorSkills}.
 */
@RestController
@RequestMapping("/api")
public class MentorSkillsResource {

    private static final String ENTITY_NAME = "userMentorSkills";
    private final Logger log = LoggerFactory.getLogger(MentorSkillsResource.class);
    private final MentorSkillsService mentorSkillsService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public MentorSkillsResource(MentorSkillsService mentorSkillsService) {
        this.mentorSkillsService = mentorSkillsService;
    }

    /**
     * {@code POST  /mentor-skills} : Create a new mentorSkills.
     *
     * @param mentorSkills the mentorSkills to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mentorSkills, or with status {@code 400 (Bad Request)} if the mentorSkills has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mentor-skills")
    public ResponseEntity<MentorSkills> createMentorSkills(@Valid @RequestBody MentorSkills mentorSkills) throws URISyntaxException {
        log.debug("REST request to save MentorSkills : {}", mentorSkills);
        if (mentorSkills.getId() != null) {
            throw new BadRequestAlertException("A new mentorSkills cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MentorSkills result = mentorSkillsService.save(mentorSkills);
        return ResponseEntity.created(new URI("/api/mentor-skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mentor-skills} : Updates an existing mentorSkills.
     *
     * @param mentorSkills the mentorSkills to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mentorSkills,
     * or with status {@code 400 (Bad Request)} if the mentorSkills is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mentorSkills couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mentor-skills")
    public ResponseEntity<MentorSkills> updateMentorSkills(@Valid @RequestBody MentorSkills mentorSkills) throws URISyntaxException {
        log.debug("REST request to update MentorSkills : {}", mentorSkills);
        if (mentorSkills.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MentorSkills result = mentorSkillsService.save(mentorSkills);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mentorSkills.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mentor-skills} : get all the mentorSkills.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mentorSkills in body.
     */
    @GetMapping("/mentor-skills")
    public ResponseEntity<List<MentorSkills>> getAllMentorSkills(Pageable pageable) {
        log.debug("REST request to get a page of MentorSkills");
        Page<MentorSkills> page = mentorSkillsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mentor-skills/:id} : get the "id" mentorSkills.
     *
     * @param id the id of the mentorSkills to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mentorSkills, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mentor-skills/{id}")
    public ResponseEntity<MentorSkills> getMentorSkills(@PathVariable Long id) {
        log.debug("REST request to get MentorSkills : {}", id);
        Optional<MentorSkills> mentorSkills = mentorSkillsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mentorSkills);
    }

    /**
     * {@code DELETE  /mentor-skills/:id} : delete the "id" mentorSkills.
     *
     * @param id the id of the mentorSkills to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mentor-skills/{id}")
    public ResponseEntity<Void> deleteMentorSkills(@PathVariable Long id) {
        log.debug("REST request to delete MentorSkills : {}", id);
        mentorSkillsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/mentor-skills/skill/{skillId}")
    public List<MentorSkillVM> findBySkill(@PathVariable Long skillId) {
        return mentorSkillsService.findBySkill(skillId);
    }
}
