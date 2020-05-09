package com.puc.psi.web.rest;

import com.puc.psi.FutPsiApp;
import com.puc.psi.domain.Partida;
import com.puc.psi.repository.PartidaRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PartidaResource} REST controller.
 */
@SpringBootTest(classes = FutPsiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PartidaResourceIT {

    private static final Integer DEFAULT_GOLS_VISITANTE = 1;
    private static final Integer UPDATED_GOLS_VISITANTE = 2;

    private static final Integer DEFAULT_GOLS_MANDANTE = 1;
    private static final Integer UPDATED_GOLS_MANDANTE = 2;

    private static final String DEFAULT_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartidaMockMvc;

    private Partida partida;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partida createEntity(EntityManager em) {
        Partida partida = new Partida()
            .golsVisitante(DEFAULT_GOLS_VISITANTE)
            .golsMandante(DEFAULT_GOLS_MANDANTE)
            .local(DEFAULT_LOCAL)
            .data(DEFAULT_DATA);
        return partida;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partida createUpdatedEntity(EntityManager em) {
        Partida partida = new Partida()
            .golsVisitante(UPDATED_GOLS_VISITANTE)
            .golsMandante(UPDATED_GOLS_MANDANTE)
            .local(UPDATED_LOCAL)
            .data(UPDATED_DATA);
        return partida;
    }

    @BeforeEach
    public void initTest() {
        partida = createEntity(em);
    }

    @Test
    @Transactional
    public void createPartida() throws Exception {
        int databaseSizeBeforeCreate = partidaRepository.findAll().size();

        // Create the Partida
        restPartidaMockMvc.perform(post("/api/partidas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partida)))
            .andExpect(status().isCreated());

        // Validate the Partida in the database
        List<Partida> partidaList = partidaRepository.findAll();
        assertThat(partidaList).hasSize(databaseSizeBeforeCreate + 1);
        Partida testPartida = partidaList.get(partidaList.size() - 1);
        assertThat(testPartida.getGolsVisitante()).isEqualTo(DEFAULT_GOLS_VISITANTE);
        assertThat(testPartida.getGolsMandante()).isEqualTo(DEFAULT_GOLS_MANDANTE);
        assertThat(testPartida.getLocal()).isEqualTo(DEFAULT_LOCAL);
        assertThat(testPartida.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    public void createPartidaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = partidaRepository.findAll().size();

        // Create the Partida with an existing ID
        partida.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartidaMockMvc.perform(post("/api/partidas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partida)))
            .andExpect(status().isBadRequest());

        // Validate the Partida in the database
        List<Partida> partidaList = partidaRepository.findAll();
        assertThat(partidaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPartidas() throws Exception {
        // Initialize the database
        partidaRepository.saveAndFlush(partida);

        // Get all the partidaList
        restPartidaMockMvc.perform(get("/api/partidas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partida.getId().intValue())))
            .andExpect(jsonPath("$.[*].golsVisitante").value(hasItem(DEFAULT_GOLS_VISITANTE)))
            .andExpect(jsonPath("$.[*].golsMandante").value(hasItem(DEFAULT_GOLS_MANDANTE)))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }
    
    @Test
    @Transactional
    public void getPartida() throws Exception {
        // Initialize the database
        partidaRepository.saveAndFlush(partida);

        // Get the partida
        restPartidaMockMvc.perform(get("/api/partidas/{id}", partida.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partida.getId().intValue()))
            .andExpect(jsonPath("$.golsVisitante").value(DEFAULT_GOLS_VISITANTE))
            .andExpect(jsonPath("$.golsMandante").value(DEFAULT_GOLS_MANDANTE))
            .andExpect(jsonPath("$.local").value(DEFAULT_LOCAL))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPartida() throws Exception {
        // Get the partida
        restPartidaMockMvc.perform(get("/api/partidas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartida() throws Exception {
        // Initialize the database
        partidaRepository.saveAndFlush(partida);

        int databaseSizeBeforeUpdate = partidaRepository.findAll().size();

        // Update the partida
        Partida updatedPartida = partidaRepository.findById(partida.getId()).get();
        // Disconnect from session so that the updates on updatedPartida are not directly saved in db
        em.detach(updatedPartida);
        updatedPartida
            .golsVisitante(UPDATED_GOLS_VISITANTE)
            .golsMandante(UPDATED_GOLS_MANDANTE)
            .local(UPDATED_LOCAL)
            .data(UPDATED_DATA);

        restPartidaMockMvc.perform(put("/api/partidas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPartida)))
            .andExpect(status().isOk());

        // Validate the Partida in the database
        List<Partida> partidaList = partidaRepository.findAll();
        assertThat(partidaList).hasSize(databaseSizeBeforeUpdate);
        Partida testPartida = partidaList.get(partidaList.size() - 1);
        assertThat(testPartida.getGolsVisitante()).isEqualTo(UPDATED_GOLS_VISITANTE);
        assertThat(testPartida.getGolsMandante()).isEqualTo(UPDATED_GOLS_MANDANTE);
        assertThat(testPartida.getLocal()).isEqualTo(UPDATED_LOCAL);
        assertThat(testPartida.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingPartida() throws Exception {
        int databaseSizeBeforeUpdate = partidaRepository.findAll().size();

        // Create the Partida

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartidaMockMvc.perform(put("/api/partidas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(partida)))
            .andExpect(status().isBadRequest());

        // Validate the Partida in the database
        List<Partida> partidaList = partidaRepository.findAll();
        assertThat(partidaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePartida() throws Exception {
        // Initialize the database
        partidaRepository.saveAndFlush(partida);

        int databaseSizeBeforeDelete = partidaRepository.findAll().size();

        // Delete the partida
        restPartidaMockMvc.perform(delete("/api/partidas/{id}", partida.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Partida> partidaList = partidaRepository.findAll();
        assertThat(partidaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
