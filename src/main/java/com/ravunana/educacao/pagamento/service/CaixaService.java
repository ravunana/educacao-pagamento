package com.ravunana.educacao.pagamento.service;

import com.ravunana.educacao.pagamento.domain.Caixa;
import com.ravunana.educacao.pagamento.repository.CaixaRepository;
import com.ravunana.educacao.pagamento.repository.search.CaixaSearchRepository;
import com.ravunana.educacao.pagamento.service.dto.CaixaDTO;
import com.ravunana.educacao.pagamento.service.mapper.CaixaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Caixa}.
 */
@Service
@Transactional
public class CaixaService {

    private final Logger log = LoggerFactory.getLogger(CaixaService.class);

    private final CaixaRepository caixaRepository;

    private final CaixaMapper caixaMapper;

    private final CaixaSearchRepository caixaSearchRepository;

    public CaixaService(CaixaRepository caixaRepository, CaixaMapper caixaMapper, CaixaSearchRepository caixaSearchRepository) {
        this.caixaRepository = caixaRepository;
        this.caixaMapper = caixaMapper;
        this.caixaSearchRepository = caixaSearchRepository;
    }

    /**
     * Save a caixa.
     *
     * @param caixaDTO the entity to save.
     * @return the persisted entity.
     */
    public CaixaDTO save(CaixaDTO caixaDTO) {
        log.debug("Request to save Caixa : {}", caixaDTO);
        Caixa caixa = caixaMapper.toEntity(caixaDTO);
        caixa = caixaRepository.save(caixa);
        CaixaDTO result = caixaMapper.toDto(caixa);
        caixaSearchRepository.save(caixa);
        return result;
    }

    /**
     * Get all the caixas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CaixaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Caixas");
        return caixaRepository.findAll(pageable)
            .map(caixaMapper::toDto);
    }


    /**
     * Get one caixa by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CaixaDTO> findOne(Long id) {
        log.debug("Request to get Caixa : {}", id);
        return caixaRepository.findById(id)
            .map(caixaMapper::toDto);
    }

    /**
     * Delete the caixa by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Caixa : {}", id);
        caixaRepository.deleteById(id);
        caixaSearchRepository.deleteById(id);
    }

    /**
     * Search for the caixa corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CaixaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Caixas for query {}", query);
        return caixaSearchRepository.search(queryStringQuery(query), pageable)
            .map(caixaMapper::toDto);
    }
}
