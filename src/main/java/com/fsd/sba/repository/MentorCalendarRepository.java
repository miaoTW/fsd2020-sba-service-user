package com.fsd.sba.repository;

import com.fsd.sba.domain.MentorCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


/**
 * Spring Data  repository for the MentorCalendar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MentorCalendarRepository extends JpaRepository<MentorCalendar, Long> {

    List<MentorCalendar> findByMentorIdAndStartDatetimeAfter(Long mentorId, Instant startDatetime);
}
