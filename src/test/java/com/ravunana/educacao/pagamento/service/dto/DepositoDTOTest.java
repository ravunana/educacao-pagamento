package com.ravunana.educacao.pagamento.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class DepositoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepositoDTO.class);
        DepositoDTO depositoDTO1 = new DepositoDTO();
        depositoDTO1.setId(1L);
        DepositoDTO depositoDTO2 = new DepositoDTO();
        assertThat(depositoDTO1).isNotEqualTo(depositoDTO2);
        depositoDTO2.setId(depositoDTO1.getId());
        assertThat(depositoDTO1).isEqualTo(depositoDTO2);
        depositoDTO2.setId(2L);
        assertThat(depositoDTO1).isNotEqualTo(depositoDTO2);
        depositoDTO1.setId(null);
        assertThat(depositoDTO1).isNotEqualTo(depositoDTO2);
    }
}
