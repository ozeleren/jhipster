package com.mycompany.myapp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Department.
 */
@Entity
@Table(name = "department")
@Document(indexName = "department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "dname", length = 20, nullable = false)
    private String dname;

    @NotNull
    @Column(name = "number_of_students", nullable = false)
    private Long numberOfStudents;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDname() {
        return dname;
    }

    public Department dname(String dname) {
        this.dname = dname;
        return this;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public Long getNumberOfStudents() {
        return numberOfStudents;
    }

    public Department numberOfStudents(Long numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
        return this;
    }

    public void setNumberOfStudents(Long numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
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
        Department department = (Department) o;
        if (department.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), department.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", dname='" + getDname() + "'" +
            ", numberOfStudents=" + getNumberOfStudents() +
            "}";
    }
}
