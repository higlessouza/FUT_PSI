package com.puc.psi.web.rest;

import com.puc.psi.FutPsiApp;
import com.puc.psi.domain.Plataforma;
import com.puc.psi.repository.PlataformaRepository;

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
 * Integration tests for the {@link PlataformaResource} REST controller.
 */
@SpringBootTest(classes = FutPsiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PlataformaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_MARCA = "BBBBBBBBBB";

    @Autowired
    private PlataformaRepository plataformaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlataformaMockMvc;

    private Plataforma plataforma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plataforma createEntity(EntityManager em) {
        Plataforma plataforma = new Plataforma()
            .nome(DEFAULT_NOME)
            .logo(DEFAULT_LOGO)
            .marca(DEFAULT_MARCA);
        return plataforma;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plataforma createUpdatedEntity(EntityManager em) {
        Plataforma plataforma = new Plataforma()
            .nome(UPDATED_NOME)
            .logo(UPDATED_LOGO)
            .marca(UPDATED_MARCA);
        return plataforma;
    }

    @BeforeEach
    public void initTest() {
        plataforma = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlataforma() throws Exception {
        int databaseSizeBeforeCreate = plataformaRepository.findAll().size();

        // Create the Plataforma
        restPlataformaMockMvc.perform(post("/api/plataformas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plataforma)))
            .andExpect(status().isCreated());

        // Validate the Plataforma in the database
        List<Plataforma> plataformaList = plataformaRepository.findAll();
        assertThat(plataformaList).hasSize(databaseSizeBeforeCreate + 1);
        Plataforma testPlataforma = plataformaList.get(plataformaList.size() - 1);
        assertThat(testPlataforma.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPlataforma.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testPlataforma.getMarca()).isEqualTo(DEFAULT_MARCA);
    }

    @Test
    @Transactional
    public void createPlataformaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = plataformaRepository.findAll().size();

        // Create the Plataforma with an existing ID
        plataforma.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlataformaMockMvc.perform(post("/api/plataformas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plataforma)))
            .andExpect(status().isBadRequest());

        // Validate the Plataforma in the database
        List<Plataforma> plataformaList = plataformaRepository.findAll();
        assertThat(plataformaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlataformas() throws Exception {
        // Initialize the database
        plataformaRepository.saveAndFlush(plataforma);

        // Get all the plataformaList
        restPlataformaMockMvc.perform(get("/api/plataformas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plataforma.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)));
    }
    
    @Test
    @Transactional
    public void getPlataforma() throws Exception {
        // Initialize the database
        plataformaRepository.saveAndFlush(plataforma);

        // Get the plataforma
        restPlataformaMockMvc.perform(get("/api/plataformas/{id}", plataforma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plataforma.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA));
    }

    @Test
    @Transactional
    public void getNonExistingPlataforma() throws Exception {
        // Get the plataforma
        restPlataformaMockMvc.perform(get("/api/plataformas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlataforma() throws Exception {
        // Initialize the database
        plataformaRepository.saveAndFlush(plataforma);

        int databaseSizeBeforeUpdate = plataformaRepository.findAll().size();

        // Update the plataforma
        Plataforma updatedPlataforma = plataformaRepository.findById(plataforma.getId()).get();
        // Disconnect from session so that the updates on updatedPlataforma are not directly saved in db
        em.detach(updatedPlataforma);
        updatedPlataforma
            .nome(UPDATED_NOME)
            .logo(UPDATED_LOGO)
            .marca(UPDATED_MARCA);

        restPlataformaMockMvc.perform(put("/api/plataformas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlataforma)))
            .andExpect(status().isOk());

        // Validate the Plataforma in the database
        List<Plataforma> plataformaList = plataformaRepository.findAll();
        assertThat(plataformaList).hasSize(databaseSizeBeforeUpdate);
        Plataforma testPlataforma = plataformaList.get(plataformaList.size() - 1);
        assertThat(testPlataforma.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPlataforma.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testPlataforma.getMarca()).isEqualTo(UPDATED_MARCA);
    }

    @Test
    @Transactional
    public void updateNonExistingPlataforma() throws Exception {
        int databaseSizeBeforeUpdate = plataformaRepository.findAll().size();

        // Create the Plataforma

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlataformaMockMvc.perform(put("/api/plataformas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plataforma)))
            .andExpect(status().isBadRequest());

        // Validate the Plataforma in the database
        List<Plataforma> plataformaList = plataformaRepository.findAll();
        assertThat(plataformaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlataforma() throws Exception {
        // Initialize the database
        plataformaRepository.saveAndFlush(plataforma);

        int databaseSizeBeforeDelete = plataformaRepository.findAll().size();

        // Delete the plataforma
        restPlataformaMockMvc.perform(delete("/api/plataformas/{id}", plataforma.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plataforma> plataformaList = plataformaRepository.findAll();
        assertThat(plataformaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
