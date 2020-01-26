package com.ravunana.educacao.pagamento.service;

import com.ravunana.educacao.pagamento.domain.PagamentoEmolumento;
import com.ravunana.educacao.pagamento.repository.PagamentoEmolumentoRepository;
import com.ravunana.educacao.pagamento.repository.search.PagamentoEmolumentoSearchRepository;
import com.ravunana.educacao.pagamento.service.dto.PagamentoEmolumentoDTO;
import com.ravunana.educacao.pagamento.service.mapper.PagamentoEmolumentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PagamentoEmolumento}.
 */
@Service
@Transactional
public class PagamentoEmolumentoService {

    private final Logger log = LoggerFactory.getLogger(PagamentoEmolumentoService.class);

    private final PagamentoEmolumentoRepository pagamentoEmolumentoRepository;

    private final PagamentoEmolumentoMapper pagamentoEmolumentoMapper;

    private final PagamentoEmolumentoSearchRepository pagamentoEmolumentoSearchRepository;

    public PagamentoEmolumentoService(PagamentoEmolumentoRepository pagamentoEmolumentoRepository, PagamentoEmolumentoMapper pagamentoEmolumentoMapper, PagamentoEmolumentoSearchRepository pagamentoEmolumentoSearchRepository) {
        this.pagamentoEmolumentoRepository = pagamentoEmolumentoRepository;
        this.pagamentoEmolumentoMapper = pagamentoEmolumentoMapper;
        this.pagamentoEmolumentoSearchRepository = pagamentoEmolumentoSearchRepository;
    }

    /**
     * Save a pagamentoEmolumento.
     *
     * @param pagamentoEmolumentoDTO the entity to save.
     * @return the persisted entity.
     */
    public PagamentoEmolumentoDTO save(PagamentoEmolumentoDTO pagamentoEmolumentoDTO) {
        log.debug("Request to save PagamentoEmolumento : {}", pagamentoEmolumentoDTO);
        PagamentoEmolumento pagamentoEmolumento = pagamentoEmolumentoMapper.toEntity(pagamentoEmolumentoDTO);
        pagamentoEmolumento = pagamentoEmolumentoRepository.save(pagamentoEmolumento);
        PagamentoEmolumentoDTO result = pagamentoEmolumentoMapper.toDto(pagamentoEmolumento);
        pagamentoEmolumentoSearchRepository.save(pagamentoEmolumento);
        return result;
    }

    /**
     * Get all the pagamentoEmolumentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PagamentoEmolumentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PagamentoEmolumentos");
        return pagamentoEmolumentoRepository.findAll(pageable)
            .map(pagamentoEmolumentoMapper::toDto);
    }


    /**
     * Get one pagamentoEmolumento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PagamentoEmolumentoDTO> findOne(Long id) {
        log.debug("Request to get PagamentoEmolumento : {}", id);
        return pagamentoEmolumentoRepository.findById(id)
            .map(pagamentoEmolumentoMapper::toDto);
    }

    /**
     * Delete the pagamentoEmolumento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PagamentoEmolumento : {}", id);
        pagamentoEmolumentoRepository.deleteById(id);
        pagamentoEmolumentoSearchRepository.deleteById(id);
    }

    /**
     * Search for the pagamentoEmolumento corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PagamentoEmolumentoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PagamentoEmolumentos for query {}", query);
        return pagamentoEmolumentoSearchRepository.search(queryStringQuery(query), pageable)
            .map(pagamentoEmolumentoMapper::toDto);
    }
}
