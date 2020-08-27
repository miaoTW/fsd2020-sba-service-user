package com.fsd.sba.web.rest.vm.user;

import java.time.Instant;

public class MentorCalendarVM {
    private Instant startDatetime;
    private Instant endDatetime;

    public Instant getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Instant getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Instant endDatetime) {
        this.endDatetime = endDatetime;
    }
}
