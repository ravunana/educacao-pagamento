package com.ravunana.educacao.pagamento.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ContaAlunoMapperTest {

    private ContaAlunoMapper contaAlunoMapper;

    @BeforeEach
    public void setUp() {
        contaAlunoMapper = new ContaAlunoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(contaAlunoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(contaAlunoMapper.fromId(null)).isNull();
    }
}
