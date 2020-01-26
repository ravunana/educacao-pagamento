package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.PagamentoApp;
import com.ravunana.educacao.pagamento.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pagamento.domain.Deposito;
import com.ravunana.educacao.pagamento.domain.Caixa;
import com.ravunana.educacao.pagamento.repository.DepositoRepository;
import com.ravunana.educacao.pagamento.repository.search.DepositoSearchRepository;
import com.ravunana.educacao.pagamento.service.DepositoService;
import com.ravunana.educacao.pagamento.service.dto.DepositoDTO;
import com.ravunana.educacao.pagamento.service.mapper.DepositoMapper;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
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
 * Integration tests for the {@link DepositoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PagamentoApp.class})
public class DepositoResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_DEPOSITO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_DEPOSITO = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_MONTANTE = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTANTE = new BigDecimal(1);

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_ANEXO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANEXO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ANEXO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANEXO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NUMERO_PROCESSO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_PROCESSO = "BBBBBBBBBB";

    private static final String DEFAULT_MEIO_LIQUIDACAO = "AAAAAAAAAA";
    private static final String UPDATED_MEIO_LIQUIDACAO = "BBBBBBBBBB";

    @Autowired
    private DepositoRepository depositoRepository;

    @Autowired
    private DepositoMapper depositoMapper;

    @Autowired
    private DepositoService depositoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pagamento.repository.search test package.
     *
     * @see com.ravunana.educacao.pagamento.repository.search.DepositoSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepositoSearchRepository mockDepositoSearchRepository;

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

    private MockMvc restDepositoMockMvc;

    private Deposito deposito;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepositoResource depositoResource = new DepositoResource(depositoService);
        this.restDepositoMockMvc = MockMvcBuilders.standaloneSetup(depositoResource)
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
    public static Deposito createEntity(EntityManager em) {
        Deposito deposito = new Deposito()
            .numero(DEFAULT_NUMERO)
            .dataDeposito(DEFAULT_DATA_DEPOSITO)
            .montante(DEFAULT_MONTANTE)
            .data(DEFAULT_DATA)
            .anexo(DEFAULT_ANEXO)
            .anexoContentType(DEFAULT_ANEXO_CONTENT_TYPE)
            .numeroProcesso(DEFAULT_NUMERO_PROCESSO)
            .meioLiquidacao(DEFAULT_MEIO_LIQUIDACAO);
        // Add required entity
        Caixa caixa;
        if (TestUtil.findAll(em, Caixa.class).isEmpty()) {
            caixa = CaixaResourceIT.createEntity(em);
            em.persist(caixa);
            em.flush();
        } else {
            caixa = TestUtil.findAll(em, Caixa.class).get(0);
        }
        deposito.setBancoCaixa(caixa);
        return deposito;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposito createUpdatedEntity(EntityManager em) {
        Deposito deposito = new Deposito()
            .numero(UPDATED_NUMERO)
            .dataDeposito(UPDATED_DATA_DEPOSITO)
            .montante(UPDATED_MONTANTE)
            .data(UPDATED_DATA)
            .anexo(UPDATED_ANEXO)
            .anexoContentType(UPDATED_ANEXO_CONTENT_TYPE)
            .numeroProcesso(UPDATED_NUMERO_PROCESSO)
            .meioLiquidacao(UPDATED_MEIO_LIQUIDACAO);
        // Add required entity
        Caixa caixa;
        if (TestUtil.findAll(em, Caixa.class).isEmpty()) {
            caixa = CaixaResourceIT.createUpdatedEntity(em);
            em.persist(caixa);
            em.flush();
        } else {
            caixa = TestUtil.findAll(em, Caixa.class).get(0);
        }
        deposito.setBancoCaixa(caixa);
        return deposito;
    }

    @BeforeEach
    public void initTest() {
        deposito = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeposito() throws Exception {
        int databaseSizeBeforeCreate = depositoRepository.findAll().size();

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);
        restDepositoMockMvc.perform(post("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isCreated());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeCreate + 1);
        Deposito testDeposito = depositoList.get(depositoList.size() - 1);
        assertThat(testDeposito.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testDeposito.getDataDeposito()).isEqualTo(DEFAULT_DATA_DEPOSITO);
        assertThat(testDeposito.getMontante()).isEqualTo(DEFAULT_MONTANTE);
        assertThat(testDeposito.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testDeposito.getAnexo()).isEqualTo(DEFAULT_ANEXO);
        assertThat(testDeposito.getAnexoContentType()).isEqualTo(DEFAULT_ANEXO_CONTENT_TYPE);
        assertThat(testDeposito.getNumeroProcesso()).isEqualTo(DEFAULT_NUMERO_PROCESSO);
        assertThat(testDeposito.getMeioLiquidacao()).isEqualTo(DEFAULT_MEIO_LIQUIDACAO);

        // Validate the Deposito in Elasticsearch
        verify(mockDepositoSearchRepository, times(1)).save(testDeposito);
    }

    @Test
    @Transactional
    public void createDepositoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depositoRepository.findAll().size();

        // Create the Deposito with an existing ID
        deposito.setId(1L);
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositoMockMvc.perform(post("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Deposito in Elasticsearch
        verify(mockDepositoSearchRepository, times(0)).save(deposito);
    }


    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositoRepository.findAll().size();
        // set the field null
        deposito.setNumero(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc.perform(post("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataDepositoIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositoRepository.findAll().size();
        // set the field null
        deposito.setDataDeposito(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc.perform(post("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositoRepository.findAll().size();
        // set the field null
        deposito.setMontante(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc.perform(post("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositoRepository.findAll().size();
        // set the field null
        deposito.setData(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc.perform(post("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroProcessoIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositoRepository.findAll().size();
        // set the field null
        deposito.setNumeroProcesso(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc.perform(post("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMeioLiquidacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = depositoRepository.findAll().size();
        // set the field null
        deposito.setMeioLiquidacao(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc.perform(post("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepositos() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList
        restDepositoMockMvc.perform(get("/api/depositos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposito.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dataDeposito").value(hasItem(DEFAULT_DATA_DEPOSITO.toString())))
            .andExpect(jsonPath("$.[*].montante").value(hasItem(DEFAULT_MONTANTE.intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].anexoContentType").value(hasItem(DEFAULT_ANEXO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anexo").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANEXO))))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)))
            .andExpect(jsonPath("$.[*].meioLiquidacao").value(hasItem(DEFAULT_MEIO_LIQUIDACAO)));
    }
    
    @Test
    @Transactional
    public void getDeposito() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        // Get the deposito
        restDepositoMockMvc.perform(get("/api/depositos/{id}", deposito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deposito.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.dataDeposito").value(DEFAULT_DATA_DEPOSITO.toString()))
            .andExpect(jsonPath("$.montante").value(DEFAULT_MONTANTE.intValue()))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.anexoContentType").value(DEFAULT_ANEXO_CONTENT_TYPE))
            .andExpect(jsonPath("$.anexo").value(Base64Utils.encodeToString(DEFAULT_ANEXO)))
            .andExpect(jsonPath("$.numeroProcesso").value(DEFAULT_NUMERO_PROCESSO))
            .andExpect(jsonPath("$.meioLiquidacao").value(DEFAULT_MEIO_LIQUIDACAO));
    }

    @Test
    @Transactional
    public void getNonExistingDeposito() throws Exception {
        // Get the deposito
        restDepositoMockMvc.perform(get("/api/depositos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeposito() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();

        // Update the deposito
        Deposito updatedDeposito = depositoRepository.findById(deposito.getId()).get();
        // Disconnect from session so that the updates on updatedDeposito are not directly saved in db
        em.detach(updatedDeposito);
        updatedDeposito
            .numero(UPDATED_NUMERO)
            .dataDeposito(UPDATED_DATA_DEPOSITO)
            .montante(UPDATED_MONTANTE)
            .data(UPDATED_DATA)
            .anexo(UPDATED_ANEXO)
            .anexoContentType(UPDATED_ANEXO_CONTENT_TYPE)
            .numeroProcesso(UPDATED_NUMERO_PROCESSO)
            .meioLiquidacao(UPDATED_MEIO_LIQUIDACAO);
        DepositoDTO depositoDTO = depositoMapper.toDto(updatedDeposito);

        restDepositoMockMvc.perform(put("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isOk());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);
        Deposito testDeposito = depositoList.get(depositoList.size() - 1);
        assertThat(testDeposito.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testDeposito.getDataDeposito()).isEqualTo(UPDATED_DATA_DEPOSITO);
        assertThat(testDeposito.getMontante()).isEqualTo(UPDATED_MONTANTE);
        assertThat(testDeposito.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testDeposito.getAnexo()).isEqualTo(UPDATED_ANEXO);
        assertThat(testDeposito.getAnexoContentType()).isEqualTo(UPDATED_ANEXO_CONTENT_TYPE);
        assertThat(testDeposito.getNumeroProcesso()).isEqualTo(UPDATED_NUMERO_PROCESSO);
        assertThat(testDeposito.getMeioLiquidacao()).isEqualTo(UPDATED_MEIO_LIQUIDACAO);

        // Validate the Deposito in Elasticsearch
        verify(mockDepositoSearchRepository, times(1)).save(testDeposito);
    }

    @Test
    @Transactional
    public void updateNonExistingDeposito() throws Exception {
        int databaseSizeBeforeUpdate = depositoRepository.findAll().size();

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositoMockMvc.perform(put("/api/depositos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Deposito in Elasticsearch
        verify(mockDepositoSearchRepository, times(0)).save(deposito);
    }

    @Test
    @Transactional
    public void deleteDeposito() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);

        int databaseSizeBeforeDelete = depositoRepository.findAll().size();

        // Delete the deposito
        restDepositoMockMvc.perform(delete("/api/depositos/{id}", deposito.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deposito> depositoList = depositoRepository.findAll();
        assertThat(depositoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Deposito in Elasticsearch
        verify(mockDepositoSearchRepository, times(1)).deleteById(deposito.getId());
    }

    @Test
    @Transactional
    public void searchDeposito() throws Exception {
        // Initialize the database
        depositoRepository.saveAndFlush(deposito);
        when(mockDepositoSearchRepository.search(queryStringQuery("id:" + deposito.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(deposito), PageRequest.of(0, 1), 1));
        // Search the deposito
        restDepositoMockMvc.perform(get("/api/_search/depositos?query=id:" + deposito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposito.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dataDeposito").value(hasItem(DEFAULT_DATA_DEPOSITO.toString())))
            .andExpect(jsonPath("$.[*].montante").value(hasItem(DEFAULT_MONTANTE.intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].anexoContentType").value(hasItem(DEFAULT_ANEXO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anexo").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANEXO))))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)))
            .andExpect(jsonPath("$.[*].meioLiquidacao").value(hasItem(DEFAULT_MEIO_LIQUIDACAO)));
    }
}
