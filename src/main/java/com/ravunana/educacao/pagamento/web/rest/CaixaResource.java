package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.service.CaixaService;
import com.ravunana.educacao.pagamento.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pagamento.service.dto.CaixaDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pagamento.domain.Caixa}.
 */
@RestController
@RequestMapping("/api")
public class CaixaResource {

    private final Logger log = LoggerFactory.getLogger(CaixaResource.class);

    private static final String ENTITY_NAME = "pagamentoCaixa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaixaService caixaService;

    public CaixaResource(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    /**
     * {@code POST  /caixas} : Create a new caixa.
     *
     * @param caixaDTO the caixaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caixaDTO, or with status {@code 400 (Bad Request)} if the caixa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/caixas")
    public ResponseEntity<CaixaDTO> createCaixa(@Valid @RequestBody CaixaDTO caixaDTO) throws URISyntaxException {
        log.debug("REST request to save Caixa : {}", caixaDTO);
        if (caixaDTO.getId() != null) {
            throw new BadRequestAlertException("A new caixa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CaixaDTO result = caixaService.save(caixaDTO);
        return ResponseEntity.created(new URI("/api/caixas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /caixas} : Updates an existing caixa.
     *
     * @param caixaDTO the caixaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caixaDTO,
     * or with status {@code 400 (Bad Request)} if the caixaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caixaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/caixas")
    public ResponseEntity<CaixaDTO> updateCaixa(@Valid @RequestBody CaixaDTO caixaDTO) throws URISyntaxException {
        log.debug("REST request to update Caixa : {}", caixaDTO);
        if (caixaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CaixaDTO result = caixaService.save(caixaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caixaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /caixas} : get all the caixas.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caixas in body.
     */
    @GetMapping("/caixas")
    public ResponseEntity<List<CaixaDTO>> getAllCaixas(Pageable pageable) {
        log.debug("REST request to get a page of Caixas");
        Page<CaixaDTO> page = caixaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /caixas/:id} : get the "id" caixa.
     *
     * @param id the id of the caixaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caixaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/caixas/{id}")
    public ResponseEntity<CaixaDTO> getCaixa(@PathVariable Long id) {
        log.debug("REST request to get Caixa : {}", id);
        Optional<CaixaDTO> caixaDTO = caixaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(caixaDTO);
    }

    /**
     * {@code DELETE  /caixas/:id} : delete the "id" caixa.
     *
     * @param id the id of the caixaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/caixas/{id}")
    public ResponseEntity<Void> deleteCaixa(@PathVariable Long id) {
        log.debug("REST request to delete Caixa : {}", id);
        caixaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/caixas?query=:query} : search for the caixa corresponding
     * to the query.
     *
     * @param query the query of the caixa search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/caixas")
    public ResponseEntity<List<CaixaDTO>> searchCaixas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Caixas for query {}", query);
        Page<CaixaDTO> page = caixaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
