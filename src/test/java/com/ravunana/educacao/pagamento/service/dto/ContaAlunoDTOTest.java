package com.ravunana.educacao.pagamento.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class ContaAlunoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaAlunoDTO.class);
        ContaAlunoDTO contaAlunoDTO1 = new ContaAlunoDTO();
        contaAlunoDTO1.setId(1L);
        ContaAlunoDTO contaAlunoDTO2 = new ContaAlunoDTO();
        assertThat(contaAlunoDTO1).isNotEqualTo(contaAlunoDTO2);
        contaAlunoDTO2.setId(contaAlunoDTO1.getId());
        assertThat(contaAlunoDTO1).isEqualTo(contaAlunoDTO2);
        contaAlunoDTO2.setId(2L);
        assertThat(contaAlunoDTO1).isNotEqualTo(contaAlunoDTO2);
        contaAlunoDTO1.setId(null);
        assertThat(contaAlunoDTO1).isNotEqualTo(contaAlunoDTO2);
    }
}
