package com.ravunana.educacao.pagamento.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class CaixaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaixaDTO.class);
        CaixaDTO caixaDTO1 = new CaixaDTO();
        caixaDTO1.setId(1L);
        CaixaDTO caixaDTO2 = new CaixaDTO();
        assertThat(caixaDTO1).isNotEqualTo(caixaDTO2);
        caixaDTO2.setId(caixaDTO1.getId());
        assertThat(caixaDTO1).isEqualTo(caixaDTO2);
        caixaDTO2.setId(2L);
        assertThat(caixaDTO1).isNotEqualTo(caixaDTO2);
        caixaDTO1.setId(null);
        assertThat(caixaDTO1).isNotEqualTo(caixaDTO2);
    }
}
