package com.fsd.sba.web.rest.vm.user;

import java.math.BigDecimal;
import java.util.List;

public class MentorSkillVM {
    private Long id;
    private Long mentorId;
    private String name;
    private int yearsOfExp;
    private String trainingsDelivered;
    private BigDecimal fee;
    private List<MentorCalendarVM> mentorCalendarList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearsOfExp() {
        return yearsOfExp;
    }

    public void setYearsOfExp(int yearsOfExp) {
        this.yearsOfExp = yearsOfExp;
    }

    public String getTrainingsDelivered() {
        return trainingsDelivered;
    }

    public void setTrainingsDelivered(String trainingsDelivered) {
        this.trainingsDelivered = trainingsDelivered;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public List<MentorCalendarVM> getMentorCalendarList() {
        return mentorCalendarList;
    }

    public void setMentorCalendarList(List<MentorCalendarVM> mentorCalendarList) {
        this.mentorCalendarList = mentorCalendarList;
    }
}
