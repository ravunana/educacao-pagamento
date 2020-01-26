package com.ravunana.educacao.pagamento.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DepositoSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DepositoSearchRepositoryMockConfiguration {

    @MockBean
    private DepositoSearchRepository mockDepositoSearchRepository;

}
