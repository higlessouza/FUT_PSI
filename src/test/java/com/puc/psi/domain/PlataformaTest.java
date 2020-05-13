package com.puc.psi.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.puc.psi.web.rest.TestUtil;

public class PlataformaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plataforma.class);
        Plataforma plataforma1 = new Plataforma();
        plataforma1.setId(1L);
        Plataforma plataforma2 = new Plataforma();
        plataforma2.setId(plataforma1.getId());
        assertThat(plataforma1).isEqualTo(plataforma2);
        plataforma2.setId(2L);
        assertThat(plataforma1).isNotEqualTo(plataforma2);
        plataforma1.setId(null);
        assertThat(plataforma1).isNotEqualTo(plataforma2);
    }
}
