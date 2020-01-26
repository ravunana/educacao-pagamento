package com.ravunana.educacao.pagamento.repository.search;

import com.ravunana.educacao.pagamento.domain.Caixa;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Caixa} entity.
 */
public interface CaixaSearchRepository extends ElasticsearchRepository<Caixa, Long> {
}
