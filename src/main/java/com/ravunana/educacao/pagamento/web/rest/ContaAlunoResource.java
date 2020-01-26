package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.service.ContaAlunoService;
import com.ravunana.educacao.pagamento.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pagamento.service.dto.ContaAlunoDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pagamento.domain.ContaAluno}.
 */
@RestController
@RequestMapping("/api")
public class ContaAlunoResource {

    private final Logger log = LoggerFactory.getLogger(ContaAlunoResource.class);

    private static final String ENTITY_NAME = "pagamentoContaAluno";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContaAlunoService contaAlunoService;

    public ContaAlunoResource(ContaAlunoService contaAlunoService) {
        this.contaAlunoService = contaAlunoService;
    }

    /**
     * {@code POST  /conta-alunos} : Create a new contaAluno.
     *
     * @param contaAlunoDTO the contaAlunoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contaAlunoDTO, or with status {@code 400 (Bad Request)} if the contaAluno has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conta-alunos")
    public ResponseEntity<ContaAlunoDTO> createContaAluno(@Valid @RequestBody ContaAlunoDTO contaAlunoDTO) throws URISyntaxException {
        log.debug("REST request to save ContaAluno : {}", contaAlunoDTO);
        if (contaAlunoDTO.getId() != null) {
            throw new BadRequestAlertException("A new contaAluno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContaAlunoDTO result = contaAlunoService.save(contaAlunoDTO);
        return ResponseEntity.created(new URI("/api/conta-alunos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conta-alunos} : Updates an existing contaAluno.
     *
     * @param contaAlunoDTO the contaAlunoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaAlunoDTO,
     * or with status {@code 400 (Bad Request)} if the contaAlunoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contaAlunoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conta-alunos")
    public ResponseEntity<ContaAlunoDTO> updateContaAluno(@Valid @RequestBody ContaAlunoDTO contaAlunoDTO) throws URISyntaxException {
        log.debug("REST request to update ContaAluno : {}", contaAlunoDTO);
        if (contaAlunoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContaAlunoDTO result = contaAlunoService.save(contaAlunoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contaAlunoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /conta-alunos} : get all the contaAlunos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contaAlunos in body.
     */
    @GetMapping("/conta-alunos")
    public ResponseEntity<List<ContaAlunoDTO>> getAllContaAlunos(Pageable pageable) {
        log.debug("REST request to get a page of ContaAlunos");
        Page<ContaAlunoDTO> page = contaAlunoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conta-alunos/:id} : get the "id" contaAluno.
     *
     * @param id the id of the contaAlunoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contaAlunoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conta-alunos/{id}")
    public ResponseEntity<ContaAlunoDTO> getContaAluno(@PathVariable Long id) {
        log.debug("REST request to get ContaAluno : {}", id);
        Optional<ContaAlunoDTO> contaAlunoDTO = contaAlunoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contaAlunoDTO);
    }

    /**
     * {@code DELETE  /conta-alunos/:id} : delete the "id" contaAluno.
     *
     * @param id the id of the contaAlunoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conta-alunos/{id}")
    public ResponseEntity<Void> deleteContaAluno(@PathVariable Long id) {
        log.debug("REST request to delete ContaAluno : {}", id);
        contaAlunoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/conta-alunos?query=:query} : search for the contaAluno corresponding
     * to the query.
     *
     * @param query the query of the contaAluno search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/conta-alunos")
    public ResponseEntity<List<ContaAlunoDTO>> searchContaAlunos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ContaAlunos for query {}", query);
        Page<ContaAlunoDTO> page = contaAlunoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
