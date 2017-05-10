package ru.ulmc.bank.dao.entity.directory;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Диапазон курса валют
 */
@Getter
@Entity
@Table(name = "DIR_COURSE_RANGE",
        indexes = {@Index(name = "course_range_id_index", columnList = "ID", unique = true)})
@SequenceGenerator(name = "SEQ_COURSE_RANGE", allocationSize = 1)
public class CourseRange {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "RANGE_FROM", nullable = false)
    private Double rangeFrom;

    @Column(name = "RANGE_TO", nullable = false)
    private Double rangeTo;

    @Setter
    @Column(name = "DISPLAY_NAME", nullable = false)
    private Double rangeStart;

}
