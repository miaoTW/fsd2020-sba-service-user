package com.fsd.sba.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Mentor.
 */
@Entity
@Table(name = "mentor")
public class Mentor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "linkedin_url", nullable = false)
    private String linkedinUrl;

    @NotNull
    @Column(name = "reg_datetime", nullable = false)
    private Instant regDatetime;

    @Column(name = "reg_code")
    private String regCode;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public Mentor username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public Mentor linkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
        return this;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public Instant getRegDatetime() {
        return regDatetime;
    }

    public Mentor regDatetime(Instant regDatetime) {
        this.regDatetime = regDatetime;
        return this;
    }

    public void setRegDatetime(Instant regDatetime) {
        this.regDatetime = regDatetime;
    }

    public String getRegCode() {
        return regCode;
    }

    public Mentor regCode(String regCode) {
        this.regCode = regCode;
        return this;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public Mentor yearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
        return this;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Boolean isActive() {
        return active;
    }

    public Mentor active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mentor)) {
            return false;
        }
        return id != null && id.equals(((Mentor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Mentor{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", linkedinUrl='" + getLinkedinUrl() + "'" +
            ", regDatetime='" + getRegDatetime() + "'" +
            ", regCode='" + getRegCode() + "'" +
            ", yearsOfExperience=" + getYearsOfExperience() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
