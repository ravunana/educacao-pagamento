package com.ravunana.educacao.pagamento.repository.search;

import com.ravunana.educacao.pagamento.domain.Deposito;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Deposito} entity.
 */
public interface DepositoSearchRepository extends ElasticsearchRepository<Deposito, Long> {
}
