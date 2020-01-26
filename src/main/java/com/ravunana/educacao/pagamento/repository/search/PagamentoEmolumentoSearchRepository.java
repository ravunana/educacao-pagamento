package com.ravunana.educacao.pagamento.repository.search;

import com.ravunana.educacao.pagamento.domain.PagamentoEmolumento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PagamentoEmolumento} entity.
 */
public interface PagamentoEmolumentoSearchRepository extends ElasticsearchRepository<PagamentoEmolumento, Long> {
}
