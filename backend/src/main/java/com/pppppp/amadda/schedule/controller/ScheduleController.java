package com.pppppp.amadda.schedule.controller;

import com.pppppp.amadda.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("")
    public void createSchedule() {
        log.info("POST /api/schedule");
    }

    @GetMapping("/{scheduleSeq}")
    public void getSchedule(@PathVariable Long scheduleSeq) {
        log.info("GET /api/schedule/" + scheduleSeq);
    }

    @GetMapping("")
    public void getSchedule(@RequestParam(value = "category", required = false) Long categorySeq,
        @RequestParam(value = "searchKey", required = false) String searchKey,
        @RequestParam(value = "unscheduled", required = false) Long unscheduled) {
        log.info("GET /api/schedule?category=" + categorySeq + "&searchKey=" + searchKey
            + "&unscheduled=" + unscheduled);
    }

    @GetMapping("/{scheduleSeq}/participation")
    public void getParticipatingUsers(@PathVariable Long scheduleSeq,
        @RequestParam(value = "searchKey", required = false) String searchKey) {
        log.info("GET /api/schedule/" + scheduleSeq + "/participation?searchKey=" + searchKey);
    }
}
