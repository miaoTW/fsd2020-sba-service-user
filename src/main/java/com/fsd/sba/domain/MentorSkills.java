package com.fsd.sba.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A MentorSkills.
 */
@Entity
@Table(name = "mentor_skills")
public class MentorSkills implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "m_id", nullable = false)
    private Long mId;

    @NotNull
    @Column(name = "s_id", nullable = false)
    private Long skillId;

    @Column(name = "self_rating")
    private Float selfRating;

    @Column(name = "years_of_exp")
    private Integer yearsOfExp;

    @Column(name = "trainings_delivered")
    private String trainingsDelivered;

    @Column(name = "facilities_offered")
    private String facilitiesOffered;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getmId() {
        return mId;
    }

    public MentorSkills mId(Long mId) {
        this.mId = mId;
        return this;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public MentorSkills skillId(Long skillId) {
        this.skillId = skillId;
        return this;
    }

    public void setsId(Long skillId) {
        this.skillId = skillId;
    }

    public Float getSelfRating() {
        return selfRating;
    }

    public MentorSkills selfRating(Float selfRating) {
        this.selfRating = selfRating;
        return this;
    }

    public void setSelfRating(Float selfRating) {
        this.selfRating = selfRating;
    }

    public Integer getYearsOfExp() {
        return yearsOfExp;
    }

    public MentorSkills yearsOfExp(Integer yearsOfExp) {
        this.yearsOfExp = yearsOfExp;
        return this;
    }

    public void setYearsOfExp(Integer yearsOfExp) {
        this.yearsOfExp = yearsOfExp;
    }

    public String getTrainingsDelivered() {
        return trainingsDelivered;
    }

    public MentorSkills trainingsDelivered(String trainingsDelivered) {
        this.trainingsDelivered = trainingsDelivered;
        return this;
    }

    public void setTrainingsDelivered(String trainingsDelivered) {
        this.trainingsDelivered = trainingsDelivered;
    }

    public String getFacilitiesOffered() {
        return facilitiesOffered;
    }

    public MentorSkills facilitiesOffered(String facilitiesOffered) {
        this.facilitiesOffered = facilitiesOffered;
        return this;
    }

    public void setFacilitiesOffered(String facilitiesOffered) {
        this.facilitiesOffered = facilitiesOffered;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MentorSkills)) {
            return false;
        }
        return id != null && id.equals(((MentorSkills) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MentorSkills{" +
            "id=" + getId() +
            ", mId=" + getmId() +
            ", sId=" + getSkillId() +
            ", selfRating=" + getSelfRating() +
            ", yearsOfExp=" + getYearsOfExp() +
            ", trainingsDelivered='" + getTrainingsDelivered() + "'" +
            ", facilitiesOffered='" + getFacilitiesOffered() + "'" +
            "}";
    }
}
