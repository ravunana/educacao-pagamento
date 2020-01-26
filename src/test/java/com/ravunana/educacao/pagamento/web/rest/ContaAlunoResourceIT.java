package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.PagamentoApp;
import com.ravunana.educacao.pagamento.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pagamento.domain.ContaAluno;
import com.ravunana.educacao.pagamento.repository.ContaAlunoRepository;
import com.ravunana.educacao.pagamento.repository.search.ContaAlunoSearchRepository;
import com.ravunana.educacao.pagamento.service.ContaAlunoService;
import com.ravunana.educacao.pagamento.service.dto.ContaAlunoDTO;
import com.ravunana.educacao.pagamento.service.mapper.ContaAlunoMapper;
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
 * Integration tests for the {@link ContaAlunoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PagamentoApp.class})
public class ContaAlunoResourceIT {

    private static final BigDecimal DEFAULT_DEBITO = new BigDecimal(0);
    private static final BigDecimal UPDATED_DEBITO = new BigDecimal(1);

    private static final BigDecimal DEFAULT_CREDITO = new BigDecimal(0);
    private static final BigDecimal UPDATED_CREDITO = new BigDecimal(1);

    private static final String DEFAULT_NUMERO_PROCESSO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_PROCESSO = "BBBBBBBBBB";

    @Autowired
    private ContaAlunoRepository contaAlunoRepository;

    @Autowired
    private ContaAlunoMapper contaAlunoMapper;

    @Autowired
    private ContaAlunoService contaAlunoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pagamento.repository.search test package.
     *
     * @see com.ravunana.educacao.pagamento.repository.search.ContaAlunoSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContaAlunoSearchRepository mockContaAlunoSearchRepository;

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

    private MockMvc restContaAlunoMockMvc;

    private ContaAluno contaAluno;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContaAlunoResource contaAlunoResource = new ContaAlunoResource(contaAlunoService);
        this.restContaAlunoMockMvc = MockMvcBuilders.standaloneSetup(contaAlunoResource)
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
    public static ContaAluno createEntity(EntityManager em) {
        ContaAluno contaAluno = new ContaAluno()
            .debito(DEFAULT_DEBITO)
            .credito(DEFAULT_CREDITO)
            .numeroProcesso(DEFAULT_NUMERO_PROCESSO);
        return contaAluno;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaAluno createUpdatedEntity(EntityManager em) {
        ContaAluno contaAluno = new ContaAluno()
            .debito(UPDATED_DEBITO)
            .credito(UPDATED_CREDITO)
            .numeroProcesso(UPDATED_NUMERO_PROCESSO);
        return contaAluno;
    }

    @BeforeEach
    public void initTest() {
        contaAluno = createEntity(em);
    }

    @Test
    @Transactional
    public void createContaAluno() throws Exception {
        int databaseSizeBeforeCreate = contaAlunoRepository.findAll().size();

        // Create the ContaAluno
        ContaAlunoDTO contaAlunoDTO = contaAlunoMapper.toDto(contaAluno);
        restContaAlunoMockMvc.perform(post("/api/conta-alunos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaAlunoDTO)))
            .andExpect(status().isCreated());

        // Validate the ContaAluno in the database
        List<ContaAluno> contaAlunoList = contaAlunoRepository.findAll();
        assertThat(contaAlunoList).hasSize(databaseSizeBeforeCreate + 1);
        ContaAluno testContaAluno = contaAlunoList.get(contaAlunoList.size() - 1);
        assertThat(testContaAluno.getDebito()).isEqualTo(DEFAULT_DEBITO);
        assertThat(testContaAluno.getCredito()).isEqualTo(DEFAULT_CREDITO);
        assertThat(testContaAluno.getNumeroProcesso()).isEqualTo(DEFAULT_NUMERO_PROCESSO);

        // Validate the ContaAluno in Elasticsearch
        verify(mockContaAlunoSearchRepository, times(1)).save(testContaAluno);
    }

    @Test
    @Transactional
    public void createContaAlunoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contaAlunoRepository.findAll().size();

        // Create the ContaAluno with an existing ID
        contaAluno.setId(1L);
        ContaAlunoDTO contaAlunoDTO = contaAlunoMapper.toDto(contaAluno);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaAlunoMockMvc.perform(post("/api/conta-alunos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaAlunoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContaAluno in the database
        List<ContaAluno> contaAlunoList = contaAlunoRepository.findAll();
        assertThat(contaAlunoList).hasSize(databaseSizeBeforeCreate);

        // Validate the ContaAluno in Elasticsearch
        verify(mockContaAlunoSearchRepository, times(0)).save(contaAluno);
    }


    @Test
    @Transactional
    public void checkDebitoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaAlunoRepository.findAll().size();
        // set the field null
        contaAluno.setDebito(null);

        // Create the ContaAluno, which fails.
        ContaAlunoDTO contaAlunoDTO = contaAlunoMapper.toDto(contaAluno);

        restContaAlunoMockMvc.perform(post("/api/conta-alunos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaAlunoDTO)))
            .andExpect(status().isBadRequest());

        List<ContaAluno> contaAlunoList = contaAlunoRepository.findAll();
        assertThat(contaAlunoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreditoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaAlunoRepository.findAll().size();
        // set the field null
        contaAluno.setCredito(null);

        // Create the ContaAluno, which fails.
        ContaAlunoDTO contaAlunoDTO = contaAlunoMapper.toDto(contaAluno);

        restContaAlunoMockMvc.perform(post("/api/conta-alunos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaAlunoDTO)))
            .andExpect(status().isBadRequest());

        List<ContaAluno> contaAlunoList = contaAlunoRepository.findAll();
        assertThat(contaAlunoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroProcessoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaAlunoRepository.findAll().size();
        // set the field null
        contaAluno.setNumeroProcesso(null);

        // Create the ContaAluno, which fails.
        ContaAlunoDTO contaAlunoDTO = contaAlunoMapper.toDto(contaAluno);

        restContaAlunoMockMvc.perform(post("/api/conta-alunos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaAlunoDTO)))
            .andExpect(status().isBadRequest());

        List<ContaAluno> contaAlunoList = contaAlunoRepository.findAll();
        assertThat(contaAlunoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContaAlunos() throws Exception {
        // Initialize the database
        contaAlunoRepository.saveAndFlush(contaAluno);

        // Get all the contaAlunoList
        restContaAlunoMockMvc.perform(get("/api/conta-alunos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaAluno.getId().intValue())))
            .andExpect(jsonPath("$.[*].debito").value(hasItem(DEFAULT_DEBITO.intValue())))
            .andExpect(jsonPath("$.[*].credito").value(hasItem(DEFAULT_CREDITO.intValue())))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)));
    }
    
    @Test
    @Transactional
    public void getContaAluno() throws Exception {
        // Initialize the database
        contaAlunoRepository.saveAndFlush(contaAluno);

        // Get the contaAluno
        restContaAlunoMockMvc.perform(get("/api/conta-alunos/{id}", contaAluno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contaAluno.getId().intValue()))
            .andExpect(jsonPath("$.debito").value(DEFAULT_DEBITO.intValue()))
            .andExpect(jsonPath("$.credito").value(DEFAULT_CREDITO.intValue()))
            .andExpect(jsonPath("$.numeroProcesso").value(DEFAULT_NUMERO_PROCESSO));
    }

    @Test
    @Transactional
    public void getNonExistingContaAluno() throws Exception {
        // Get the contaAluno
        restContaAlunoMockMvc.perform(get("/api/conta-alunos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContaAluno() throws Exception {
        // Initialize the database
        contaAlunoRepository.saveAndFlush(contaAluno);

        int databaseSizeBeforeUpdate = contaAlunoRepository.findAll().size();

        // Update the contaAluno
        ContaAluno updatedContaAluno = contaAlunoRepository.findById(contaAluno.getId()).get();
        // Disconnect from session so that the updates on updatedContaAluno are not directly saved in db
        em.detach(updatedContaAluno);
        updatedContaAluno
            .debito(UPDATED_DEBITO)
            .credito(UPDATED_CREDITO)
            .numeroProcesso(UPDATED_NUMERO_PROCESSO);
        ContaAlunoDTO contaAlunoDTO = contaAlunoMapper.toDto(updatedContaAluno);

        restContaAlunoMockMvc.perform(put("/api/conta-alunos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaAlunoDTO)))
            .andExpect(status().isOk());

        // Validate the ContaAluno in the database
        List<ContaAluno> contaAlunoList = contaAlunoRepository.findAll();
        assertThat(contaAlunoList).hasSize(databaseSizeBeforeUpdate);
        ContaAluno testContaAluno = contaAlunoList.get(contaAlunoList.size() - 1);
        assertThat(testContaAluno.getDebito()).isEqualTo(UPDATED_DEBITO);
        assertThat(testContaAluno.getCredito()).isEqualTo(UPDATED_CREDITO);
        assertThat(testContaAluno.getNumeroProcesso()).isEqualTo(UPDATED_NUMERO_PROCESSO);

        // Validate the ContaAluno in Elasticsearch
        verify(mockContaAlunoSearchRepository, times(1)).save(testContaAluno);
    }

    @Test
    @Transactional
    public void updateNonExistingContaAluno() throws Exception {
        int databaseSizeBeforeUpdate = contaAlunoRepository.findAll().size();

        // Create the ContaAluno
        ContaAlunoDTO contaAlunoDTO = contaAlunoMapper.toDto(contaAluno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaAlunoMockMvc.perform(put("/api/conta-alunos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaAlunoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContaAluno in the database
        List<ContaAluno> contaAlunoList = contaAlunoRepository.findAll();
        assertThat(contaAlunoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContaAluno in Elasticsearch
        verify(mockContaAlunoSearchRepository, times(0)).save(contaAluno);
    }

    @Test
    @Transactional
    public void deleteContaAluno() throws Exception {
        // Initialize the database
        contaAlunoRepository.saveAndFlush(contaAluno);

        int databaseSizeBeforeDelete = contaAlunoRepository.findAll().size();

        // Delete the contaAluno
        restContaAlunoMockMvc.perform(delete("/api/conta-alunos/{id}", contaAluno.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContaAluno> contaAlunoList = contaAlunoRepository.findAll();
        assertThat(contaAlunoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ContaAluno in Elasticsearch
        verify(mockContaAlunoSearchRepository, times(1)).deleteById(contaAluno.getId());
    }

    @Test
    @Transactional
    public void searchContaAluno() throws Exception {
        // Initialize the database
        contaAlunoRepository.saveAndFlush(contaAluno);
        when(mockContaAlunoSearchRepository.search(queryStringQuery("id:" + contaAluno.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(contaAluno), PageRequest.of(0, 1), 1));
        // Search the contaAluno
        restContaAlunoMockMvc.perform(get("/api/_search/conta-alunos?query=id:" + contaAluno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaAluno.getId().intValue())))
            .andExpect(jsonPath("$.[*].debito").value(hasItem(DEFAULT_DEBITO.intValue())))
            .andExpect(jsonPath("$.[*].credito").value(hasItem(DEFAULT_CREDITO.intValue())))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)));
    }
}
