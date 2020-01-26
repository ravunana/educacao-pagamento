package com.ravunana.educacao.pagamento.repository.search;

import com.ravunana.educacao.pagamento.domain.ContaAluno;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ContaAluno} entity.
 */
public interface ContaAlunoSearchRepository extends ElasticsearchRepository<ContaAluno, Long> {
}
