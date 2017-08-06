package com.thatconference.bacon.repository;

import com.thatconference.bacon.domain.Bacon;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Bacon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaconRepository extends JpaRepository<Bacon,Long> {
    
}
