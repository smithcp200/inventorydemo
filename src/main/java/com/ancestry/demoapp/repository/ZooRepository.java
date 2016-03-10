package com.ancestry.demoapp.repository;

import com.ancestry.demoapp.domain.Zoo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Zoo entity.
 */
public interface ZooRepository extends JpaRepository<Zoo,Long> {

}
