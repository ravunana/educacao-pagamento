package com.ravunana.educacao.pagamento.service;

import com.ravunana.educacao.pagamento.domain.Deposito;
import com.ravunana.educacao.pagamento.repository.DepositoRepository;
import com.ravunana.educacao.pagamento.repository.search.DepositoSearchRepository;
import com.ravunana.educacao.pagamento.service.dto.DepositoDTO;
import com.ravunana.educacao.pagamento.service.mapper.DepositoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Deposito}.
 */
@Service
@Transactional
public class DepositoService {

    private final Logger log = LoggerFactory.getLogger(DepositoService.class);

    private final DepositoRepository depositoRepository;

    private final DepositoMapper depositoMapper;

    private final DepositoSearchRepository depositoSearchRepository;

    public DepositoService(DepositoRepository depositoRepository, DepositoMapper depositoMapper, DepositoSearchRepository depositoSearchRepository) {
        this.depositoRepository = depositoRepository;
        this.depositoMapper = depositoMapper;
        this.depositoSearchRepository = depositoSearchRepository;
    }

    /**
     * Save a deposito.
     *
     * @param depositoDTO the entity to save.
     * @return the persisted entity.
     */
    public DepositoDTO save(DepositoDTO depositoDTO) {
        log.debug("Request to save Deposito : {}", depositoDTO);
        Deposito deposito = depositoMapper.toEntity(depositoDTO);
        deposito = depositoRepository.save(deposito);
        DepositoDTO result = depositoMapper.toDto(deposito);
        depositoSearchRepository.save(deposito);
        return result;
    }

    /**
     * Get all the depositos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DepositoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Depositos");
        return depositoRepository.findAll(pageable)
            .map(depositoMapper::toDto);
    }


    /**
     * Get one deposito by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepositoDTO> findOne(Long id) {
        log.debug("Request to get Deposito : {}", id);
        return depositoRepository.findById(id)
            .map(depositoMapper::toDto);
    }

    /**
     * Delete the deposito by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Deposito : {}", id);
        depositoRepository.deleteById(id);
        depositoSearchRepository.deleteById(id);
    }

    /**
     * Search for the deposito corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DepositoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Depositos for query {}", query);
        return depositoSearchRepository.search(queryStringQuery(query), pageable)
            .map(depositoMapper::toDto);
    }
}
