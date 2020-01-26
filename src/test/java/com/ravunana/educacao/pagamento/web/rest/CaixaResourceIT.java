package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.PagamentoApp;
import com.ravunana.educacao.pagamento.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pagamento.domain.Caixa;
import com.ravunana.educacao.pagamento.repository.CaixaRepository;
import com.ravunana.educacao.pagamento.repository.search.CaixaSearchRepository;
import com.ravunana.educacao.pagamento.service.CaixaService;
import com.ravunana.educacao.pagamento.service.dto.CaixaDTO;
import com.ravunana.educacao.pagamento.service.mapper.CaixaMapper;
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
 * Integration tests for the {@link CaixaResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PagamentoApp.class})
public class CaixaResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_PROPRIETARIO = "AAAAAAAAAA";
    private static final String UPDATED_PROPRIETARIO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_CONTA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_CONTA = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    @Autowired
    private CaixaRepository caixaRepository;

    @Autowired
    private CaixaMapper caixaMapper;

    @Autowired
    private CaixaService caixaService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pagamento.repository.search test package.
     *
     * @see com.ravunana.educacao.pagamento.repository.search.CaixaSearchRepositoryMockConfiguration
     */
    @Autowired
    private CaixaSearchRepository mockCaixaSearchRepository;

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

    private MockMvc restCaixaMockMvc;

    private Caixa caixa;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CaixaResource caixaResource = new CaixaResource(caixaService);
        this.restCaixaMockMvc = MockMvcBuilders.standaloneSetup(caixaResource)
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
    public static Caixa createEntity(EntityManager em) {
        Caixa caixa = new Caixa()
            .descricao(DEFAULT_DESCRICAO)
            .proprietario(DEFAULT_PROPRIETARIO)
            .numeroConta(DEFAULT_NUMERO_CONTA)
            .iban(DEFAULT_IBAN)
            .ativo(DEFAULT_ATIVO);
        return caixa;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caixa createUpdatedEntity(EntityManager em) {
        Caixa caixa = new Caixa()
            .descricao(UPDATED_DESCRICAO)
            .proprietario(UPDATED_PROPRIETARIO)
            .numeroConta(UPDATED_NUMERO_CONTA)
            .iban(UPDATED_IBAN)
            .ativo(UPDATED_ATIVO);
        return caixa;
    }

    @BeforeEach
    public void initTest() {
        caixa = createEntity(em);
    }

    @Test
    @Transactional
    public void createCaixa() throws Exception {
        int databaseSizeBeforeCreate = caixaRepository.findAll().size();

        // Create the Caixa
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);
        restCaixaMockMvc.perform(post("/api/caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isCreated());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeCreate + 1);
        Caixa testCaixa = caixaList.get(caixaList.size() - 1);
        assertThat(testCaixa.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testCaixa.getProprietario()).isEqualTo(DEFAULT_PROPRIETARIO);
        assertThat(testCaixa.getNumeroConta()).isEqualTo(DEFAULT_NUMERO_CONTA);
        assertThat(testCaixa.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testCaixa.isAtivo()).isEqualTo(DEFAULT_ATIVO);

        // Validate the Caixa in Elasticsearch
        verify(mockCaixaSearchRepository, times(1)).save(testCaixa);
    }

    @Test
    @Transactional
    public void createCaixaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = caixaRepository.findAll().size();

        // Create the Caixa with an existing ID
        caixa.setId(1L);
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaixaMockMvc.perform(post("/api/caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Caixa in Elasticsearch
        verify(mockCaixaSearchRepository, times(0)).save(caixa);
    }


    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = caixaRepository.findAll().size();
        // set the field null
        caixa.setDescricao(null);

        // Create the Caixa, which fails.
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        restCaixaMockMvc.perform(post("/api/caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isBadRequest());

        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProprietarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = caixaRepository.findAll().size();
        // set the field null
        caixa.setProprietario(null);

        // Create the Caixa, which fails.
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        restCaixaMockMvc.perform(post("/api/caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isBadRequest());

        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroContaIsRequired() throws Exception {
        int databaseSizeBeforeTest = caixaRepository.findAll().size();
        // set the field null
        caixa.setNumeroConta(null);

        // Create the Caixa, which fails.
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        restCaixaMockMvc.perform(post("/api/caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isBadRequest());

        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAtivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = caixaRepository.findAll().size();
        // set the field null
        caixa.setAtivo(null);

        // Create the Caixa, which fails.
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        restCaixaMockMvc.perform(post("/api/caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isBadRequest());

        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCaixas() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        // Get all the caixaList
        restCaixaMockMvc.perform(get("/api/caixas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caixa.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].proprietario").value(hasItem(DEFAULT_PROPRIETARIO)))
            .andExpect(jsonPath("$.[*].numeroConta").value(hasItem(DEFAULT_NUMERO_CONTA)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCaixa() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        // Get the caixa
        restCaixaMockMvc.perform(get("/api/caixas/{id}", caixa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(caixa.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.proprietario").value(DEFAULT_PROPRIETARIO))
            .andExpect(jsonPath("$.numeroConta").value(DEFAULT_NUMERO_CONTA))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCaixa() throws Exception {
        // Get the caixa
        restCaixaMockMvc.perform(get("/api/caixas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCaixa() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();

        // Update the caixa
        Caixa updatedCaixa = caixaRepository.findById(caixa.getId()).get();
        // Disconnect from session so that the updates on updatedCaixa are not directly saved in db
        em.detach(updatedCaixa);
        updatedCaixa
            .descricao(UPDATED_DESCRICAO)
            .proprietario(UPDATED_PROPRIETARIO)
            .numeroConta(UPDATED_NUMERO_CONTA)
            .iban(UPDATED_IBAN)
            .ativo(UPDATED_ATIVO);
        CaixaDTO caixaDTO = caixaMapper.toDto(updatedCaixa);

        restCaixaMockMvc.perform(put("/api/caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isOk());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);
        Caixa testCaixa = caixaList.get(caixaList.size() - 1);
        assertThat(testCaixa.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testCaixa.getProprietario()).isEqualTo(UPDATED_PROPRIETARIO);
        assertThat(testCaixa.getNumeroConta()).isEqualTo(UPDATED_NUMERO_CONTA);
        assertThat(testCaixa.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testCaixa.isAtivo()).isEqualTo(UPDATED_ATIVO);

        // Validate the Caixa in Elasticsearch
        verify(mockCaixaSearchRepository, times(1)).save(testCaixa);
    }

    @Test
    @Transactional
    public void updateNonExistingCaixa() throws Exception {
        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();

        // Create the Caixa
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaixaMockMvc.perform(put("/api/caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Caixa in Elasticsearch
        verify(mockCaixaSearchRepository, times(0)).save(caixa);
    }

    @Test
    @Transactional
    public void deleteCaixa() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        int databaseSizeBeforeDelete = caixaRepository.findAll().size();

        // Delete the caixa
        restCaixaMockMvc.perform(delete("/api/caixas/{id}", caixa.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Caixa in Elasticsearch
        verify(mockCaixaSearchRepository, times(1)).deleteById(caixa.getId());
    }

    @Test
    @Transactional
    public void searchCaixa() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);
        when(mockCaixaSearchRepository.search(queryStringQuery("id:" + caixa.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(caixa), PageRequest.of(0, 1), 1));
        // Search the caixa
        restCaixaMockMvc.perform(get("/api/_search/caixas?query=id:" + caixa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caixa.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].proprietario").value(hasItem(DEFAULT_PROPRIETARIO)))
            .andExpect(jsonPath("$.[*].numeroConta").value(hasItem(DEFAULT_NUMERO_CONTA)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }
}
