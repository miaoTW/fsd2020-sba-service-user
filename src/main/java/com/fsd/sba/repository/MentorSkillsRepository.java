package com.fsd.sba.repository;
import com.fsd.sba.domain.MentorSkills;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the MentorSkills entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MentorSkillsRepository extends JpaRepository<MentorSkills, Long> {

    List<MentorSkills> findBySkillId(Long skillId);
}
