package com.ancestry.demoapp.repository;

import com.ancestry.demoapp.domain.AnimalSpecies;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AnimalSpecies entity.
 */
public interface AnimalSpeciesRepository extends JpaRepository<AnimalSpecies,Long> {

}
