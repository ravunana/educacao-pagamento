package com.ravunana.educacao.pagamento.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PagamentoEmolumentoMapperTest {

    private PagamentoEmolumentoMapper pagamentoEmolumentoMapper;

    @BeforeEach
    public void setUp() {
        pagamentoEmolumentoMapper = new PagamentoEmolumentoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(pagamentoEmolumentoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(pagamentoEmolumentoMapper.fromId(null)).isNull();
    }
}
