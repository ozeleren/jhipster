package com.mycompany.myapp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Document(indexName = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "fname", length = 20, nullable = false)
    private String fname;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "sname", length = 20, nullable = false)
    private String sname;

    @Column(name = "gpa")
    private Double gpa;

    @NotNull
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private ZonedDateTime birthDate;

    @NotNull
    @Column(name = "expected_graduation_date", nullable = false)
    private Instant expectedGraduationDate;

    @ManyToOne
    private Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public Student fname(String fname) {
        this.fname = fname;
        return this;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public Student sname(String sname) {
        this.sname = sname;
        return this;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Double getGpa() {
        return gpa;
    }

    public Student gpa(Double gpa) {
        this.gpa = gpa;
        return this;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public Student enrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
        return this;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public ZonedDateTime getBirthDate() {
        return birthDate;
    }

    public Student birthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public Instant getExpectedGraduationDate() {
        return expectedGraduationDate;
    }

    public Student expectedGraduationDate(Instant expectedGraduationDate) {
        this.expectedGraduationDate = expectedGraduationDate;
        return this;
    }

    public void setExpectedGraduationDate(Instant expectedGraduationDate) {
        this.expectedGraduationDate = expectedGraduationDate;
    }

    public Department getDepartment() {
        return department;
    }

    public Student department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        if (student.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", fname='" + getFname() + "'" +
            ", sname='" + getSname() + "'" +
            ", gpa=" + getGpa() +
            ", enrollmentDate='" + getEnrollmentDate() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", expectedGraduationDate='" + getExpectedGraduationDate() + "'" +
            "}";
    }
}
