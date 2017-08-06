package com.thatconference.bacon.service;

import com.thatconference.bacon.domain.Bacon;
import com.thatconference.bacon.repository.BaconRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Bacon.
 */
@Service
@Transactional
public class BaconService {

    private final Logger log = LoggerFactory.getLogger(BaconService.class);

    private final BaconRepository baconRepository;

    public BaconService(BaconRepository baconRepository) {
        this.baconRepository = baconRepository;
    }

    /**
     * Save a bacon.
     *
     * @param bacon the entity to save
     * @return the persisted entity
     */
    public Bacon save(Bacon bacon) {
        log.debug("Request to save Bacon : {}", bacon);
        return baconRepository.save(bacon);
    }

    /**
     *  Get all the bacons.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Bacon> findAll() {
        log.debug("Request to get all Bacons");
        return baconRepository.findAll();
    }

    /**
     *  Get one bacon by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Bacon findOne(Long id) {
        log.debug("Request to get Bacon : {}", id);
        return baconRepository.findOne(id);
    }

    /**
     *  Delete the  bacon by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bacon : {}", id);
        baconRepository.delete(id);
    }
}
