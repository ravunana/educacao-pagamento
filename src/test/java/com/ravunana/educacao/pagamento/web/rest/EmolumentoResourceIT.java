package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.PagamentoApp;
import com.ravunana.educacao.pagamento.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pagamento.domain.Emolumento;
import com.ravunana.educacao.pagamento.repository.EmolumentoRepository;
import com.ravunana.educacao.pagamento.repository.search.EmolumentoSearchRepository;
import com.ravunana.educacao.pagamento.service.EmolumentoService;
import com.ravunana.educacao.pagamento.service.dto.EmolumentoDTO;
import com.ravunana.educacao.pagamento.service.mapper.EmolumentoMapper;
import com.ravunana.educacao.pagamento.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static com.ravunana.educacao.pagamento.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EmolumentoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PagamentoApp.class})
public class EmolumentoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTANTE = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTANTE = new BigDecimal(1);

    private static final Double DEFAULT_MONTANTE_MULTA = 0D;
    private static final Double UPDATED_MONTANTE_MULTA = 1D;

    private static final Integer DEFAULT_TEMPO_MULTA = 0;
    private static final Integer UPDATED_TEMPO_MULTA = 1;

    private static final Double DEFAULT_QUANTIDADE = 0D;
    private static final Double UPDATED_QUANTIDADE = 1D;

    private static final String DEFAULT_TURNO = "AAAAAAAAAA";
    private static final String UPDATED_TURNO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CLASSE = 1;
    private static final Integer UPDATED_CLASSE = 2;

    private static final String DEFAULT_CURSO = "AAAAAAAAAA";
    private static final String UPDATED_CURSO = "BBBBBBBBBB";

    @Autowired
    private EmolumentoRepository emolumentoRepository;

    @Autowired
    private EmolumentoMapper emolumentoMapper;

    @Autowired
    private EmolumentoService emolumentoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pagamento.repository.search test package.
     *
     * @see com.ravunana.educacao.pagamento.repository.search.EmolumentoSearchRepositoryMockConfiguration
     */
    @Autowired
    private EmolumentoSearchRepository mockEmolumentoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restEmolumentoMockMvc;

    private Emolumento emolumento;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmolumentoResource emolumentoResource = new EmolumentoResource(emolumentoService);
        this.restEmolumentoMockMvc = MockMvcBuilders.standaloneSetup(emolumentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emolumento createEntity(EntityManager em) {
        Emolumento emolumento = new Emolumento()
            .nome(DEFAULT_NOME)
            .montante(DEFAULT_MONTANTE)
            .montanteMulta(DEFAULT_MONTANTE_MULTA)
            .tempoMulta(DEFAULT_TEMPO_MULTA)
            .quantidade(DEFAULT_QUANTIDADE)
            .turno(DEFAULT_TURNO)
            .classe(DEFAULT_CLASSE)
            .curso(DEFAULT_CURSO);
        return emolumento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emolumento createUpdatedEntity(EntityManager em) {
        Emolumento emolumento = new Emolumento()
            .nome(UPDATED_NOME)
            .montante(UPDATED_MONTANTE)
            .montanteMulta(UPDATED_MONTANTE_MULTA)
            .tempoMulta(UPDATED_TEMPO_MULTA)
            .quantidade(UPDATED_QUANTIDADE)
            .turno(UPDATED_TURNO)
            .classe(UPDATED_CLASSE)
            .curso(UPDATED_CURSO);
        return emolumento;
    }

    @BeforeEach
    public void initTest() {
        emolumento = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmolumento() throws Exception {
        int databaseSizeBeforeCreate = emolumentoRepository.findAll().size();

        // Create the Emolumento
        EmolumentoDTO emolumentoDTO = emolumentoMapper.toDto(emolumento);
        restEmolumentoMockMvc.perform(post("/api/emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emolumentoDTO)))
            .andExpect(status().isCreated());

        // Validate the Emolumento in the database
        List<Emolumento> emolumentoList = emolumentoRepository.findAll();
        assertThat(emolumentoList).hasSize(databaseSizeBeforeCreate + 1);
        Emolumento testEmolumento = emolumentoList.get(emolumentoList.size() - 1);
        assertThat(testEmolumento.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEmolumento.getMontante()).isEqualTo(DEFAULT_MONTANTE);
        assertThat(testEmolumento.getMontanteMulta()).isEqualTo(DEFAULT_MONTANTE_MULTA);
        assertThat(testEmolumento.getTempoMulta()).isEqualTo(DEFAULT_TEMPO_MULTA);
        assertThat(testEmolumento.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
        assertThat(testEmolumento.getTurno()).isEqualTo(DEFAULT_TURNO);
        assertThat(testEmolumento.getClasse()).isEqualTo(DEFAULT_CLASSE);
        assertThat(testEmolumento.getCurso()).isEqualTo(DEFAULT_CURSO);

        // Validate the Emolumento in Elasticsearch
        verify(mockEmolumentoSearchRepository, times(1)).save(testEmolumento);
    }

    @Test
    @Transactional
    public void createEmolumentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emolumentoRepository.findAll().size();

        // Create the Emolumento with an existing ID
        emolumento.setId(1L);
        EmolumentoDTO emolumentoDTO = emolumentoMapper.toDto(emolumento);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmolumentoMockMvc.perform(post("/api/emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emolumentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Emolumento in the database
        List<Emolumento> emolumentoList = emolumentoRepository.findAll();
        assertThat(emolumentoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Emolumento in Elasticsearch
        verify(mockEmolumentoSearchRepository, times(0)).save(emolumento);
    }


    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = emolumentoRepository.findAll().size();
        // set the field null
        emolumento.setNome(null);

        // Create the Emolumento, which fails.
        EmolumentoDTO emolumentoDTO = emolumentoMapper.toDto(emolumento);

        restEmolumentoMockMvc.perform(post("/api/emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emolumentoDTO)))
            .andExpect(status().isBadRequest());

        List<Emolumento> emolumentoList = emolumentoRepository.findAll();
        assertThat(emolumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = emolumentoRepository.findAll().size();
        // set the field null
        emolumento.setMontante(null);

        // Create the Emolumento, which fails.
        EmolumentoDTO emolumentoDTO = emolumentoMapper.toDto(emolumento);

        restEmolumentoMockMvc.perform(post("/api/emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emolumentoDTO)))
            .andExpect(status().isBadRequest());

        List<Emolumento> emolumentoList = emolumentoRepository.findAll();
        assertThat(emolumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontanteMultaIsRequired() throws Exception {
        int databaseSizeBeforeTest = emolumentoRepository.findAll().size();
        // set the field null
        emolumento.setMontanteMulta(null);

        // Create the Emolumento, which fails.
        EmolumentoDTO emolumentoDTO = emolumentoMapper.toDto(emolumento);

        restEmolumentoMockMvc.perform(post("/api/emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emolumentoDTO)))
            .andExpect(status().isBadRequest());

        List<Emolumento> emolumentoList = emolumentoRepository.findAll();
        assertThat(emolumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTempoMultaIsRequired() throws Exception {
        int databaseSizeBeforeTest = emolumentoRepository.findAll().size();
        // set the field null
        emolumento.setTempoMulta(null);

        // Create the Emolumento, which fails.
        EmolumentoDTO emolumentoDTO = emolumentoMapper.toDto(emolumento);

        restEmolumentoMockMvc.perform(post("/api/emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emolumentoDTO)))
            .andExpect(status().isBadRequest());

        List<Emolumento> emolumentoList = emolumentoRepository.findAll();
        assertThat(emolumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmolumentos() throws Exception {
        // Initialize the database
        emolumentoRepository.saveAndFlush(emolumento);

        // Get all the emolumentoList
        restEmolumentoMockMvc.perform(get("/api/emolumentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emolumento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].montante").value(hasItem(DEFAULT_MONTANTE.intValue())))
            .andExpect(jsonPath("$.[*].montanteMulta").value(hasItem(DEFAULT_MONTANTE_MULTA.doubleValue())))
            .andExpect(jsonPath("$.[*].tempoMulta").value(hasItem(DEFAULT_TEMPO_MULTA)))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE.doubleValue())))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)))
            .andExpect(jsonPath("$.[*].curso").value(hasItem(DEFAULT_CURSO)));
    }
    
    @Test
    @Transactional
    public void getEmolumento() throws Exception {
        // Initialize the database
        emolumentoRepository.saveAndFlush(emolumento);

        // Get the emolumento
        restEmolumentoMockMvc.perform(get("/api/emolumentos/{id}", emolumento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(emolumento.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.montante").value(DEFAULT_MONTANTE.intValue()))
            .andExpect(jsonPath("$.montanteMulta").value(DEFAULT_MONTANTE_MULTA.doubleValue()))
            .andExpect(jsonPath("$.tempoMulta").value(DEFAULT_TEMPO_MULTA))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE.doubleValue()))
            .andExpect(jsonPath("$.turno").value(DEFAULT_TURNO))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE))
            .andExpect(jsonPath("$.curso").value(DEFAULT_CURSO));
    }

    @Test
    @Transactional
    public void getNonExistingEmolumento() throws Exception {
        // Get the emolumento
        restEmolumentoMockMvc.perform(get("/api/emolumentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmolumento() throws Exception {
        // Initialize the database
        emolumentoRepository.saveAndFlush(emolumento);

        int databaseSizeBeforeUpdate = emolumentoRepository.findAll().size();

        // Update the emolumento
        Emolumento updatedEmolumento = emolumentoRepository.findById(emolumento.getId()).get();
        // Disconnect from session so that the updates on updatedEmolumento are not directly saved in db
        em.detach(updatedEmolumento);
        updatedEmolumento
            .nome(UPDATED_NOME)
            .montante(UPDATED_MONTANTE)
            .montanteMulta(UPDATED_MONTANTE_MULTA)
            .tempoMulta(UPDATED_TEMPO_MULTA)
            .quantidade(UPDATED_QUANTIDADE)
            .turno(UPDATED_TURNO)
            .classe(UPDATED_CLASSE)
            .curso(UPDATED_CURSO);
        EmolumentoDTO emolumentoDTO = emolumentoMapper.toDto(updatedEmolumento);

        restEmolumentoMockMvc.perform(put("/api/emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emolumentoDTO)))
            .andExpect(status().isOk());

        // Validate the Emolumento in the database
        List<Emolumento> emolumentoList = emolumentoRepository.findAll();
        assertThat(emolumentoList).hasSize(databaseSizeBeforeUpdate);
        Emolumento testEmolumento = emolumentoList.get(emolumentoList.size() - 1);
        assertThat(testEmolumento.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEmolumento.getMontante()).isEqualTo(UPDATED_MONTANTE);
        assertThat(testEmolumento.getMontanteMulta()).isEqualTo(UPDATED_MONTANTE_MULTA);
        assertThat(testEmolumento.getTempoMulta()).isEqualTo(UPDATED_TEMPO_MULTA);
        assertThat(testEmolumento.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testEmolumento.getTurno()).isEqualTo(UPDATED_TURNO);
        assertThat(testEmolumento.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testEmolumento.getCurso()).isEqualTo(UPDATED_CURSO);

        // Validate the Emolumento in Elasticsearch
        verify(mockEmolumentoSearchRepository, times(1)).save(testEmolumento);
    }

    @Test
    @Transactional
    public void updateNonExistingEmolumento() throws Exception {
        int databaseSizeBeforeUpdate = emolumentoRepository.findAll().size();

        // Create the Emolumento
        EmolumentoDTO emolumentoDTO = emolumentoMapper.toDto(emolumento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmolumentoMockMvc.perform(put("/api/emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emolumentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Emolumento in the database
        List<Emolumento> emolumentoList = emolumentoRepository.findAll();
        assertThat(emolumentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Emolumento in Elasticsearch
        verify(mockEmolumentoSearchRepository, times(0)).save(emolumento);
    }

    @Test
    @Transactional
    public void deleteEmolumento() throws Exception {
        // Initialize the database
        emolumentoRepository.saveAndFlush(emolumento);

        int databaseSizeBeforeDelete = emolumentoRepository.findAll().size();

        // Delete the emolumento
        restEmolumentoMockMvc.perform(delete("/api/emolumentos/{id}", emolumento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Emolumento> emolumentoList = emolumentoRepository.findAll();
        assertThat(emolumentoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Emolumento in Elasticsearch
        verify(mockEmolumentoSearchRepository, times(1)).deleteById(emolumento.getId());
    }

    @Test
    @Transactional
    public void searchEmolumento() throws Exception {
        // Initialize the database
        emolumentoRepository.saveAndFlush(emolumento);
        when(mockEmolumentoSearchRepository.search(queryStringQuery("id:" + emolumento.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(emolumento), PageRequest.of(0, 1), 1));
        // Search the emolumento
        restEmolumentoMockMvc.perform(get("/api/_search/emolumentos?query=id:" + emolumento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emolumento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].montante").value(hasItem(DEFAULT_MONTANTE.intValue())))
            .andExpect(jsonPath("$.[*].montanteMulta").value(hasItem(DEFAULT_MONTANTE_MULTA.doubleValue())))
            .andExpect(jsonPath("$.[*].tempoMulta").value(hasItem(DEFAULT_TEMPO_MULTA)))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE.doubleValue())))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)))
            .andExpect(jsonPath("$.[*].curso").value(hasItem(DEFAULT_CURSO)));
    }
}
