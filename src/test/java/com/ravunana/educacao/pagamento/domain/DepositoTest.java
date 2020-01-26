package com.ravunana.educacao.pagamento.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class DepositoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deposito.class);
        Deposito deposito1 = new Deposito();
        deposito1.setId(1L);
        Deposito deposito2 = new Deposito();
        deposito2.setId(deposito1.getId());
        assertThat(deposito1).isEqualTo(deposito2);
        deposito2.setId(2L);
        assertThat(deposito1).isNotEqualTo(deposito2);
        deposito1.setId(null);
        assertThat(deposito1).isNotEqualTo(deposito2);
    }
}
