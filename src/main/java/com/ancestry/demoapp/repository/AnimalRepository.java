package com.ancestry.demoapp.repository;

import com.ancestry.demoapp.domain.Animal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Animal entity.
 */
public interface AnimalRepository extends JpaRepository<Animal,Long> {

}
