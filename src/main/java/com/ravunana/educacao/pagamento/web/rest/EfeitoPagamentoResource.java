package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.service.EfeitoPagamentoService;
import com.ravunana.educacao.pagamento.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pagamento.service.dto.EfeitoPagamentoDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pagamento.domain.EfeitoPagamento}.
 */
@RestController
@RequestMapping("/api")
public class EfeitoPagamentoResource {

    private final Logger log = LoggerFactory.getLogger(EfeitoPagamentoResource.class);

    private static final String ENTITY_NAME = "pagamentoEfeitoPagamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EfeitoPagamentoService efeitoPagamentoService;

    public EfeitoPagamentoResource(EfeitoPagamentoService efeitoPagamentoService) {
        this.efeitoPagamentoService = efeitoPagamentoService;
    }

    /**
     * {@code POST  /efeito-pagamentos} : Create a new efeitoPagamento.
     *
     * @param efeitoPagamentoDTO the efeitoPagamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new efeitoPagamentoDTO, or with status {@code 400 (Bad Request)} if the efeitoPagamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/efeito-pagamentos")
    public ResponseEntity<EfeitoPagamentoDTO> createEfeitoPagamento(@Valid @RequestBody EfeitoPagamentoDTO efeitoPagamentoDTO) throws URISyntaxException {
        log.debug("REST request to save EfeitoPagamento : {}", efeitoPagamentoDTO);
        if (efeitoPagamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new efeitoPagamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EfeitoPagamentoDTO result = efeitoPagamentoService.save(efeitoPagamentoDTO);
        return ResponseEntity.created(new URI("/api/efeito-pagamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /efeito-pagamentos} : Updates an existing efeitoPagamento.
     *
     * @param efeitoPagamentoDTO the efeitoPagamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated efeitoPagamentoDTO,
     * or with status {@code 400 (Bad Request)} if the efeitoPagamentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the efeitoPagamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/efeito-pagamentos")
    public ResponseEntity<EfeitoPagamentoDTO> updateEfeitoPagamento(@Valid @RequestBody EfeitoPagamentoDTO efeitoPagamentoDTO) throws URISyntaxException {
        log.debug("REST request to update EfeitoPagamento : {}", efeitoPagamentoDTO);
        if (efeitoPagamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EfeitoPagamentoDTO result = efeitoPagamentoService.save(efeitoPagamentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, efeitoPagamentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /efeito-pagamentos} : get all the efeitoPagamentos.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of efeitoPagamentos in body.
     */
    @GetMapping("/efeito-pagamentos")
    public ResponseEntity<List<EfeitoPagamentoDTO>> getAllEfeitoPagamentos(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of EfeitoPagamentos");
        Page<EfeitoPagamentoDTO> page;
        if (eagerload) {
            page = efeitoPagamentoService.findAllWithEagerRelationships(pageable);
        } else {
            page = efeitoPagamentoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /efeito-pagamentos/:id} : get the "id" efeitoPagamento.
     *
     * @param id the id of the efeitoPagamentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the efeitoPagamentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/efeito-pagamentos/{id}")
    public ResponseEntity<EfeitoPagamentoDTO> getEfeitoPagamento(@PathVariable Long id) {
        log.debug("REST request to get EfeitoPagamento : {}", id);
        Optional<EfeitoPagamentoDTO> efeitoPagamentoDTO = efeitoPagamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(efeitoPagamentoDTO);
    }

    /**
     * {@code DELETE  /efeito-pagamentos/:id} : delete the "id" efeitoPagamento.
     *
     * @param id the id of the efeitoPagamentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/efeito-pagamentos/{id}")
    public ResponseEntity<Void> deleteEfeitoPagamento(@PathVariable Long id) {
        log.debug("REST request to delete EfeitoPagamento : {}", id);
        efeitoPagamentoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/efeito-pagamentos?query=:query} : search for the efeitoPagamento corresponding
     * to the query.
     *
     * @param query the query of the efeitoPagamento search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/efeito-pagamentos")
    public ResponseEntity<List<EfeitoPagamentoDTO>> searchEfeitoPagamentos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of EfeitoPagamentos for query {}", query);
        Page<EfeitoPagamentoDTO> page = efeitoPagamentoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
