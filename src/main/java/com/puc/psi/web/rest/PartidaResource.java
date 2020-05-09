package com.puc.psi.web.rest;

import com.puc.psi.domain.Partida;
import com.puc.psi.repository.PartidaRepository;
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
 * REST controller for managing {@link com.puc.psi.domain.Partida}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PartidaResource {

    private final Logger log = LoggerFactory.getLogger(PartidaResource.class);

    private static final String ENTITY_NAME = "partida";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartidaRepository partidaRepository;

    public PartidaResource(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    /**
     * {@code POST  /partidas} : Create a new partida.
     *
     * @param partida the partida to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partida, or with status {@code 400 (Bad Request)} if the partida has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/partidas")
    public ResponseEntity<Partida> createPartida(@RequestBody Partida partida) throws URISyntaxException {
        log.debug("REST request to save Partida : {}", partida);
        if (partida.getId() != null) {
            throw new BadRequestAlertException("A new partida cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Partida result = partidaRepository.save(partida);
        return ResponseEntity.created(new URI("/api/partidas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /partidas} : Updates an existing partida.
     *
     * @param partida the partida to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partida,
     * or with status {@code 400 (Bad Request)} if the partida is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partida couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/partidas")
    public ResponseEntity<Partida> updatePartida(@RequestBody Partida partida) throws URISyntaxException {
        log.debug("REST request to update Partida : {}", partida);
        if (partida.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Partida result = partidaRepository.save(partida);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partida.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /partidas} : get all the partidas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partidas in body.
     */
    @GetMapping("/partidas")
    public ResponseEntity<List<Partida>> getAllPartidas(Pageable pageable) {
        log.debug("REST request to get a page of Partidas");
        Page<Partida> page = partidaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /partidas/:id} : get the "id" partida.
     *
     * @param id the id of the partida to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partida, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/partidas/{id}")
    public ResponseEntity<Partida> getPartida(@PathVariable Long id) {
        log.debug("REST request to get Partida : {}", id);
        Optional<Partida> partida = partidaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(partida);
    }

    /**
     * {@code DELETE  /partidas/:id} : delete the "id" partida.
     *
     * @param id the id of the partida to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/partidas/{id}")
    public ResponseEntity<Void> deletePartida(@PathVariable Long id) {
        log.debug("REST request to delete Partida : {}", id);
        partidaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
