package com.ravunana.educacao.pagamento.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class PagamentoEmolumentoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PagamentoEmolumento.class);
        PagamentoEmolumento pagamentoEmolumento1 = new PagamentoEmolumento();
        pagamentoEmolumento1.setId(1L);
        PagamentoEmolumento pagamentoEmolumento2 = new PagamentoEmolumento();
        pagamentoEmolumento2.setId(pagamentoEmolumento1.getId());
        assertThat(pagamentoEmolumento1).isEqualTo(pagamentoEmolumento2);
        pagamentoEmolumento2.setId(2L);
        assertThat(pagamentoEmolumento1).isNotEqualTo(pagamentoEmolumento2);
        pagamentoEmolumento1.setId(null);
        assertThat(pagamentoEmolumento1).isNotEqualTo(pagamentoEmolumento2);
    }
}
