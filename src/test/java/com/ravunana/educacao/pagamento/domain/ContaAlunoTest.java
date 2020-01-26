package com.ravunana.educacao.pagamento.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class ContaAlunoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaAluno.class);
        ContaAluno contaAluno1 = new ContaAluno();
        contaAluno1.setId(1L);
        ContaAluno contaAluno2 = new ContaAluno();
        contaAluno2.setId(contaAluno1.getId());
        assertThat(contaAluno1).isEqualTo(contaAluno2);
        contaAluno2.setId(2L);
        assertThat(contaAluno1).isNotEqualTo(contaAluno2);
        contaAluno1.setId(null);
        assertThat(contaAluno1).isNotEqualTo(contaAluno2);
    }
}
