package com.pppppp.amadda.schedule.entity;

import java.time.LocalDateTime;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Schedule extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scheduleSeq;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_seq")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "repeated_schedule_seq")
	private RepeatedSchedule repeatedSchedule;

	@Column(nullable = false)
	private Boolean isTimeSelected;

	@Column
	private LocalDateTime scheduleStartAt;

	@Column
	private LocalDateTime scheduleEndAt;

	@Column(nullable = false)
	private Boolean isAlarmed;

	@Column(nullable = false)
	private Boolean isFinished;

	@Column
	private String scheduleContent;

	@Column(nullable = false)
	private Boolean isAuthorizedAll;
}
