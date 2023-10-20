package com.pppppp.amadda.schedule.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

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

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	@ColumnDefault("0")
	private boolean isTimeSelected;

	@Column
	private LocalDateTime scheduleStartAt;

	@Column
	private LocalDateTime scheduleEndAt;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	@ColumnDefault("0")
	private boolean isAlarmed;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	@ColumnDefault("0")
	private boolean isFinished;

	@Column
	private String scheduleContent;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	@ColumnDefault("0")
	private boolean isAuthorizedAll;
}
