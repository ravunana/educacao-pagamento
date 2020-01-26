package com.ravunana.educacao.pagamento.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class EfeitoPagamentoMapperTest {

    private EfeitoPagamentoMapper efeitoPagamentoMapper;

    @BeforeEach
    public void setUp() {
        efeitoPagamentoMapper = new EfeitoPagamentoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(efeitoPagamentoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(efeitoPagamentoMapper.fromId(null)).isNull();
    }
}
