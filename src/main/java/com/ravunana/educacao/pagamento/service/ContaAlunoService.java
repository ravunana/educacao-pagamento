package com.ravunana.educacao.pagamento.service;

import com.ravunana.educacao.pagamento.domain.ContaAluno;
import com.ravunana.educacao.pagamento.repository.ContaAlunoRepository;
import com.ravunana.educacao.pagamento.repository.search.ContaAlunoSearchRepository;
import com.ravunana.educacao.pagamento.service.dto.ContaAlunoDTO;
import com.ravunana.educacao.pagamento.service.mapper.ContaAlunoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ContaAluno}.
 */
@Service
@Transactional
public class ContaAlunoService {

    private final Logger log = LoggerFactory.getLogger(ContaAlunoService.class);

    private final ContaAlunoRepository contaAlunoRepository;

    private final ContaAlunoMapper contaAlunoMapper;

    private final ContaAlunoSearchRepository contaAlunoSearchRepository;

    public ContaAlunoService(ContaAlunoRepository contaAlunoRepository, ContaAlunoMapper contaAlunoMapper, ContaAlunoSearchRepository contaAlunoSearchRepository) {
        this.contaAlunoRepository = contaAlunoRepository;
        this.contaAlunoMapper = contaAlunoMapper;
        this.contaAlunoSearchRepository = contaAlunoSearchRepository;
    }

    /**
     * Save a contaAluno.
     *
     * @param contaAlunoDTO the entity to save.
     * @return the persisted entity.
     */
    public ContaAlunoDTO save(ContaAlunoDTO contaAlunoDTO) {
        log.debug("Request to save ContaAluno : {}", contaAlunoDTO);
        ContaAluno contaAluno = contaAlunoMapper.toEntity(contaAlunoDTO);
        contaAluno = contaAlunoRepository.save(contaAluno);
        ContaAlunoDTO result = contaAlunoMapper.toDto(contaAluno);
        contaAlunoSearchRepository.save(contaAluno);
        return result;
    }

    /**
     * Get all the contaAlunos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContaAlunoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContaAlunos");
        return contaAlunoRepository.findAll(pageable)
            .map(contaAlunoMapper::toDto);
    }


    /**
     * Get one contaAluno by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContaAlunoDTO> findOne(Long id) {
        log.debug("Request to get ContaAluno : {}", id);
        return contaAlunoRepository.findById(id)
            .map(contaAlunoMapper::toDto);
    }

    /**
     * Delete the contaAluno by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContaAluno : {}", id);
        contaAlunoRepository.deleteById(id);
        contaAlunoSearchRepository.deleteById(id);
    }

    /**
     * Search for the contaAluno corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContaAlunoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContaAlunos for query {}", query);
        return contaAlunoSearchRepository.search(queryStringQuery(query), pageable)
            .map(contaAlunoMapper::toDto);
    }
}
