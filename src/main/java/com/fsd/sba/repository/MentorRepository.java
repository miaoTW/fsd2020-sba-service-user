package com.fsd.sba.repository;
import com.fsd.sba.domain.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Mentor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {

    Page<Mentor> findByUsernameContaining(Pageable pageable, String username);
}
