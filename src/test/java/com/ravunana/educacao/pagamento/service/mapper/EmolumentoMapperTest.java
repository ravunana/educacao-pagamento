package com.ravunana.educacao.pagamento.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class EmolumentoMapperTest {

    private EmolumentoMapper emolumentoMapper;

    @BeforeEach
    public void setUp() {
        emolumentoMapper = new EmolumentoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(emolumentoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(emolumentoMapper.fromId(null)).isNull();
    }
}
