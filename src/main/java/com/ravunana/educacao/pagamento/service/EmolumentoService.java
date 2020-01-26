package com.ravunana.educacao.pagamento.service;

import com.ravunana.educacao.pagamento.domain.Emolumento;
import com.ravunana.educacao.pagamento.repository.EmolumentoRepository;
import com.ravunana.educacao.pagamento.repository.search.EmolumentoSearchRepository;
import com.ravunana.educacao.pagamento.service.dto.EmolumentoDTO;
import com.ravunana.educacao.pagamento.service.mapper.EmolumentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Emolumento}.
 */
@Service
@Transactional
public class EmolumentoService {

    private final Logger log = LoggerFactory.getLogger(EmolumentoService.class);

    private final EmolumentoRepository emolumentoRepository;

    private final EmolumentoMapper emolumentoMapper;

    private final EmolumentoSearchRepository emolumentoSearchRepository;

    public EmolumentoService(EmolumentoRepository emolumentoRepository, EmolumentoMapper emolumentoMapper, EmolumentoSearchRepository emolumentoSearchRepository) {
        this.emolumentoRepository = emolumentoRepository;
        this.emolumentoMapper = emolumentoMapper;
        this.emolumentoSearchRepository = emolumentoSearchRepository;
    }

    /**
     * Save a emolumento.
     *
     * @param emolumentoDTO the entity to save.
     * @return the persisted entity.
     */
    public EmolumentoDTO save(EmolumentoDTO emolumentoDTO) {
        log.debug("Request to save Emolumento : {}", emolumentoDTO);
        Emolumento emolumento = emolumentoMapper.toEntity(emolumentoDTO);
        emolumento = emolumentoRepository.save(emolumento);
        EmolumentoDTO result = emolumentoMapper.toDto(emolumento);
        emolumentoSearchRepository.save(emolumento);
        return result;
    }

    /**
     * Get all the emolumentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmolumentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Emolumentos");
        return emolumentoRepository.findAll(pageable)
            .map(emolumentoMapper::toDto);
    }


    /**
     * Get one emolumento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmolumentoDTO> findOne(Long id) {
        log.debug("Request to get Emolumento : {}", id);
        return emolumentoRepository.findById(id)
            .map(emolumentoMapper::toDto);
    }

    /**
     * Delete the emolumento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Emolumento : {}", id);
        emolumentoRepository.deleteById(id);
        emolumentoSearchRepository.deleteById(id);
    }

    /**
     * Search for the emolumento corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmolumentoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Emolumentos for query {}", query);
        return emolumentoSearchRepository.search(queryStringQuery(query), pageable)
            .map(emolumentoMapper::toDto);
    }
}
