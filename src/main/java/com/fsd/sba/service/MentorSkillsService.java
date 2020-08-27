package com.fsd.sba.service;

import com.fsd.sba.domain.MentorCalendar;
import com.fsd.sba.domain.MentorSkills;
import com.fsd.sba.repository.MentorCalendarRepository;
import com.fsd.sba.repository.MentorRepository;
import com.fsd.sba.repository.MentorSkillsRepository;
import com.fsd.sba.web.rest.vm.user.MentorCalendarVM;
import com.fsd.sba.web.rest.vm.user.MentorSkillVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MentorSkills}.
 */
@Service
@Transactional
public class MentorSkillsService {

    private final Logger log = LoggerFactory.getLogger(MentorSkillsService.class);

    private final MentorSkillsRepository mentorSkillsRepository;
    private final MentorRepository mentorRepository;
    private final MentorCalendarRepository mentorCalendarRepository;

    public MentorSkillsService(MentorSkillsRepository mentorSkillsRepository, MentorRepository mentorRepository, MentorCalendarRepository mentorCalendarRepository) {
        this.mentorSkillsRepository = mentorSkillsRepository;
        this.mentorRepository = mentorRepository;
        this.mentorCalendarRepository = mentorCalendarRepository;
    }

    /**
     * Save a mentorSkills.
     *
     * @param mentorSkills the entity to save.
     * @return the persisted entity.
     */
    public MentorSkills save(MentorSkills mentorSkills) {
        log.debug("Request to save MentorSkills : {}", mentorSkills);
        return mentorSkillsRepository.save(mentorSkills);
    }

    /**
     * Get all the mentorSkills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MentorSkills> findAll(Pageable pageable) {
        log.debug("Request to get all MentorSkills");
        return mentorSkillsRepository.findAll(pageable);
    }


    /**
     * Get one mentorSkills by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MentorSkills> findOne(Long id) {
        log.debug("Request to get MentorSkills : {}", id);
        return mentorSkillsRepository.findById(id);
    }

    /**
     * Delete the mentorSkills by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MentorSkills : {}", id);
        mentorSkillsRepository.deleteById(id);
    }

    public List<MentorSkillVM> findBySkill(Long skillId) {
        return mentorSkillsRepository.findBySkillId(skillId)
            .stream()
            .map(skill -> {
                MentorSkillVM vm = new MentorSkillVM();
                vm.setId(skill.getId());
                vm.setMentorId(skill.getmId());
                vm.setYearsOfExp(skill.getYearsOfExp());
                vm.setTrainingsDelivered(skill.getTrainingsDelivered());
                mentorRepository.findById(skill.getmId())
                    .ifPresent((mentor) -> vm.setName(mentor.getUsername()));

                List<MentorCalendar> calendarList = mentorCalendarRepository.findByMentorIdAndStartDatetimeAfter(skill.getmId(), Instant.now());

                vm.setMentorCalendarList(
                    calendarList.stream().map(
                        calendar -> {
                            MentorCalendarVM mentorCalendarVM = new MentorCalendarVM();
                            mentorCalendarVM.setStartDatetime(calendar.getStartDatetime());
                            mentorCalendarVM.setEndDatetime(calendar.getEndDatetime());
                            return mentorCalendarVM;
                        }
                    ).collect(Collectors.toList())
                );
                return vm;
            })
            .filter(mentorSkillVM -> !CollectionUtils.isEmpty(mentorSkillVM.getMentorCalendarList()))
            .collect(Collectors.toList());
    }
}
