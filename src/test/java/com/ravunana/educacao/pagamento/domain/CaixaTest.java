package com.ravunana.educacao.pagamento.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class CaixaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caixa.class);
        Caixa caixa1 = new Caixa();
        caixa1.setId(1L);
        Caixa caixa2 = new Caixa();
        caixa2.setId(caixa1.getId());
        assertThat(caixa1).isEqualTo(caixa2);
        caixa2.setId(2L);
        assertThat(caixa1).isNotEqualTo(caixa2);
        caixa1.setId(null);
        assertThat(caixa1).isNotEqualTo(caixa2);
    }
}
