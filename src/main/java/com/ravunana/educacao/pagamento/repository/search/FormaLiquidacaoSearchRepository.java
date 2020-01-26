package com.ravunana.educacao.pagamento.repository.search;

import com.ravunana.educacao.pagamento.domain.FormaLiquidacao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FormaLiquidacao} entity.
 */
public interface FormaLiquidacaoSearchRepository extends ElasticsearchRepository<FormaLiquidacao, Long> {
}
