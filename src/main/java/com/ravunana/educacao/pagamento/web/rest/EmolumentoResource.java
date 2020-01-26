package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.service.EmolumentoService;
import com.ravunana.educacao.pagamento.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pagamento.service.dto.EmolumentoDTO;

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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.ravunana.educacao.pagamento.domain.Emolumento}.
 */
@RestController
@RequestMapping("/api")
public class EmolumentoResource {

    private final Logger log = LoggerFactory.getLogger(EmolumentoResource.class);

    private static final String ENTITY_NAME = "pagamentoEmolumento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmolumentoService emolumentoService;

    public EmolumentoResource(EmolumentoService emolumentoService) {
        this.emolumentoService = emolumentoService;
    }

    /**
     * {@code POST  /emolumentos} : Create a new emolumento.
     *
     * @param emolumentoDTO the emolumentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emolumentoDTO, or with status {@code 400 (Bad Request)} if the emolumento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/emolumentos")
    public ResponseEntity<EmolumentoDTO> createEmolumento(@Valid @RequestBody EmolumentoDTO emolumentoDTO) throws URISyntaxException {
        log.debug("REST request to save Emolumento : {}", emolumentoDTO);
        if (emolumentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new emolumento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmolumentoDTO result = emolumentoService.save(emolumentoDTO);
        return ResponseEntity.created(new URI("/api/emolumentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emolumentos} : Updates an existing emolumento.
     *
     * @param emolumentoDTO the emolumentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emolumentoDTO,
     * or with status {@code 400 (Bad Request)} if the emolumentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emolumentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/emolumentos")
    public ResponseEntity<EmolumentoDTO> updateEmolumento(@Valid @RequestBody EmolumentoDTO emolumentoDTO) throws URISyntaxException {
        log.debug("REST request to update Emolumento : {}", emolumentoDTO);
        if (emolumentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmolumentoDTO result = emolumentoService.save(emolumentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emolumentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /emolumentos} : get all the emolumentos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emolumentos in body.
     */
    @GetMapping("/emolumentos")
    public ResponseEntity<List<EmolumentoDTO>> getAllEmolumentos(Pageable pageable) {
        log.debug("REST request to get a page of Emolumentos");
        Page<EmolumentoDTO> page = emolumentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emolumentos/:id} : get the "id" emolumento.
     *
     * @param id the id of the emolumentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emolumentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/emolumentos/{id}")
    public ResponseEntity<EmolumentoDTO> getEmolumento(@PathVariable Long id) {
        log.debug("REST request to get Emolumento : {}", id);
        Optional<EmolumentoDTO> emolumentoDTO = emolumentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emolumentoDTO);
    }

    /**
     * {@code DELETE  /emolumentos/:id} : delete the "id" emolumento.
     *
     * @param id the id of the emolumentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/emolumentos/{id}")
    public ResponseEntity<Void> deleteEmolumento(@PathVariable Long id) {
        log.debug("REST request to delete Emolumento : {}", id);
        emolumentoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/emolumentos?query=:query} : search for the emolumento corresponding
     * to the query.
     *
     * @param query the query of the emolumento search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/emolumentos")
    public ResponseEntity<List<EmolumentoDTO>> searchEmolumentos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Emolumentos for query {}", query);
        Page<EmolumentoDTO> page = emolumentoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
