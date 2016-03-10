package com.ancestry.demoapp.repository;

import com.ancestry.demoapp.domain.Feeding;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Feeding entity.
 */
public interface FeedingRepository extends JpaRepository<Feeding,Long> {

}
