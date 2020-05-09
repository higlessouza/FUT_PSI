package com.puc.psi.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.puc.psi.web.rest.TestUtil;

public class PartidaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Partida.class);
        Partida partida1 = new Partida();
        partida1.setId(1L);
        Partida partida2 = new Partida();
        partida2.setId(partida1.getId());
        assertThat(partida1).isEqualTo(partida2);
        partida2.setId(2L);
        assertThat(partida1).isNotEqualTo(partida2);
        partida1.setId(null);
        assertThat(partida1).isNotEqualTo(partida2);
    }
}
