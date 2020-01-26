package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.service.PagamentoEmolumentoService;
import com.ravunana.educacao.pagamento.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pagamento.service.dto.PagamentoEmolumentoDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pagamento.domain.PagamentoEmolumento}.
 */
@RestController
@RequestMapping("/api")
public class PagamentoEmolumentoResource {

    private final Logger log = LoggerFactory.getLogger(PagamentoEmolumentoResource.class);

    private static final String ENTITY_NAME = "pagamentoPagamentoEmolumento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PagamentoEmolumentoService pagamentoEmolumentoService;

    public PagamentoEmolumentoResource(PagamentoEmolumentoService pagamentoEmolumentoService) {
        this.pagamentoEmolumentoService = pagamentoEmolumentoService;
    }

    /**
     * {@code POST  /pagamento-emolumentos} : Create a new pagamentoEmolumento.
     *
     * @param pagamentoEmolumentoDTO the pagamentoEmolumentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pagamentoEmolumentoDTO, or with status {@code 400 (Bad Request)} if the pagamentoEmolumento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pagamento-emolumentos")
    public ResponseEntity<PagamentoEmolumentoDTO> createPagamentoEmolumento(@Valid @RequestBody PagamentoEmolumentoDTO pagamentoEmolumentoDTO) throws URISyntaxException {
        log.debug("REST request to save PagamentoEmolumento : {}", pagamentoEmolumentoDTO);
        if (pagamentoEmolumentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new pagamentoEmolumento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PagamentoEmolumentoDTO result = pagamentoEmolumentoService.save(pagamentoEmolumentoDTO);
        return ResponseEntity.created(new URI("/api/pagamento-emolumentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pagamento-emolumentos} : Updates an existing pagamentoEmolumento.
     *
     * @param pagamentoEmolumentoDTO the pagamentoEmolumentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagamentoEmolumentoDTO,
     * or with status {@code 400 (Bad Request)} if the pagamentoEmolumentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pagamentoEmolumentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pagamento-emolumentos")
    public ResponseEntity<PagamentoEmolumentoDTO> updatePagamentoEmolumento(@Valid @RequestBody PagamentoEmolumentoDTO pagamentoEmolumentoDTO) throws URISyntaxException {
        log.debug("REST request to update PagamentoEmolumento : {}", pagamentoEmolumentoDTO);
        if (pagamentoEmolumentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PagamentoEmolumentoDTO result = pagamentoEmolumentoService.save(pagamentoEmolumentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pagamentoEmolumentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pagamento-emolumentos} : get all the pagamentoEmolumentos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pagamentoEmolumentos in body.
     */
    @GetMapping("/pagamento-emolumentos")
    public ResponseEntity<List<PagamentoEmolumentoDTO>> getAllPagamentoEmolumentos(Pageable pageable) {
        log.debug("REST request to get a page of PagamentoEmolumentos");
        Page<PagamentoEmolumentoDTO> page = pagamentoEmolumentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pagamento-emolumentos/:id} : get the "id" pagamentoEmolumento.
     *
     * @param id the id of the pagamentoEmolumentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pagamentoEmolumentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pagamento-emolumentos/{id}")
    public ResponseEntity<PagamentoEmolumentoDTO> getPagamentoEmolumento(@PathVariable Long id) {
        log.debug("REST request to get PagamentoEmolumento : {}", id);
        Optional<PagamentoEmolumentoDTO> pagamentoEmolumentoDTO = pagamentoEmolumentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pagamentoEmolumentoDTO);
    }

    /**
     * {@code DELETE  /pagamento-emolumentos/:id} : delete the "id" pagamentoEmolumento.
     *
     * @param id the id of the pagamentoEmolumentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pagamento-emolumentos/{id}")
    public ResponseEntity<Void> deletePagamentoEmolumento(@PathVariable Long id) {
        log.debug("REST request to delete PagamentoEmolumento : {}", id);
        pagamentoEmolumentoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/pagamento-emolumentos?query=:query} : search for the pagamentoEmolumento corresponding
     * to the query.
     *
     * @param query the query of the pagamentoEmolumento search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/pagamento-emolumentos")
    public ResponseEntity<List<PagamentoEmolumentoDTO>> searchPagamentoEmolumentos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PagamentoEmolumentos for query {}", query);
        Page<PagamentoEmolumentoDTO> page = pagamentoEmolumentoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
