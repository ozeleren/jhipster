package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Take;

import com.mycompany.myapp.repository.TakeRepository;
import com.mycompany.myapp.repository.search.TakeSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Take.
 */
@RestController
@RequestMapping("/api")
public class TakeResource {

    private final Logger log = LoggerFactory.getLogger(TakeResource.class);

    private static final String ENTITY_NAME = "take";

    private final TakeRepository takeRepository;

    private final TakeSearchRepository takeSearchRepository;

    public TakeResource(TakeRepository takeRepository, TakeSearchRepository takeSearchRepository) {
        this.takeRepository = takeRepository;
        this.takeSearchRepository = takeSearchRepository;
    }

    /**
     * POST  /takes : Create a new take.
     *
     * @param take the take to create
     * @return the ResponseEntity with status 201 (Created) and with body the new take, or with status 400 (Bad Request) if the take has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/takes")
    @Timed
    public ResponseEntity<Take> createTake(@RequestBody Take take) throws URISyntaxException {
        log.debug("REST request to save Take : {}", take);
        if (take.getId() != null) {
            throw new BadRequestAlertException("A new take cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Take result = takeRepository.save(take);
        takeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/takes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /takes : Updates an existing take.
     *
     * @param take the take to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated take,
     * or with status 400 (Bad Request) if the take is not valid,
     * or with status 500 (Internal Server Error) if the take couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/takes")
    @Timed
    public ResponseEntity<Take> updateTake(@RequestBody Take take) throws URISyntaxException {
        log.debug("REST request to update Take : {}", take);
        if (take.getId() == null) {
            return createTake(take);
        }
        Take result = takeRepository.save(take);
        takeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, take.getId().toString()))
            .body(result);
    }

    /**
     * GET  /takes : get all the takes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of takes in body
     */
    @GetMapping("/takes")
    @Timed
    public ResponseEntity<List<Take>> getAllTakes(Pageable pageable) {
        log.debug("REST request to get a page of Takes");
        Page<Take> page = takeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/takes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /takes/:id : get the "id" take.
     *
     * @param id the id of the take to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the take, or with status 404 (Not Found)
     */
    @GetMapping("/takes/{id}")
    @Timed
    public ResponseEntity<Take> getTake(@PathVariable Long id) {
        log.debug("REST request to get Take : {}", id);
        Take take = takeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(take));
    }

    /**
     * DELETE  /takes/:id : delete the "id" take.
     *
     * @param id the id of the take to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/takes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTake(@PathVariable Long id) {
        log.debug("REST request to delete Take : {}", id);
        takeRepository.delete(id);
        takeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/takes?query=:query : search for the take corresponding
     * to the query.
     *
     * @param query the query of the take search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/takes")
    @Timed
    public ResponseEntity<List<Take>> searchTakes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Takes for query {}", query);
        Page<Take> page = takeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/takes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
