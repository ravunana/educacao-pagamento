package com.ravunana.educacao.pagamento.service;

import com.ravunana.educacao.pagamento.domain.EfeitoPagamento;
import com.ravunana.educacao.pagamento.repository.EfeitoPagamentoRepository;
import com.ravunana.educacao.pagamento.repository.search.EfeitoPagamentoSearchRepository;
import com.ravunana.educacao.pagamento.service.dto.EfeitoPagamentoDTO;
import com.ravunana.educacao.pagamento.service.mapper.EfeitoPagamentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link EfeitoPagamento}.
 */
@Service
@Transactional
public class EfeitoPagamentoService {

    private final Logger log = LoggerFactory.getLogger(EfeitoPagamentoService.class);

    private final EfeitoPagamentoRepository efeitoPagamentoRepository;

    private final EfeitoPagamentoMapper efeitoPagamentoMapper;

    private final EfeitoPagamentoSearchRepository efeitoPagamentoSearchRepository;

    public EfeitoPagamentoService(EfeitoPagamentoRepository efeitoPagamentoRepository, EfeitoPagamentoMapper efeitoPagamentoMapper, EfeitoPagamentoSearchRepository efeitoPagamentoSearchRepository) {
        this.efeitoPagamentoRepository = efeitoPagamentoRepository;
        this.efeitoPagamentoMapper = efeitoPagamentoMapper;
        this.efeitoPagamentoSearchRepository = efeitoPagamentoSearchRepository;
    }

    /**
     * Save a efeitoPagamento.
     *
     * @param efeitoPagamentoDTO the entity to save.
     * @return the persisted entity.
     */
    public EfeitoPagamentoDTO save(EfeitoPagamentoDTO efeitoPagamentoDTO) {
        log.debug("Request to save EfeitoPagamento : {}", efeitoPagamentoDTO);
        EfeitoPagamento efeitoPagamento = efeitoPagamentoMapper.toEntity(efeitoPagamentoDTO);
        efeitoPagamento = efeitoPagamentoRepository.save(efeitoPagamento);
        EfeitoPagamentoDTO result = efeitoPagamentoMapper.toDto(efeitoPagamento);
        efeitoPagamentoSearchRepository.save(efeitoPagamento);
        return result;
    }

    /**
     * Get all the efeitoPagamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EfeitoPagamentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EfeitoPagamentos");
        return efeitoPagamentoRepository.findAll(pageable)
            .map(efeitoPagamentoMapper::toDto);
    }

    /**
     * Get all the efeitoPagamentos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EfeitoPagamentoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return efeitoPagamentoRepository.findAllWithEagerRelationships(pageable).map(efeitoPagamentoMapper::toDto);
    }
    

    /**
     * Get one efeitoPagamento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EfeitoPagamentoDTO> findOne(Long id) {
        log.debug("Request to get EfeitoPagamento : {}", id);
        return efeitoPagamentoRepository.findOneWithEagerRelationships(id)
            .map(efeitoPagamentoMapper::toDto);
    }

    /**
     * Delete the efeitoPagamento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EfeitoPagamento : {}", id);
        efeitoPagamentoRepository.deleteById(id);
        efeitoPagamentoSearchRepository.deleteById(id);
    }

    /**
     * Search for the efeitoPagamento corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EfeitoPagamentoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EfeitoPagamentos for query {}", query);
        return efeitoPagamentoSearchRepository.search(queryStringQuery(query), pageable)
            .map(efeitoPagamentoMapper::toDto);
    }
}
