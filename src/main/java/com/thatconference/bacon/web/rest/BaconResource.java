package com.thatconference.bacon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.thatconference.bacon.domain.Bacon;
import com.thatconference.bacon.service.BaconService;
import com.thatconference.bacon.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Bacon.
 */
@RestController
@RequestMapping("/api")
public class BaconResource {

    private final Logger log = LoggerFactory.getLogger(BaconResource.class);

    private static final String ENTITY_NAME = "bacon";

    private final BaconService baconService;

    public BaconResource(BaconService baconService) {
        this.baconService = baconService;
    }

    /**
     * POST  /bacons : Create a new bacon.
     *
     * @param bacon the bacon to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bacon, or with status 400 (Bad Request) if the bacon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bacons")
    @Timed
    public ResponseEntity<Bacon> createBacon(@Valid @RequestBody Bacon bacon) throws URISyntaxException {
        log.debug("REST request to save Bacon : {}", bacon);
        if (bacon.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bacon cannot already have an ID")).body(null);
        }
        Bacon result = baconService.save(bacon);
        return ResponseEntity.created(new URI("/api/bacons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bacons : Updates an existing bacon.
     *
     * @param bacon the bacon to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bacon,
     * or with status 400 (Bad Request) if the bacon is not valid,
     * or with status 500 (Internal Server Error) if the bacon couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bacons")
    @Timed
    public ResponseEntity<Bacon> updateBacon(@Valid @RequestBody Bacon bacon) throws URISyntaxException {
        log.debug("REST request to update Bacon : {}", bacon);
        if (bacon.getId() == null) {
            return createBacon(bacon);
        }
        Bacon result = baconService.save(bacon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bacon.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bacons : get all the bacons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bacons in body
     */
    @GetMapping("/bacons")
    @Timed
    public List<Bacon> getAllBacons() {
        log.debug("REST request to get all Bacons");
        return baconService.findAll();
    }

    /**
     * GET  /bacons/:id : get the "id" bacon.
     *
     * @param id the id of the bacon to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bacon, or with status 404 (Not Found)
     */
    @GetMapping("/bacons/{id}")
    @Timed
    public ResponseEntity<Bacon> getBacon(@PathVariable Long id) {
        log.debug("REST request to get Bacon : {}", id);
        Bacon bacon = baconService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bacon));
    }

    /**
     * DELETE  /bacons/:id : delete the "id" bacon.
     *
     * @param id the id of the bacon to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bacons/{id}")
    @Timed
    public ResponseEntity<Void> deleteBacon(@PathVariable Long id) {
        log.debug("REST request to delete Bacon : {}", id);
        baconService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
