package com.ravunana.educacao.pagamento.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PagamentoEmolumentoSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PagamentoEmolumentoSearchRepositoryMockConfiguration {

    @MockBean
    private PagamentoEmolumentoSearchRepository mockPagamentoEmolumentoSearchRepository;

}
