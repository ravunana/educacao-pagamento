package com.ravunana.educacao.pagamento.repository.search;

import com.ravunana.educacao.pagamento.domain.EfeitoPagamento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link EfeitoPagamento} entity.
 */
public interface EfeitoPagamentoSearchRepository extends ElasticsearchRepository<EfeitoPagamento, Long> {
}
