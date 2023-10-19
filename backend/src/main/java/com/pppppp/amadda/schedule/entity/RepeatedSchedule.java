package com.pppppp.amadda.schedule.entity;

import com.pppppp.amadda.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RepeatedSchedule extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long repeatedScheduleSeq;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true, length = 50)
	private RepeatedFreqency repeatedFrequency;
}
