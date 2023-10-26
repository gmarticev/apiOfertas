package com.mycompany.apiofertas.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.apiofertas.web.rest.TestUtil;

public class OfertaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Oferta.class);
        Oferta oferta1 = new Oferta();
        oferta1.setId(1L);
        Oferta oferta2 = new Oferta();
        oferta2.setId(oferta1.getId());
        assertThat(oferta1).isEqualTo(oferta2);
        oferta2.setId(2L);
        assertThat(oferta1).isNotEqualTo(oferta2);
        oferta1.setId(null);
        assertThat(oferta1).isNotEqualTo(oferta2);
    }
}
