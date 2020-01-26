package com.ravunana.educacao.pagamento.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pagamento.web.rest.TestUtil;

public class EmolumentoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmolumentoDTO.class);
        EmolumentoDTO emolumentoDTO1 = new EmolumentoDTO();
        emolumentoDTO1.setId(1L);
        EmolumentoDTO emolumentoDTO2 = new EmolumentoDTO();
        assertThat(emolumentoDTO1).isNotEqualTo(emolumentoDTO2);
        emolumentoDTO2.setId(emolumentoDTO1.getId());
        assertThat(emolumentoDTO1).isEqualTo(emolumentoDTO2);
        emolumentoDTO2.setId(2L);
        assertThat(emolumentoDTO1).isNotEqualTo(emolumentoDTO2);
        emolumentoDTO1.setId(null);
        assertThat(emolumentoDTO1).isNotEqualTo(emolumentoDTO2);
    }
}
