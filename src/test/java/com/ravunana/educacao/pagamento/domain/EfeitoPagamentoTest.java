package com.ravunana.educacao.pagamento.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class EfeitoPagamentoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EfeitoPagamento.class);
        EfeitoPagamento efeitoPagamento1 = new EfeitoPagamento();
        efeitoPagamento1.setId(1L);
        EfeitoPagamento efeitoPagamento2 = new EfeitoPagamento();
        efeitoPagamento2.setId(efeitoPagamento1.getId());
        assertThat(efeitoPagamento1).isEqualTo(efeitoPagamento2);
        efeitoPagamento2.setId(2L);
        assertThat(efeitoPagamento1).isNotEqualTo(efeitoPagamento2);
        efeitoPagamento1.setId(null);
        assertThat(efeitoPagamento1).isNotEqualTo(efeitoPagamento2);
    }
}
