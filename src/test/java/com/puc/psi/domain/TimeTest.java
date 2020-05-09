package com.puc.psi.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.puc.psi.web.rest.TestUtil;

public class TimeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Time.class);
        Time time1 = new Time();
        time1.setId(1L);
        Time time2 = new Time();
        time2.setId(time1.getId());
        assertThat(time1).isEqualTo(time2);
        time2.setId(2L);
        assertThat(time1).isNotEqualTo(time2);
        time1.setId(null);
        assertThat(time1).isNotEqualTo(time2);
    }
}
