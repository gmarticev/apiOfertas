package com.mycompany.apiofertas.web.rest;

import com.mycompany.apiofertas.ApiofertasApp;
import com.mycompany.apiofertas.domain.Oferta;
import com.mycompany.apiofertas.repository.OfertaRepository;
import com.mycompany.apiofertas.repository.search.OfertaSearchRepository;
import com.mycompany.apiofertas.service.OfertaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OfertaResource} REST controller.
 */
@SpringBootTest(classes = ApiofertasApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class OfertaResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_EMPRESA = "BBBBBBBBBB";

    private static final Long DEFAULT_SALARIO = 1L;
    private static final Long UPDATED_SALARIO = 2L;

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private OfertaService ofertaService;

    /**
     * This repository is mocked in the com.mycompany.apiofertas.repository.search test package.
     *
     * @see com.mycompany.apiofertas.repository.search.OfertaSearchRepositoryMockConfiguration
     */
    @Autowired
    private OfertaSearchRepository mockOfertaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOfertaMockMvc;

    private Oferta oferta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oferta createEntity(EntityManager em) {
        Oferta oferta = new Oferta()
            .titulo(DEFAULT_TITULO)
            .descripcion(DEFAULT_DESCRIPCION)
            .empresa(DEFAULT_EMPRESA)
            .salario(DEFAULT_SALARIO)
            .ciudad(DEFAULT_CIUDAD)
            .email(DEFAULT_EMAIL);
        return oferta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oferta createUpdatedEntity(EntityManager em) {
        Oferta oferta = new Oferta()
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .empresa(UPDATED_EMPRESA)
            .salario(UPDATED_SALARIO)
            .ciudad(UPDATED_CIUDAD)
            .email(UPDATED_EMAIL);
        return oferta;
    }

    @BeforeEach
    public void initTest() {
        oferta = createEntity(em);
    }

    @Test
    @Transactional
    public void createOferta() throws Exception {
        int databaseSizeBeforeCreate = ofertaRepository.findAll().size();
        // Create the Oferta
        restOfertaMockMvc.perform(post("/api/ofertas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oferta)))
            .andExpect(status().isCreated());

        // Validate the Oferta in the database
        List<Oferta> ofertaList = ofertaRepository.findAll();
        assertThat(ofertaList).hasSize(databaseSizeBeforeCreate + 1);
        Oferta testOferta = ofertaList.get(ofertaList.size() - 1);
        assertThat(testOferta.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testOferta.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testOferta.getEmpresa()).isEqualTo(DEFAULT_EMPRESA);
        assertThat(testOferta.getSalario()).isEqualTo(DEFAULT_SALARIO);
        assertThat(testOferta.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testOferta.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Oferta in Elasticsearch
        verify(mockOfertaSearchRepository, times(1)).save(testOferta);
    }

    @Test
    @Transactional
    public void createOfertaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ofertaRepository.findAll().size();

        // Create the Oferta with an existing ID
        oferta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfertaMockMvc.perform(post("/api/ofertas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oferta)))
            .andExpect(status().isBadRequest());

        // Validate the Oferta in the database
        List<Oferta> ofertaList = ofertaRepository.findAll();
        assertThat(ofertaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Oferta in Elasticsearch
        verify(mockOfertaSearchRepository, times(0)).save(oferta);
    }


    @Test
    @Transactional
    public void getAllOfertas() throws Exception {
        // Initialize the database
        ofertaRepository.saveAndFlush(oferta);

        // Get all the ofertaList
        restOfertaMockMvc.perform(get("/api/ofertas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oferta.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].empresa").value(hasItem(DEFAULT_EMPRESA)))
            .andExpect(jsonPath("$.[*].salario").value(hasItem(DEFAULT_SALARIO.intValue())))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }
    
    @Test
    @Transactional
    public void getOferta() throws Exception {
        // Initialize the database
        ofertaRepository.saveAndFlush(oferta);

        // Get the oferta
        restOfertaMockMvc.perform(get("/api/ofertas/{id}", oferta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oferta.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.empresa").value(DEFAULT_EMPRESA))
            .andExpect(jsonPath("$.salario").value(DEFAULT_SALARIO.intValue()))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }
    @Test
    @Transactional
    public void getNonExistingOferta() throws Exception {
        // Get the oferta
        restOfertaMockMvc.perform(get("/api/ofertas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOferta() throws Exception {
        // Initialize the database
        ofertaService.save(oferta);

        int databaseSizeBeforeUpdate = ofertaRepository.findAll().size();

        // Update the oferta
        Oferta updatedOferta = ofertaRepository.findById(oferta.getId()).get();
        // Disconnect from session so that the updates on updatedOferta are not directly saved in db
        em.detach(updatedOferta);
        updatedOferta
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .empresa(UPDATED_EMPRESA)
            .salario(UPDATED_SALARIO)
            .ciudad(UPDATED_CIUDAD)
            .email(UPDATED_EMAIL);

        restOfertaMockMvc.perform(put("/api/ofertas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOferta)))
            .andExpect(status().isOk());

        // Validate the Oferta in the database
        List<Oferta> ofertaList = ofertaRepository.findAll();
        assertThat(ofertaList).hasSize(databaseSizeBeforeUpdate);
        Oferta testOferta = ofertaList.get(ofertaList.size() - 1);
        assertThat(testOferta.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testOferta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testOferta.getEmpresa()).isEqualTo(UPDATED_EMPRESA);
        assertThat(testOferta.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testOferta.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testOferta.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Oferta in Elasticsearch
        verify(mockOfertaSearchRepository, times(2)).save(testOferta);
    }

    @Test
    @Transactional
    public void updateNonExistingOferta() throws Exception {
        int databaseSizeBeforeUpdate = ofertaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfertaMockMvc.perform(put("/api/ofertas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oferta)))
            .andExpect(status().isBadRequest());

        // Validate the Oferta in the database
        List<Oferta> ofertaList = ofertaRepository.findAll();
        assertThat(ofertaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Oferta in Elasticsearch
        verify(mockOfertaSearchRepository, times(0)).save(oferta);
    }

    @Test
    @Transactional
    public void deleteOferta() throws Exception {
        // Initialize the database
        ofertaService.save(oferta);

        int databaseSizeBeforeDelete = ofertaRepository.findAll().size();

        // Delete the oferta
        restOfertaMockMvc.perform(delete("/api/ofertas/{id}", oferta.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Oferta> ofertaList = ofertaRepository.findAll();
        assertThat(ofertaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Oferta in Elasticsearch
        verify(mockOfertaSearchRepository, times(1)).deleteById(oferta.getId());
    }

    @Test
    @Transactional
    public void searchOferta() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ofertaService.save(oferta);
        when(mockOfertaSearchRepository.search(queryStringQuery("id:" + oferta.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(oferta), PageRequest.of(0, 1), 1));

        // Search the oferta
        restOfertaMockMvc.perform(get("/api/_search/ofertas?query=id:" + oferta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oferta.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].empresa").value(hasItem(DEFAULT_EMPRESA)))
            .andExpect(jsonPath("$.[*].salario").value(hasItem(DEFAULT_SALARIO.intValue())))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }
}
