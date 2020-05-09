package com.puc.psi.web.rest;

import com.puc.psi.domain.Campeonato;
import com.puc.psi.repository.CampeonatoRepository;
import com.puc.psi.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.puc.psi.domain.Campeonato}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CampeonatoResource {

    private final Logger log = LoggerFactory.getLogger(CampeonatoResource.class);

    private static final String ENTITY_NAME = "campeonato";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CampeonatoRepository campeonatoRepository;

    public CampeonatoResource(CampeonatoRepository campeonatoRepository) {
        this.campeonatoRepository = campeonatoRepository;
    }

    /**
     * {@code POST  /campeonatoes} : Create a new campeonato.
     *
     * @param campeonato the campeonato to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new campeonato, or with status {@code 400 (Bad Request)} if the campeonato has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/campeonatoes")
    public ResponseEntity<Campeonato> createCampeonato(@RequestBody Campeonato campeonato) throws URISyntaxException {
        log.debug("REST request to save Campeonato : {}", campeonato);
        if (campeonato.getId() != null) {
            throw new BadRequestAlertException("A new campeonato cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Campeonato result = campeonatoRepository.save(campeonato);
        return ResponseEntity.created(new URI("/api/campeonatoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /campeonatoes} : Updates an existing campeonato.
     *
     * @param campeonato the campeonato to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campeonato,
     * or with status {@code 400 (Bad Request)} if the campeonato is not valid,
     * or with status {@code 500 (Internal Server Error)} if the campeonato couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/campeonatoes")
    public ResponseEntity<Campeonato> updateCampeonato(@RequestBody Campeonato campeonato) throws URISyntaxException {
        log.debug("REST request to update Campeonato : {}", campeonato);
        if (campeonato.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Campeonato result = campeonatoRepository.save(campeonato);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, campeonato.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /campeonatoes} : get all the campeonatoes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of campeonatoes in body.
     */
    @GetMapping("/campeonatoes")
    public ResponseEntity<List<Campeonato>> getAllCampeonatoes(Pageable pageable) {
        log.debug("REST request to get a page of Campeonatoes");
        Page<Campeonato> page = campeonatoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /campeonatoes/:id} : get the "id" campeonato.
     *
     * @param id the id of the campeonato to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the campeonato, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/campeonatoes/{id}")
    public ResponseEntity<Campeonato> getCampeonato(@PathVariable Long id) {
        log.debug("REST request to get Campeonato : {}", id);
        Optional<Campeonato> campeonato = campeonatoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(campeonato);
    }

    /**
     * {@code DELETE  /campeonatoes/:id} : delete the "id" campeonato.
     *
     * @param id the id of the campeonato to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/campeonatoes/{id}")
    public ResponseEntity<Void> deleteCampeonato(@PathVariable Long id) {
        log.debug("REST request to delete Campeonato : {}", id);
        campeonatoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
