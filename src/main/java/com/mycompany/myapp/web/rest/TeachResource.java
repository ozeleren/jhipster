package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Teach;

import com.mycompany.myapp.repository.TeachRepository;
import com.mycompany.myapp.repository.search.TeachSearchRepository;
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
 * REST controller for managing Teach.
 */
@RestController
@RequestMapping("/api")
public class TeachResource {

    private final Logger log = LoggerFactory.getLogger(TeachResource.class);

    private static final String ENTITY_NAME = "teach";

    private final TeachRepository teachRepository;

    private final TeachSearchRepository teachSearchRepository;

    public TeachResource(TeachRepository teachRepository, TeachSearchRepository teachSearchRepository) {
        this.teachRepository = teachRepository;
        this.teachSearchRepository = teachSearchRepository;
    }

    /**
     * POST  /teaches : Create a new teach.
     *
     * @param teach the teach to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teach, or with status 400 (Bad Request) if the teach has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/teaches")
    @Timed
    public ResponseEntity<Teach> createTeach(@RequestBody Teach teach) throws URISyntaxException {
        log.debug("REST request to save Teach : {}", teach);
        if (teach.getId() != null) {
            throw new BadRequestAlertException("A new teach cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Teach result = teachRepository.save(teach);
        teachSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/teaches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /teaches : Updates an existing teach.
     *
     * @param teach the teach to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teach,
     * or with status 400 (Bad Request) if the teach is not valid,
     * or with status 500 (Internal Server Error) if the teach couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/teaches")
    @Timed
    public ResponseEntity<Teach> updateTeach(@RequestBody Teach teach) throws URISyntaxException {
        log.debug("REST request to update Teach : {}", teach);
        if (teach.getId() == null) {
            return createTeach(teach);
        }
        Teach result = teachRepository.save(teach);
        teachSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, teach.getId().toString()))
            .body(result);
    }

    /**
     * GET  /teaches : get all the teaches.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of teaches in body
     */
    @GetMapping("/teaches")
    @Timed
    public ResponseEntity<List<Teach>> getAllTeaches(Pageable pageable) {
        log.debug("REST request to get a page of Teaches");
        Page<Teach> page = teachRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/teaches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /teaches/:id : get the "id" teach.
     *
     * @param id the id of the teach to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teach, or with status 404 (Not Found)
     */
    @GetMapping("/teaches/{id}")
    @Timed
    public ResponseEntity<Teach> getTeach(@PathVariable Long id) {
        log.debug("REST request to get Teach : {}", id);
        Teach teach = teachRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(teach));
    }

    /**
     * DELETE  /teaches/:id : delete the "id" teach.
     *
     * @param id the id of the teach to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/teaches/{id}")
    @Timed
    public ResponseEntity<Void> deleteTeach(@PathVariable Long id) {
        log.debug("REST request to delete Teach : {}", id);
        teachRepository.delete(id);
        teachSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/teaches?query=:query : search for the teach corresponding
     * to the query.
     *
     * @param query the query of the teach search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/teaches")
    @Timed
    public ResponseEntity<List<Teach>> searchTeaches(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Teaches for query {}", query);
        Page<Teach> page = teachSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/teaches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
