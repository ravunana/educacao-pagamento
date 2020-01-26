package com.ravunana.educacao.pagamento.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class DepositoMapperTest {

    private DepositoMapper depositoMapper;

    @BeforeEach
    public void setUp() {
        depositoMapper = new DepositoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(depositoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(depositoMapper.fromId(null)).isNull();
    }
}
