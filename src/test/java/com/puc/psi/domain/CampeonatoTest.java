package com.puc.psi.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.puc.psi.web.rest.TestUtil;

public class CampeonatoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campeonato.class);
        Campeonato campeonato1 = new Campeonato();
        campeonato1.setId(1L);
        Campeonato campeonato2 = new Campeonato();
        campeonato2.setId(campeonato1.getId());
        assertThat(campeonato1).isEqualTo(campeonato2);
        campeonato2.setId(2L);
        assertThat(campeonato1).isNotEqualTo(campeonato2);
        campeonato1.setId(null);
        assertThat(campeonato1).isNotEqualTo(campeonato2);
    }
}
