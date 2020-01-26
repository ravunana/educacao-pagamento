package com.ravunana.educacao.pagamento.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class PagamentoEmolumentoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PagamentoEmolumentoDTO.class);
        PagamentoEmolumentoDTO pagamentoEmolumentoDTO1 = new PagamentoEmolumentoDTO();
        pagamentoEmolumentoDTO1.setId(1L);
        PagamentoEmolumentoDTO pagamentoEmolumentoDTO2 = new PagamentoEmolumentoDTO();
        assertThat(pagamentoEmolumentoDTO1).isNotEqualTo(pagamentoEmolumentoDTO2);
        pagamentoEmolumentoDTO2.setId(pagamentoEmolumentoDTO1.getId());
        assertThat(pagamentoEmolumentoDTO1).isEqualTo(pagamentoEmolumentoDTO2);
        pagamentoEmolumentoDTO2.setId(2L);
        assertThat(pagamentoEmolumentoDTO1).isNotEqualTo(pagamentoEmolumentoDTO2);
        pagamentoEmolumentoDTO1.setId(null);
        assertThat(pagamentoEmolumentoDTO1).isNotEqualTo(pagamentoEmolumentoDTO2);
    }
}
