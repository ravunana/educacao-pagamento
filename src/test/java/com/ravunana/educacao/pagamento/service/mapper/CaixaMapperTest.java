package com.ravunana.educacao.pagamento.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CaixaMapperTest {

    private CaixaMapper caixaMapper;

    @BeforeEach
    public void setUp() {
        caixaMapper = new CaixaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(caixaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(caixaMapper.fromId(null)).isNull();
    }
}
