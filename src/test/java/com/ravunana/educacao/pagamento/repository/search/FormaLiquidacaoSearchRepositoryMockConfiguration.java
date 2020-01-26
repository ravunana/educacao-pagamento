package com.ravunana.educacao.pagamento.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link FormaLiquidacaoSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FormaLiquidacaoSearchRepositoryMockConfiguration {

    @MockBean
    private FormaLiquidacaoSearchRepository mockFormaLiquidacaoSearchRepository;

}
