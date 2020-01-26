package com.ravunana.educacao.pagamento.service;

import com.ravunana.educacao.pagamento.domain.FormaLiquidacao;
import com.ravunana.educacao.pagamento.repository.FormaLiquidacaoRepository;
import com.ravunana.educacao.pagamento.repository.search.FormaLiquidacaoSearchRepository;
import com.ravunana.educacao.pagamento.service.dto.FormaLiquidacaoDTO;
import com.ravunana.educacao.pagamento.service.mapper.FormaLiquidacaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link FormaLiquidacao}.
 */
@Service
@Transactional
public class FormaLiquidacaoService {

    private final Logger log = LoggerFactory.getLogger(FormaLiquidacaoService.class);

    private final FormaLiquidacaoRepository formaLiquidacaoRepository;

    private final FormaLiquidacaoMapper formaLiquidacaoMapper;

    private final FormaLiquidacaoSearchRepository formaLiquidacaoSearchRepository;

    public FormaLiquidacaoService(FormaLiquidacaoRepository formaLiquidacaoRepository, FormaLiquidacaoMapper formaLiquidacaoMapper, FormaLiquidacaoSearchRepository formaLiquidacaoSearchRepository) {
        this.formaLiquidacaoRepository = formaLiquidacaoRepository;
        this.formaLiquidacaoMapper = formaLiquidacaoMapper;
        this.formaLiquidacaoSearchRepository = formaLiquidacaoSearchRepository;
    }

    /**
     * Save a formaLiquidacao.
     *
     * @param formaLiquidacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public FormaLiquidacaoDTO save(FormaLiquidacaoDTO formaLiquidacaoDTO) {
        log.debug("Request to save FormaLiquidacao : {}", formaLiquidacaoDTO);
        FormaLiquidacao formaLiquidacao = formaLiquidacaoMapper.toEntity(formaLiquidacaoDTO);
        formaLiquidacao = formaLiquidacaoRepository.save(formaLiquidacao);
        FormaLiquidacaoDTO result = formaLiquidacaoMapper.toDto(formaLiquidacao);
        formaLiquidacaoSearchRepository.save(formaLiquidacao);
        return result;
    }

    /**
     * Get all the formaLiquidacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FormaLiquidacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormaLiquidacaos");
        return formaLiquidacaoRepository.findAll(pageable)
            .map(formaLiquidacaoMapper::toDto);
    }


    /**
     * Get one formaLiquidacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FormaLiquidacaoDTO> findOne(Long id) {
        log.debug("Request to get FormaLiquidacao : {}", id);
        return formaLiquidacaoRepository.findById(id)
            .map(formaLiquidacaoMapper::toDto);
    }

    /**
     * Delete the formaLiquidacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FormaLiquidacao : {}", id);
        formaLiquidacaoRepository.deleteById(id);
        formaLiquidacaoSearchRepository.deleteById(id);
    }

    /**
     * Search for the formaLiquidacao corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FormaLiquidacaoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FormaLiquidacaos for query {}", query);
        return formaLiquidacaoSearchRepository.search(queryStringQuery(query), pageable)
            .map(formaLiquidacaoMapper::toDto);
    }
}
