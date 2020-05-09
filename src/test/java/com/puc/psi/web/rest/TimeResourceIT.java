package com.puc.psi.web.rest;

import com.puc.psi.FutPsiApp;
import com.puc.psi.domain.Time;
import com.puc.psi.repository.TimeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TimeResource} REST controller.
 */
@SpringBootTest(classes = FutPsiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class TimeResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeMockMvc;

    private Time time;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Time createEntity(EntityManager em) {
        Time time = new Time()
            .nome(DEFAULT_NOME)
            .logo(DEFAULT_LOGO);
        return time;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Time createUpdatedEntity(EntityManager em) {
        Time time = new Time()
            .nome(UPDATED_NOME)
            .logo(UPDATED_LOGO);
        return time;
    }

    @BeforeEach
    public void initTest() {
        time = createEntity(em);
    }

    @Test
    @Transactional
    public void createTime() throws Exception {
        int databaseSizeBeforeCreate = timeRepository.findAll().size();

        // Create the Time
        restTimeMockMvc.perform(post("/api/times").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(time)))
            .andExpect(status().isCreated());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeCreate + 1);
        Time testTime = timeList.get(timeList.size() - 1);
        assertThat(testTime.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTime.getLogo()).isEqualTo(DEFAULT_LOGO);
    }

    @Test
    @Transactional
    public void createTimeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timeRepository.findAll().size();

        // Create the Time with an existing ID
        time.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeMockMvc.perform(post("/api/times").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(time)))
            .andExpect(status().isBadRequest());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTimes() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);

        // Get all the timeList
        restTimeMockMvc.perform(get("/api/times?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(time.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)));
    }
    
    @Test
    @Transactional
    public void getTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);

        // Get the time
        restTimeMockMvc.perform(get("/api/times/{id}", time.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(time.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO));
    }

    @Test
    @Transactional
    public void getNonExistingTime() throws Exception {
        // Get the time
        restTimeMockMvc.perform(get("/api/times/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);

        int databaseSizeBeforeUpdate = timeRepository.findAll().size();

        // Update the time
        Time updatedTime = timeRepository.findById(time.getId()).get();
        // Disconnect from session so that the updates on updatedTime are not directly saved in db
        em.detach(updatedTime);
        updatedTime
            .nome(UPDATED_NOME)
            .logo(UPDATED_LOGO);

        restTimeMockMvc.perform(put("/api/times").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTime)))
            .andExpect(status().isOk());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeUpdate);
        Time testTime = timeList.get(timeList.size() - 1);
        assertThat(testTime.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTime.getLogo()).isEqualTo(UPDATED_LOGO);
    }

    @Test
    @Transactional
    public void updateNonExistingTime() throws Exception {
        int databaseSizeBeforeUpdate = timeRepository.findAll().size();

        // Create the Time

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeMockMvc.perform(put("/api/times").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(time)))
            .andExpect(status().isBadRequest());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);

        int databaseSizeBeforeDelete = timeRepository.findAll().size();

        // Delete the time
        restTimeMockMvc.perform(delete("/api/times/{id}", time.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
