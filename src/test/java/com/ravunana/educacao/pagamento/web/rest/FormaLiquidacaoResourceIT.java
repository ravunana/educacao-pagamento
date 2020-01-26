package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.PagamentoApp;
import com.ravunana.educacao.pagamento.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pagamento.domain.FormaLiquidacao;
import com.ravunana.educacao.pagamento.repository.FormaLiquidacaoRepository;
import com.ravunana.educacao.pagamento.repository.search.FormaLiquidacaoSearchRepository;
import com.ravunana.educacao.pagamento.service.FormaLiquidacaoService;
import com.ravunana.educacao.pagamento.service.dto.FormaLiquidacaoDTO;
import com.ravunana.educacao.pagamento.service.mapper.FormaLiquidacaoMapper;
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
 * Integration tests for the {@link FormaLiquidacaoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PagamentoApp.class})
public class FormaLiquidacaoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Double DEFAULT_JURO = 0D;
    private static final Double UPDATED_JURO = 1D;

    private static final Integer DEFAULT_PRAZO_LIQUIDACAO = 0;
    private static final Integer UPDATED_PRAZO_LIQUIDACAO = 1;

    private static final Integer DEFAULT_QUANTIDADE = 1;
    private static final Integer UPDATED_QUANTIDADE = 2;

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    @Autowired
    private FormaLiquidacaoRepository formaLiquidacaoRepository;

    @Autowired
    private FormaLiquidacaoMapper formaLiquidacaoMapper;

    @Autowired
    private FormaLiquidacaoService formaLiquidacaoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pagamento.repository.search test package.
     *
     * @see com.ravunana.educacao.pagamento.repository.search.FormaLiquidacaoSearchRepositoryMockConfiguration
     */
    @Autowired
    private FormaLiquidacaoSearchRepository mockFormaLiquidacaoSearchRepository;

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

    private MockMvc restFormaLiquidacaoMockMvc;

    private FormaLiquidacao formaLiquidacao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormaLiquidacaoResource formaLiquidacaoResource = new FormaLiquidacaoResource(formaLiquidacaoService);
        this.restFormaLiquidacaoMockMvc = MockMvcBuilders.standaloneSetup(formaLiquidacaoResource)
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
    public static FormaLiquidacao createEntity(EntityManager em) {
        FormaLiquidacao formaLiquidacao = new FormaLiquidacao()
            .nome(DEFAULT_NOME)
            .juro(DEFAULT_JURO)
            .prazoLiquidacao(DEFAULT_PRAZO_LIQUIDACAO)
            .quantidade(DEFAULT_QUANTIDADE)
            .icon(DEFAULT_ICON);
        return formaLiquidacao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormaLiquidacao createUpdatedEntity(EntityManager em) {
        FormaLiquidacao formaLiquidacao = new FormaLiquidacao()
            .nome(UPDATED_NOME)
            .juro(UPDATED_JURO)
            .prazoLiquidacao(UPDATED_PRAZO_LIQUIDACAO)
            .quantidade(UPDATED_QUANTIDADE)
            .icon(UPDATED_ICON);
        return formaLiquidacao;
    }

    @BeforeEach
    public void initTest() {
        formaLiquidacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormaLiquidacao() throws Exception {
        int databaseSizeBeforeCreate = formaLiquidacaoRepository.findAll().size();

        // Create the FormaLiquidacao
        FormaLiquidacaoDTO formaLiquidacaoDTO = formaLiquidacaoMapper.toDto(formaLiquidacao);
        restFormaLiquidacaoMockMvc.perform(post("/api/forma-liquidacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaLiquidacaoDTO)))
            .andExpect(status().isCreated());

        // Validate the FormaLiquidacao in the database
        List<FormaLiquidacao> formaLiquidacaoList = formaLiquidacaoRepository.findAll();
        assertThat(formaLiquidacaoList).hasSize(databaseSizeBeforeCreate + 1);
        FormaLiquidacao testFormaLiquidacao = formaLiquidacaoList.get(formaLiquidacaoList.size() - 1);
        assertThat(testFormaLiquidacao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFormaLiquidacao.getJuro()).isEqualTo(DEFAULT_JURO);
        assertThat(testFormaLiquidacao.getPrazoLiquidacao()).isEqualTo(DEFAULT_PRAZO_LIQUIDACAO);
        assertThat(testFormaLiquidacao.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
        assertThat(testFormaLiquidacao.getIcon()).isEqualTo(DEFAULT_ICON);

        // Validate the FormaLiquidacao in Elasticsearch
        verify(mockFormaLiquidacaoSearchRepository, times(1)).save(testFormaLiquidacao);
    }

    @Test
    @Transactional
    public void createFormaLiquidacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formaLiquidacaoRepository.findAll().size();

        // Create the FormaLiquidacao with an existing ID
        formaLiquidacao.setId(1L);
        FormaLiquidacaoDTO formaLiquidacaoDTO = formaLiquidacaoMapper.toDto(formaLiquidacao);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormaLiquidacaoMockMvc.perform(post("/api/forma-liquidacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaLiquidacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FormaLiquidacao in the database
        List<FormaLiquidacao> formaLiquidacaoList = formaLiquidacaoRepository.findAll();
        assertThat(formaLiquidacaoList).hasSize(databaseSizeBeforeCreate);

        // Validate the FormaLiquidacao in Elasticsearch
        verify(mockFormaLiquidacaoSearchRepository, times(0)).save(formaLiquidacao);
    }


    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = formaLiquidacaoRepository.findAll().size();
        // set the field null
        formaLiquidacao.setNome(null);

        // Create the FormaLiquidacao, which fails.
        FormaLiquidacaoDTO formaLiquidacaoDTO = formaLiquidacaoMapper.toDto(formaLiquidacao);

        restFormaLiquidacaoMockMvc.perform(post("/api/forma-liquidacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaLiquidacaoDTO)))
            .andExpect(status().isBadRequest());

        List<FormaLiquidacao> formaLiquidacaoList = formaLiquidacaoRepository.findAll();
        assertThat(formaLiquidacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJuroIsRequired() throws Exception {
        int databaseSizeBeforeTest = formaLiquidacaoRepository.findAll().size();
        // set the field null
        formaLiquidacao.setJuro(null);

        // Create the FormaLiquidacao, which fails.
        FormaLiquidacaoDTO formaLiquidacaoDTO = formaLiquidacaoMapper.toDto(formaLiquidacao);

        restFormaLiquidacaoMockMvc.perform(post("/api/forma-liquidacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaLiquidacaoDTO)))
            .andExpect(status().isBadRequest());

        List<FormaLiquidacao> formaLiquidacaoList = formaLiquidacaoRepository.findAll();
        assertThat(formaLiquidacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrazoLiquidacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = formaLiquidacaoRepository.findAll().size();
        // set the field null
        formaLiquidacao.setPrazoLiquidacao(null);

        // Create the FormaLiquidacao, which fails.
        FormaLiquidacaoDTO formaLiquidacaoDTO = formaLiquidacaoMapper.toDto(formaLiquidacao);

        restFormaLiquidacaoMockMvc.perform(post("/api/forma-liquidacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaLiquidacaoDTO)))
            .andExpect(status().isBadRequest());

        List<FormaLiquidacao> formaLiquidacaoList = formaLiquidacaoRepository.findAll();
        assertThat(formaLiquidacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = formaLiquidacaoRepository.findAll().size();
        // set the field null
        formaLiquidacao.setQuantidade(null);

        // Create the FormaLiquidacao, which fails.
        FormaLiquidacaoDTO formaLiquidacaoDTO = formaLiquidacaoMapper.toDto(formaLiquidacao);

        restFormaLiquidacaoMockMvc.perform(post("/api/forma-liquidacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaLiquidacaoDTO)))
            .andExpect(status().isBadRequest());

        List<FormaLiquidacao> formaLiquidacaoList = formaLiquidacaoRepository.findAll();
        assertThat(formaLiquidacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFormaLiquidacaos() throws Exception {
        // Initialize the database
        formaLiquidacaoRepository.saveAndFlush(formaLiquidacao);

        // Get all the formaLiquidacaoList
        restFormaLiquidacaoMockMvc.perform(get("/api/forma-liquidacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formaLiquidacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].juro").value(hasItem(DEFAULT_JURO.doubleValue())))
            .andExpect(jsonPath("$.[*].prazoLiquidacao").value(hasItem(DEFAULT_PRAZO_LIQUIDACAO)))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)));
    }
    
    @Test
    @Transactional
    public void getFormaLiquidacao() throws Exception {
        // Initialize the database
        formaLiquidacaoRepository.saveAndFlush(formaLiquidacao);

        // Get the formaLiquidacao
        restFormaLiquidacaoMockMvc.perform(get("/api/forma-liquidacaos/{id}", formaLiquidacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formaLiquidacao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.juro").value(DEFAULT_JURO.doubleValue()))
            .andExpect(jsonPath("$.prazoLiquidacao").value(DEFAULT_PRAZO_LIQUIDACAO))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON));
    }

    @Test
    @Transactional
    public void getNonExistingFormaLiquidacao() throws Exception {
        // Get the formaLiquidacao
        restFormaLiquidacaoMockMvc.perform(get("/api/forma-liquidacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormaLiquidacao() throws Exception {
        // Initialize the database
        formaLiquidacaoRepository.saveAndFlush(formaLiquidacao);

        int databaseSizeBeforeUpdate = formaLiquidacaoRepository.findAll().size();

        // Update the formaLiquidacao
        FormaLiquidacao updatedFormaLiquidacao = formaLiquidacaoRepository.findById(formaLiquidacao.getId()).get();
        // Disconnect from session so that the updates on updatedFormaLiquidacao are not directly saved in db
        em.detach(updatedFormaLiquidacao);
        updatedFormaLiquidacao
            .nome(UPDATED_NOME)
            .juro(UPDATED_JURO)
            .prazoLiquidacao(UPDATED_PRAZO_LIQUIDACAO)
            .quantidade(UPDATED_QUANTIDADE)
            .icon(UPDATED_ICON);
        FormaLiquidacaoDTO formaLiquidacaoDTO = formaLiquidacaoMapper.toDto(updatedFormaLiquidacao);

        restFormaLiquidacaoMockMvc.perform(put("/api/forma-liquidacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaLiquidacaoDTO)))
            .andExpect(status().isOk());

        // Validate the FormaLiquidacao in the database
        List<FormaLiquidacao> formaLiquidacaoList = formaLiquidacaoRepository.findAll();
        assertThat(formaLiquidacaoList).hasSize(databaseSizeBeforeUpdate);
        FormaLiquidacao testFormaLiquidacao = formaLiquidacaoList.get(formaLiquidacaoList.size() - 1);
        assertThat(testFormaLiquidacao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFormaLiquidacao.getJuro()).isEqualTo(UPDATED_JURO);
        assertThat(testFormaLiquidacao.getPrazoLiquidacao()).isEqualTo(UPDATED_PRAZO_LIQUIDACAO);
        assertThat(testFormaLiquidacao.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testFormaLiquidacao.getIcon()).isEqualTo(UPDATED_ICON);

        // Validate the FormaLiquidacao in Elasticsearch
        verify(mockFormaLiquidacaoSearchRepository, times(1)).save(testFormaLiquidacao);
    }

    @Test
    @Transactional
    public void updateNonExistingFormaLiquidacao() throws Exception {
        int databaseSizeBeforeUpdate = formaLiquidacaoRepository.findAll().size();

        // Create the FormaLiquidacao
        FormaLiquidacaoDTO formaLiquidacaoDTO = formaLiquidacaoMapper.toDto(formaLiquidacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormaLiquidacaoMockMvc.perform(put("/api/forma-liquidacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaLiquidacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FormaLiquidacao in the database
        List<FormaLiquidacao> formaLiquidacaoList = formaLiquidacaoRepository.findAll();
        assertThat(formaLiquidacaoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FormaLiquidacao in Elasticsearch
        verify(mockFormaLiquidacaoSearchRepository, times(0)).save(formaLiquidacao);
    }

    @Test
    @Transactional
    public void deleteFormaLiquidacao() throws Exception {
        // Initialize the database
        formaLiquidacaoRepository.saveAndFlush(formaLiquidacao);

        int databaseSizeBeforeDelete = formaLiquidacaoRepository.findAll().size();

        // Delete the formaLiquidacao
        restFormaLiquidacaoMockMvc.perform(delete("/api/forma-liquidacaos/{id}", formaLiquidacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormaLiquidacao> formaLiquidacaoList = formaLiquidacaoRepository.findAll();
        assertThat(formaLiquidacaoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FormaLiquidacao in Elasticsearch
        verify(mockFormaLiquidacaoSearchRepository, times(1)).deleteById(formaLiquidacao.getId());
    }

    @Test
    @Transactional
    public void searchFormaLiquidacao() throws Exception {
        // Initialize the database
        formaLiquidacaoRepository.saveAndFlush(formaLiquidacao);
        when(mockFormaLiquidacaoSearchRepository.search(queryStringQuery("id:" + formaLiquidacao.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(formaLiquidacao), PageRequest.of(0, 1), 1));
        // Search the formaLiquidacao
        restFormaLiquidacaoMockMvc.perform(get("/api/_search/forma-liquidacaos?query=id:" + formaLiquidacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formaLiquidacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].juro").value(hasItem(DEFAULT_JURO.doubleValue())))
            .andExpect(jsonPath("$.[*].prazoLiquidacao").value(hasItem(DEFAULT_PRAZO_LIQUIDACAO)))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)));
    }
}
