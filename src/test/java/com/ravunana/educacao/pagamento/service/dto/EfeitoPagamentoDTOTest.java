package com.ravunana.educacao.pagamento.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class EfeitoPagamentoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EfeitoPagamentoDTO.class);
        EfeitoPagamentoDTO efeitoPagamentoDTO1 = new EfeitoPagamentoDTO();
        efeitoPagamentoDTO1.setId(1L);
        EfeitoPagamentoDTO efeitoPagamentoDTO2 = new EfeitoPagamentoDTO();
        assertThat(efeitoPagamentoDTO1).isNotEqualTo(efeitoPagamentoDTO2);
        efeitoPagamentoDTO2.setId(efeitoPagamentoDTO1.getId());
        assertThat(efeitoPagamentoDTO1).isEqualTo(efeitoPagamentoDTO2);
        efeitoPagamentoDTO2.setId(2L);
        assertThat(efeitoPagamentoDTO1).isNotEqualTo(efeitoPagamentoDTO2);
        efeitoPagamentoDTO1.setId(null);
        assertThat(efeitoPagamentoDTO1).isNotEqualTo(efeitoPagamentoDTO2);
    }
}
