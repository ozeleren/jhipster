package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Take;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Take entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TakeRepository extends JpaRepository<Take, Long> {

}
