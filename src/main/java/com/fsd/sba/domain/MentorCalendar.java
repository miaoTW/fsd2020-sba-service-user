package com.fsd.sba.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A MentorCalendar.
 */
@Entity
@Table(name = "mentor_calendar")
public class MentorCalendar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "m_id", nullable = false)
    private Long mentorId;

    @Column(name = "start_datetime")
    private Instant startDatetime;

    @Column(name = "end_datetime")
    private Instant endDatetime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public MentorCalendar mentorId(Long mentorId) {
        this.mentorId = mentorId;
        return this;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public Instant getStartDatetime() {
        return startDatetime;
    }

    public MentorCalendar startDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
        return this;
    }

    public void setStartDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Instant getEndDatetime() {
        return endDatetime;
    }

    public MentorCalendar endDatetime(Instant endDatetime) {
        this.endDatetime = endDatetime;
        return this;
    }

    public void setEndDatetime(Instant endDatetime) {
        this.endDatetime = endDatetime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MentorCalendar)) {
            return false;
        }
        return id != null && id.equals(((MentorCalendar) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MentorCalendar{" +
            "id=" + getId() +
            ", mId=" + getMentorId() +
            ", startDatetime='" + getStartDatetime() + "'" +
            ", endDatetime='" + getEndDatetime() + "'" +
            "}";
    }
}
