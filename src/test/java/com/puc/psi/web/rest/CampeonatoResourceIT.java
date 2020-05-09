package com.puc.psi.web.rest;

import com.puc.psi.FutPsiApp;
import com.puc.psi.domain.Campeonato;
import com.puc.psi.repository.CampeonatoRepository;

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
 * Integration tests for the {@link CampeonatoResource} REST controller.
 */
@SpringBootTest(classes = FutPsiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CampeonatoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCampeonatoMockMvc;

    private Campeonato campeonato;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campeonato createEntity(EntityManager em) {
        Campeonato campeonato = new Campeonato()
            .nome(DEFAULT_NOME)
            .logo(DEFAULT_LOGO)
            .cidade(DEFAULT_CIDADE);
        return campeonato;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campeonato createUpdatedEntity(EntityManager em) {
        Campeonato campeonato = new Campeonato()
            .nome(UPDATED_NOME)
            .logo(UPDATED_LOGO)
            .cidade(UPDATED_CIDADE);
        return campeonato;
    }

    @BeforeEach
    public void initTest() {
        campeonato = createEntity(em);
    }

    @Test
    @Transactional
    public void createCampeonato() throws Exception {
        int databaseSizeBeforeCreate = campeonatoRepository.findAll().size();

        // Create the Campeonato
        restCampeonatoMockMvc.perform(post("/api/campeonatoes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campeonato)))
            .andExpect(status().isCreated());

        // Validate the Campeonato in the database
        List<Campeonato> campeonatoList = campeonatoRepository.findAll();
        assertThat(campeonatoList).hasSize(databaseSizeBeforeCreate + 1);
        Campeonato testCampeonato = campeonatoList.get(campeonatoList.size() - 1);
        assertThat(testCampeonato.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCampeonato.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCampeonato.getCidade()).isEqualTo(DEFAULT_CIDADE);
    }

    @Test
    @Transactional
    public void createCampeonatoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = campeonatoRepository.findAll().size();

        // Create the Campeonato with an existing ID
        campeonato.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampeonatoMockMvc.perform(post("/api/campeonatoes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campeonato)))
            .andExpect(status().isBadRequest());

        // Validate the Campeonato in the database
        List<Campeonato> campeonatoList = campeonatoRepository.findAll();
        assertThat(campeonatoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCampeonatoes() throws Exception {
        // Initialize the database
        campeonatoRepository.saveAndFlush(campeonato);

        // Get all the campeonatoList
        restCampeonatoMockMvc.perform(get("/api/campeonatoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campeonato.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)));
    }
    
    @Test
    @Transactional
    public void getCampeonato() throws Exception {
        // Initialize the database
        campeonatoRepository.saveAndFlush(campeonato);

        // Get the campeonato
        restCampeonatoMockMvc.perform(get("/api/campeonatoes/{id}", campeonato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(campeonato.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE));
    }

    @Test
    @Transactional
    public void getNonExistingCampeonato() throws Exception {
        // Get the campeonato
        restCampeonatoMockMvc.perform(get("/api/campeonatoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampeonato() throws Exception {
        // Initialize the database
        campeonatoRepository.saveAndFlush(campeonato);

        int databaseSizeBeforeUpdate = campeonatoRepository.findAll().size();

        // Update the campeonato
        Campeonato updatedCampeonato = campeonatoRepository.findById(campeonato.getId()).get();
        // Disconnect from session so that the updates on updatedCampeonato are not directly saved in db
        em.detach(updatedCampeonato);
        updatedCampeonato
            .nome(UPDATED_NOME)
            .logo(UPDATED_LOGO)
            .cidade(UPDATED_CIDADE);

        restCampeonatoMockMvc.perform(put("/api/campeonatoes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCampeonato)))
            .andExpect(status().isOk());

        // Validate the Campeonato in the database
        List<Campeonato> campeonatoList = campeonatoRepository.findAll();
        assertThat(campeonatoList).hasSize(databaseSizeBeforeUpdate);
        Campeonato testCampeonato = campeonatoList.get(campeonatoList.size() - 1);
        assertThat(testCampeonato.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCampeonato.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCampeonato.getCidade()).isEqualTo(UPDATED_CIDADE);
    }

    @Test
    @Transactional
    public void updateNonExistingCampeonato() throws Exception {
        int databaseSizeBeforeUpdate = campeonatoRepository.findAll().size();

        // Create the Campeonato

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampeonatoMockMvc.perform(put("/api/campeonatoes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campeonato)))
            .andExpect(status().isBadRequest());

        // Validate the Campeonato in the database
        List<Campeonato> campeonatoList = campeonatoRepository.findAll();
        assertThat(campeonatoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCampeonato() throws Exception {
        // Initialize the database
        campeonatoRepository.saveAndFlush(campeonato);

        int databaseSizeBeforeDelete = campeonatoRepository.findAll().size();

        // Delete the campeonato
        restCampeonatoMockMvc.perform(delete("/api/campeonatoes/{id}", campeonato.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Campeonato> campeonatoList = campeonatoRepository.findAll();
        assertThat(campeonatoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
