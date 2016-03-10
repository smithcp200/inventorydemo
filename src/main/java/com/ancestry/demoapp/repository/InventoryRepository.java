package com.ancestry.demoapp.repository;

import com.ancestry.demoapp.domain.Inventory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Inventory entity.
 */
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

}
