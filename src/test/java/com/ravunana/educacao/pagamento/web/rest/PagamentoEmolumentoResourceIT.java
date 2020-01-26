package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.PagamentoApp;
import com.ravunana.educacao.pagamento.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pagamento.domain.PagamentoEmolumento;
import com.ravunana.educacao.pagamento.domain.FormaLiquidacao;
import com.ravunana.educacao.pagamento.repository.PagamentoEmolumentoRepository;
import com.ravunana.educacao.pagamento.repository.search.PagamentoEmolumentoSearchRepository;
import com.ravunana.educacao.pagamento.service.PagamentoEmolumentoService;
import com.ravunana.educacao.pagamento.service.dto.PagamentoEmolumentoDTO;
import com.ravunana.educacao.pagamento.service.mapper.PagamentoEmolumentoMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.ravunana.educacao.pagamento.web.rest.TestUtil.sameInstant;
import static com.ravunana.educacao.pagamento.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PagamentoEmolumentoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PagamentoApp.class})
public class PagamentoEmolumentoResourceIT {

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_PROCESSO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_PROCESSO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_ALUNO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_ALUNO = "BBBBBBBBBB";

    private static final String DEFAULT_TURMA_ALUNO = "AAAAAAAAAA";
    private static final String UPDATED_TURMA_ALUNO = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    @Autowired
    private PagamentoEmolumentoRepository pagamentoEmolumentoRepository;

    @Autowired
    private PagamentoEmolumentoMapper pagamentoEmolumentoMapper;

    @Autowired
    private PagamentoEmolumentoService pagamentoEmolumentoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pagamento.repository.search test package.
     *
     * @see com.ravunana.educacao.pagamento.repository.search.PagamentoEmolumentoSearchRepositoryMockConfiguration
     */
    @Autowired
    private PagamentoEmolumentoSearchRepository mockPagamentoEmolumentoSearchRepository;

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

    private MockMvc restPagamentoEmolumentoMockMvc;

    private PagamentoEmolumento pagamentoEmolumento;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PagamentoEmolumentoResource pagamentoEmolumentoResource = new PagamentoEmolumentoResource(pagamentoEmolumentoService);
        this.restPagamentoEmolumentoMockMvc = MockMvcBuilders.standaloneSetup(pagamentoEmolumentoResource)
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
    public static PagamentoEmolumento createEntity(EntityManager em) {
        PagamentoEmolumento pagamentoEmolumento = new PagamentoEmolumento()
            .data(DEFAULT_DATA)
            .numero(DEFAULT_NUMERO)
            .numeroProcesso(DEFAULT_NUMERO_PROCESSO)
            .nomeAluno(DEFAULT_NOME_ALUNO)
            .turmaAluno(DEFAULT_TURMA_ALUNO)
            .estado(DEFAULT_ESTADO);
        // Add required entity
        FormaLiquidacao formaLiquidacao;
        if (TestUtil.findAll(em, FormaLiquidacao.class).isEmpty()) {
            formaLiquidacao = FormaLiquidacaoResourceIT.createEntity(em);
            em.persist(formaLiquidacao);
            em.flush();
        } else {
            formaLiquidacao = TestUtil.findAll(em, FormaLiquidacao.class).get(0);
        }
        pagamentoEmolumento.setFormaLiquidacao(formaLiquidacao);
        return pagamentoEmolumento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PagamentoEmolumento createUpdatedEntity(EntityManager em) {
        PagamentoEmolumento pagamentoEmolumento = new PagamentoEmolumento()
            .data(UPDATED_DATA)
            .numero(UPDATED_NUMERO)
            .numeroProcesso(UPDATED_NUMERO_PROCESSO)
            .nomeAluno(UPDATED_NOME_ALUNO)
            .turmaAluno(UPDATED_TURMA_ALUNO)
            .estado(UPDATED_ESTADO);
        // Add required entity
        FormaLiquidacao formaLiquidacao;
        if (TestUtil.findAll(em, FormaLiquidacao.class).isEmpty()) {
            formaLiquidacao = FormaLiquidacaoResourceIT.createUpdatedEntity(em);
            em.persist(formaLiquidacao);
            em.flush();
        } else {
            formaLiquidacao = TestUtil.findAll(em, FormaLiquidacao.class).get(0);
        }
        pagamentoEmolumento.setFormaLiquidacao(formaLiquidacao);
        return pagamentoEmolumento;
    }

    @BeforeEach
    public void initTest() {
        pagamentoEmolumento = createEntity(em);
    }

    @Test
    @Transactional
    public void createPagamentoEmolumento() throws Exception {
        int databaseSizeBeforeCreate = pagamentoEmolumentoRepository.findAll().size();

        // Create the PagamentoEmolumento
        PagamentoEmolumentoDTO pagamentoEmolumentoDTO = pagamentoEmolumentoMapper.toDto(pagamentoEmolumento);
        restPagamentoEmolumentoMockMvc.perform(post("/api/pagamento-emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagamentoEmolumentoDTO)))
            .andExpect(status().isCreated());

        // Validate the PagamentoEmolumento in the database
        List<PagamentoEmolumento> pagamentoEmolumentoList = pagamentoEmolumentoRepository.findAll();
        assertThat(pagamentoEmolumentoList).hasSize(databaseSizeBeforeCreate + 1);
        PagamentoEmolumento testPagamentoEmolumento = pagamentoEmolumentoList.get(pagamentoEmolumentoList.size() - 1);
        assertThat(testPagamentoEmolumento.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testPagamentoEmolumento.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testPagamentoEmolumento.getNumeroProcesso()).isEqualTo(DEFAULT_NUMERO_PROCESSO);
        assertThat(testPagamentoEmolumento.getNomeAluno()).isEqualTo(DEFAULT_NOME_ALUNO);
        assertThat(testPagamentoEmolumento.getTurmaAluno()).isEqualTo(DEFAULT_TURMA_ALUNO);
        assertThat(testPagamentoEmolumento.getEstado()).isEqualTo(DEFAULT_ESTADO);

        // Validate the PagamentoEmolumento in Elasticsearch
        verify(mockPagamentoEmolumentoSearchRepository, times(1)).save(testPagamentoEmolumento);
    }

    @Test
    @Transactional
    public void createPagamentoEmolumentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pagamentoEmolumentoRepository.findAll().size();

        // Create the PagamentoEmolumento with an existing ID
        pagamentoEmolumento.setId(1L);
        PagamentoEmolumentoDTO pagamentoEmolumentoDTO = pagamentoEmolumentoMapper.toDto(pagamentoEmolumento);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagamentoEmolumentoMockMvc.perform(post("/api/pagamento-emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagamentoEmolumentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PagamentoEmolumento in the database
        List<PagamentoEmolumento> pagamentoEmolumentoList = pagamentoEmolumentoRepository.findAll();
        assertThat(pagamentoEmolumentoList).hasSize(databaseSizeBeforeCreate);

        // Validate the PagamentoEmolumento in Elasticsearch
        verify(mockPagamentoEmolumentoSearchRepository, times(0)).save(pagamentoEmolumento);
    }


    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagamentoEmolumentoRepository.findAll().size();
        // set the field null
        pagamentoEmolumento.setNumero(null);

        // Create the PagamentoEmolumento, which fails.
        PagamentoEmolumentoDTO pagamentoEmolumentoDTO = pagamentoEmolumentoMapper.toDto(pagamentoEmolumento);

        restPagamentoEmolumentoMockMvc.perform(post("/api/pagamento-emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagamentoEmolumentoDTO)))
            .andExpect(status().isBadRequest());

        List<PagamentoEmolumento> pagamentoEmolumentoList = pagamentoEmolumentoRepository.findAll();
        assertThat(pagamentoEmolumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroProcessoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagamentoEmolumentoRepository.findAll().size();
        // set the field null
        pagamentoEmolumento.setNumeroProcesso(null);

        // Create the PagamentoEmolumento, which fails.
        PagamentoEmolumentoDTO pagamentoEmolumentoDTO = pagamentoEmolumentoMapper.toDto(pagamentoEmolumento);

        restPagamentoEmolumentoMockMvc.perform(post("/api/pagamento-emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagamentoEmolumentoDTO)))
            .andExpect(status().isBadRequest());

        List<PagamentoEmolumento> pagamentoEmolumentoList = pagamentoEmolumentoRepository.findAll();
        assertThat(pagamentoEmolumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomeAlunoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagamentoEmolumentoRepository.findAll().size();
        // set the field null
        pagamentoEmolumento.setNomeAluno(null);

        // Create the PagamentoEmolumento, which fails.
        PagamentoEmolumentoDTO pagamentoEmolumentoDTO = pagamentoEmolumentoMapper.toDto(pagamentoEmolumento);

        restPagamentoEmolumentoMockMvc.perform(post("/api/pagamento-emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagamentoEmolumentoDTO)))
            .andExpect(status().isBadRequest());

        List<PagamentoEmolumento> pagamentoEmolumentoList = pagamentoEmolumentoRepository.findAll();
        assertThat(pagamentoEmolumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTurmaAlunoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagamentoEmolumentoRepository.findAll().size();
        // set the field null
        pagamentoEmolumento.setTurmaAluno(null);

        // Create the PagamentoEmolumento, which fails.
        PagamentoEmolumentoDTO pagamentoEmolumentoDTO = pagamentoEmolumentoMapper.toDto(pagamentoEmolumento);

        restPagamentoEmolumentoMockMvc.perform(post("/api/pagamento-emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagamentoEmolumentoDTO)))
            .andExpect(status().isBadRequest());

        List<PagamentoEmolumento> pagamentoEmolumentoList = pagamentoEmolumentoRepository.findAll();
        assertThat(pagamentoEmolumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPagamentoEmolumentos() throws Exception {
        // Initialize the database
        pagamentoEmolumentoRepository.saveAndFlush(pagamentoEmolumento);

        // Get all the pagamentoEmolumentoList
        restPagamentoEmolumentoMockMvc.perform(get("/api/pagamento-emolumentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pagamentoEmolumento.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)))
            .andExpect(jsonPath("$.[*].nomeAluno").value(hasItem(DEFAULT_NOME_ALUNO)))
            .andExpect(jsonPath("$.[*].turmaAluno").value(hasItem(DEFAULT_TURMA_ALUNO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }
    
    @Test
    @Transactional
    public void getPagamentoEmolumento() throws Exception {
        // Initialize the database
        pagamentoEmolumentoRepository.saveAndFlush(pagamentoEmolumento);

        // Get the pagamentoEmolumento
        restPagamentoEmolumentoMockMvc.perform(get("/api/pagamento-emolumentos/{id}", pagamentoEmolumento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pagamentoEmolumento.getId().intValue()))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.numeroProcesso").value(DEFAULT_NUMERO_PROCESSO))
            .andExpect(jsonPath("$.nomeAluno").value(DEFAULT_NOME_ALUNO))
            .andExpect(jsonPath("$.turmaAluno").value(DEFAULT_TURMA_ALUNO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    public void getNonExistingPagamentoEmolumento() throws Exception {
        // Get the pagamentoEmolumento
        restPagamentoEmolumentoMockMvc.perform(get("/api/pagamento-emolumentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePagamentoEmolumento() throws Exception {
        // Initialize the database
        pagamentoEmolumentoRepository.saveAndFlush(pagamentoEmolumento);

        int databaseSizeBeforeUpdate = pagamentoEmolumentoRepository.findAll().size();

        // Update the pagamentoEmolumento
        PagamentoEmolumento updatedPagamentoEmolumento = pagamentoEmolumentoRepository.findById(pagamentoEmolumento.getId()).get();
        // Disconnect from session so that the updates on updatedPagamentoEmolumento are not directly saved in db
        em.detach(updatedPagamentoEmolumento);
        updatedPagamentoEmolumento
            .data(UPDATED_DATA)
            .numero(UPDATED_NUMERO)
            .numeroProcesso(UPDATED_NUMERO_PROCESSO)
            .nomeAluno(UPDATED_NOME_ALUNO)
            .turmaAluno(UPDATED_TURMA_ALUNO)
            .estado(UPDATED_ESTADO);
        PagamentoEmolumentoDTO pagamentoEmolumentoDTO = pagamentoEmolumentoMapper.toDto(updatedPagamentoEmolumento);

        restPagamentoEmolumentoMockMvc.perform(put("/api/pagamento-emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagamentoEmolumentoDTO)))
            .andExpect(status().isOk());

        // Validate the PagamentoEmolumento in the database
        List<PagamentoEmolumento> pagamentoEmolumentoList = pagamentoEmolumentoRepository.findAll();
        assertThat(pagamentoEmolumentoList).hasSize(databaseSizeBeforeUpdate);
        PagamentoEmolumento testPagamentoEmolumento = pagamentoEmolumentoList.get(pagamentoEmolumentoList.size() - 1);
        assertThat(testPagamentoEmolumento.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testPagamentoEmolumento.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPagamentoEmolumento.getNumeroProcesso()).isEqualTo(UPDATED_NUMERO_PROCESSO);
        assertThat(testPagamentoEmolumento.getNomeAluno()).isEqualTo(UPDATED_NOME_ALUNO);
        assertThat(testPagamentoEmolumento.getTurmaAluno()).isEqualTo(UPDATED_TURMA_ALUNO);
        assertThat(testPagamentoEmolumento.getEstado()).isEqualTo(UPDATED_ESTADO);

        // Validate the PagamentoEmolumento in Elasticsearch
        verify(mockPagamentoEmolumentoSearchRepository, times(1)).save(testPagamentoEmolumento);
    }

    @Test
    @Transactional
    public void updateNonExistingPagamentoEmolumento() throws Exception {
        int databaseSizeBeforeUpdate = pagamentoEmolumentoRepository.findAll().size();

        // Create the PagamentoEmolumento
        PagamentoEmolumentoDTO pagamentoEmolumentoDTO = pagamentoEmolumentoMapper.toDto(pagamentoEmolumento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagamentoEmolumentoMockMvc.perform(put("/api/pagamento-emolumentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagamentoEmolumentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PagamentoEmolumento in the database
        List<PagamentoEmolumento> pagamentoEmolumentoList = pagamentoEmolumentoRepository.findAll();
        assertThat(pagamentoEmolumentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PagamentoEmolumento in Elasticsearch
        verify(mockPagamentoEmolumentoSearchRepository, times(0)).save(pagamentoEmolumento);
    }

    @Test
    @Transactional
    public void deletePagamentoEmolumento() throws Exception {
        // Initialize the database
        pagamentoEmolumentoRepository.saveAndFlush(pagamentoEmolumento);

        int databaseSizeBeforeDelete = pagamentoEmolumentoRepository.findAll().size();

        // Delete the pagamentoEmolumento
        restPagamentoEmolumentoMockMvc.perform(delete("/api/pagamento-emolumentos/{id}", pagamentoEmolumento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PagamentoEmolumento> pagamentoEmolumentoList = pagamentoEmolumentoRepository.findAll();
        assertThat(pagamentoEmolumentoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PagamentoEmolumento in Elasticsearch
        verify(mockPagamentoEmolumentoSearchRepository, times(1)).deleteById(pagamentoEmolumento.getId());
    }

    @Test
    @Transactional
    public void searchPagamentoEmolumento() throws Exception {
        // Initialize the database
        pagamentoEmolumentoRepository.saveAndFlush(pagamentoEmolumento);
        when(mockPagamentoEmolumentoSearchRepository.search(queryStringQuery("id:" + pagamentoEmolumento.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(pagamentoEmolumento), PageRequest.of(0, 1), 1));
        // Search the pagamentoEmolumento
        restPagamentoEmolumentoMockMvc.perform(get("/api/_search/pagamento-emolumentos?query=id:" + pagamentoEmolumento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pagamentoEmolumento.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)))
            .andExpect(jsonPath("$.[*].nomeAluno").value(hasItem(DEFAULT_NOME_ALUNO)))
            .andExpect(jsonPath("$.[*].turmaAluno").value(hasItem(DEFAULT_TURMA_ALUNO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }
}
