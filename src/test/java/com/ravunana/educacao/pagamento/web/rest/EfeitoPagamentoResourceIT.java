package com.ravunana.educacao.pagamento.web.rest;

import com.ravunana.educacao.pagamento.PagamentoApp;
import com.ravunana.educacao.pagamento.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pagamento.domain.EfeitoPagamento;
import com.ravunana.educacao.pagamento.domain.Emolumento;
import com.ravunana.educacao.pagamento.domain.PagamentoEmolumento;
import com.ravunana.educacao.pagamento.repository.EfeitoPagamentoRepository;
import com.ravunana.educacao.pagamento.repository.search.EfeitoPagamentoSearchRepository;
import com.ravunana.educacao.pagamento.service.EfeitoPagamentoService;
import com.ravunana.educacao.pagamento.service.dto.EfeitoPagamentoDTO;
import com.ravunana.educacao.pagamento.service.mapper.EfeitoPagamentoMapper;
import com.ravunana.educacao.pagamento.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
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
 * Integration tests for the {@link EfeitoPagamentoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PagamentoApp.class})
public class EfeitoPagamentoResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTIDADE = 1;
    private static final Integer UPDATED_QUANTIDADE = 2;

    private static final BigDecimal DEFAULT_MONTANTE = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTANTE = new BigDecimal(1);

    private static final Double DEFAULT_DESCONTO = 0D;
    private static final Double UPDATED_DESCONTO = 1D;

    private static final Double DEFAULT_MULTA = 0D;
    private static final Double UPDATED_MULTA = 1D;

    private static final Double DEFAULT_JURO = 0D;
    private static final Double UPDATED_JURO = 1D;

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final LocalDate DEFAULT_VENCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VENCIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_QUITADO = false;
    private static final Boolean UPDATED_QUITADO = true;

    @Autowired
    private EfeitoPagamentoRepository efeitoPagamentoRepository;

    @Mock
    private EfeitoPagamentoRepository efeitoPagamentoRepositoryMock;

    @Autowired
    private EfeitoPagamentoMapper efeitoPagamentoMapper;

    @Mock
    private EfeitoPagamentoService efeitoPagamentoServiceMock;

    @Autowired
    private EfeitoPagamentoService efeitoPagamentoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pagamento.repository.search test package.
     *
     * @see com.ravunana.educacao.pagamento.repository.search.EfeitoPagamentoSearchRepositoryMockConfiguration
     */
    @Autowired
    private EfeitoPagamentoSearchRepository mockEfeitoPagamentoSearchRepository;

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

    private MockMvc restEfeitoPagamentoMockMvc;

    private EfeitoPagamento efeitoPagamento;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EfeitoPagamentoResource efeitoPagamentoResource = new EfeitoPagamentoResource(efeitoPagamentoService);
        this.restEfeitoPagamentoMockMvc = MockMvcBuilders.standaloneSetup(efeitoPagamentoResource)
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
    public static EfeitoPagamento createEntity(EntityManager em) {
        EfeitoPagamento efeitoPagamento = new EfeitoPagamento()
            .descricao(DEFAULT_DESCRICAO)
            .quantidade(DEFAULT_QUANTIDADE)
            .montante(DEFAULT_MONTANTE)
            .desconto(DEFAULT_DESCONTO)
            .multa(DEFAULT_MULTA)
            .juro(DEFAULT_JURO)
            .data(DEFAULT_DATA)
            .vencimento(DEFAULT_VENCIMENTO)
            .quitado(DEFAULT_QUITADO);
        // Add required entity
        Emolumento emolumento;
        if (TestUtil.findAll(em, Emolumento.class).isEmpty()) {
            emolumento = EmolumentoResourceIT.createEntity(em);
            em.persist(emolumento);
            em.flush();
        } else {
            emolumento = TestUtil.findAll(em, Emolumento.class).get(0);
        }
        efeitoPagamento.setEmolumento(emolumento);
        // Add required entity
        PagamentoEmolumento pagamentoEmolumento;
        if (TestUtil.findAll(em, PagamentoEmolumento.class).isEmpty()) {
            pagamentoEmolumento = PagamentoEmolumentoResourceIT.createEntity(em);
            em.persist(pagamentoEmolumento);
            em.flush();
        } else {
            pagamentoEmolumento = TestUtil.findAll(em, PagamentoEmolumento.class).get(0);
        }
        efeitoPagamento.setPagamento(pagamentoEmolumento);
        return efeitoPagamento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EfeitoPagamento createUpdatedEntity(EntityManager em) {
        EfeitoPagamento efeitoPagamento = new EfeitoPagamento()
            .descricao(UPDATED_DESCRICAO)
            .quantidade(UPDATED_QUANTIDADE)
            .montante(UPDATED_MONTANTE)
            .desconto(UPDATED_DESCONTO)
            .multa(UPDATED_MULTA)
            .juro(UPDATED_JURO)
            .data(UPDATED_DATA)
            .vencimento(UPDATED_VENCIMENTO)
            .quitado(UPDATED_QUITADO);
        // Add required entity
        Emolumento emolumento;
        if (TestUtil.findAll(em, Emolumento.class).isEmpty()) {
            emolumento = EmolumentoResourceIT.createUpdatedEntity(em);
            em.persist(emolumento);
            em.flush();
        } else {
            emolumento = TestUtil.findAll(em, Emolumento.class).get(0);
        }
        efeitoPagamento.setEmolumento(emolumento);
        // Add required entity
        PagamentoEmolumento pagamentoEmolumento;
        if (TestUtil.findAll(em, PagamentoEmolumento.class).isEmpty()) {
            pagamentoEmolumento = PagamentoEmolumentoResourceIT.createUpdatedEntity(em);
            em.persist(pagamentoEmolumento);
            em.flush();
        } else {
            pagamentoEmolumento = TestUtil.findAll(em, PagamentoEmolumento.class).get(0);
        }
        efeitoPagamento.setPagamento(pagamentoEmolumento);
        return efeitoPagamento;
    }

    @BeforeEach
    public void initTest() {
        efeitoPagamento = createEntity(em);
    }

    @Test
    @Transactional
    public void createEfeitoPagamento() throws Exception {
        int databaseSizeBeforeCreate = efeitoPagamentoRepository.findAll().size();

        // Create the EfeitoPagamento
        EfeitoPagamentoDTO efeitoPagamentoDTO = efeitoPagamentoMapper.toDto(efeitoPagamento);
        restEfeitoPagamentoMockMvc.perform(post("/api/efeito-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(efeitoPagamentoDTO)))
            .andExpect(status().isCreated());

        // Validate the EfeitoPagamento in the database
        List<EfeitoPagamento> efeitoPagamentoList = efeitoPagamentoRepository.findAll();
        assertThat(efeitoPagamentoList).hasSize(databaseSizeBeforeCreate + 1);
        EfeitoPagamento testEfeitoPagamento = efeitoPagamentoList.get(efeitoPagamentoList.size() - 1);
        assertThat(testEfeitoPagamento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testEfeitoPagamento.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
        assertThat(testEfeitoPagamento.getMontante()).isEqualTo(DEFAULT_MONTANTE);
        assertThat(testEfeitoPagamento.getDesconto()).isEqualTo(DEFAULT_DESCONTO);
        assertThat(testEfeitoPagamento.getMulta()).isEqualTo(DEFAULT_MULTA);
        assertThat(testEfeitoPagamento.getJuro()).isEqualTo(DEFAULT_JURO);
        assertThat(testEfeitoPagamento.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testEfeitoPagamento.getVencimento()).isEqualTo(DEFAULT_VENCIMENTO);
        assertThat(testEfeitoPagamento.isQuitado()).isEqualTo(DEFAULT_QUITADO);

        // Validate the EfeitoPagamento in Elasticsearch
        verify(mockEfeitoPagamentoSearchRepository, times(1)).save(testEfeitoPagamento);
    }

    @Test
    @Transactional
    public void createEfeitoPagamentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = efeitoPagamentoRepository.findAll().size();

        // Create the EfeitoPagamento with an existing ID
        efeitoPagamento.setId(1L);
        EfeitoPagamentoDTO efeitoPagamentoDTO = efeitoPagamentoMapper.toDto(efeitoPagamento);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEfeitoPagamentoMockMvc.perform(post("/api/efeito-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(efeitoPagamentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EfeitoPagamento in the database
        List<EfeitoPagamento> efeitoPagamentoList = efeitoPagamentoRepository.findAll();
        assertThat(efeitoPagamentoList).hasSize(databaseSizeBeforeCreate);

        // Validate the EfeitoPagamento in Elasticsearch
        verify(mockEfeitoPagamentoSearchRepository, times(0)).save(efeitoPagamento);
    }


    @Test
    @Transactional
    public void checkQuantidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = efeitoPagamentoRepository.findAll().size();
        // set the field null
        efeitoPagamento.setQuantidade(null);

        // Create the EfeitoPagamento, which fails.
        EfeitoPagamentoDTO efeitoPagamentoDTO = efeitoPagamentoMapper.toDto(efeitoPagamento);

        restEfeitoPagamentoMockMvc.perform(post("/api/efeito-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(efeitoPagamentoDTO)))
            .andExpect(status().isBadRequest());

        List<EfeitoPagamento> efeitoPagamentoList = efeitoPagamentoRepository.findAll();
        assertThat(efeitoPagamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = efeitoPagamentoRepository.findAll().size();
        // set the field null
        efeitoPagamento.setMontante(null);

        // Create the EfeitoPagamento, which fails.
        EfeitoPagamentoDTO efeitoPagamentoDTO = efeitoPagamentoMapper.toDto(efeitoPagamento);

        restEfeitoPagamentoMockMvc.perform(post("/api/efeito-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(efeitoPagamentoDTO)))
            .andExpect(status().isBadRequest());

        List<EfeitoPagamento> efeitoPagamentoList = efeitoPagamentoRepository.findAll();
        assertThat(efeitoPagamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuitadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = efeitoPagamentoRepository.findAll().size();
        // set the field null
        efeitoPagamento.setQuitado(null);

        // Create the EfeitoPagamento, which fails.
        EfeitoPagamentoDTO efeitoPagamentoDTO = efeitoPagamentoMapper.toDto(efeitoPagamento);

        restEfeitoPagamentoMockMvc.perform(post("/api/efeito-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(efeitoPagamentoDTO)))
            .andExpect(status().isBadRequest());

        List<EfeitoPagamento> efeitoPagamentoList = efeitoPagamentoRepository.findAll();
        assertThat(efeitoPagamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEfeitoPagamentos() throws Exception {
        // Initialize the database
        efeitoPagamentoRepository.saveAndFlush(efeitoPagamento);

        // Get all the efeitoPagamentoList
        restEfeitoPagamentoMockMvc.perform(get("/api/efeito-pagamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(efeitoPagamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)))
            .andExpect(jsonPath("$.[*].montante").value(hasItem(DEFAULT_MONTANTE.intValue())))
            .andExpect(jsonPath("$.[*].desconto").value(hasItem(DEFAULT_DESCONTO.doubleValue())))
            .andExpect(jsonPath("$.[*].multa").value(hasItem(DEFAULT_MULTA.doubleValue())))
            .andExpect(jsonPath("$.[*].juro").value(hasItem(DEFAULT_JURO.doubleValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].vencimento").value(hasItem(DEFAULT_VENCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].quitado").value(hasItem(DEFAULT_QUITADO.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllEfeitoPagamentosWithEagerRelationshipsIsEnabled() throws Exception {
        EfeitoPagamentoResource efeitoPagamentoResource = new EfeitoPagamentoResource(efeitoPagamentoServiceMock);
        when(efeitoPagamentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restEfeitoPagamentoMockMvc = MockMvcBuilders.standaloneSetup(efeitoPagamentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEfeitoPagamentoMockMvc.perform(get("/api/efeito-pagamentos?eagerload=true"))
        .andExpect(status().isOk());

        verify(efeitoPagamentoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllEfeitoPagamentosWithEagerRelationshipsIsNotEnabled() throws Exception {
        EfeitoPagamentoResource efeitoPagamentoResource = new EfeitoPagamentoResource(efeitoPagamentoServiceMock);
            when(efeitoPagamentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restEfeitoPagamentoMockMvc = MockMvcBuilders.standaloneSetup(efeitoPagamentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEfeitoPagamentoMockMvc.perform(get("/api/efeito-pagamentos?eagerload=true"))
        .andExpect(status().isOk());

            verify(efeitoPagamentoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEfeitoPagamento() throws Exception {
        // Initialize the database
        efeitoPagamentoRepository.saveAndFlush(efeitoPagamento);

        // Get the efeitoPagamento
        restEfeitoPagamentoMockMvc.perform(get("/api/efeito-pagamentos/{id}", efeitoPagamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(efeitoPagamento.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE))
            .andExpect(jsonPath("$.montante").value(DEFAULT_MONTANTE.intValue()))
            .andExpect(jsonPath("$.desconto").value(DEFAULT_DESCONTO.doubleValue()))
            .andExpect(jsonPath("$.multa").value(DEFAULT_MULTA.doubleValue()))
            .andExpect(jsonPath("$.juro").value(DEFAULT_JURO.doubleValue()))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.vencimento").value(DEFAULT_VENCIMENTO.toString()))
            .andExpect(jsonPath("$.quitado").value(DEFAULT_QUITADO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEfeitoPagamento() throws Exception {
        // Get the efeitoPagamento
        restEfeitoPagamentoMockMvc.perform(get("/api/efeito-pagamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEfeitoPagamento() throws Exception {
        // Initialize the database
        efeitoPagamentoRepository.saveAndFlush(efeitoPagamento);

        int databaseSizeBeforeUpdate = efeitoPagamentoRepository.findAll().size();

        // Update the efeitoPagamento
        EfeitoPagamento updatedEfeitoPagamento = efeitoPagamentoRepository.findById(efeitoPagamento.getId()).get();
        // Disconnect from session so that the updates on updatedEfeitoPagamento are not directly saved in db
        em.detach(updatedEfeitoPagamento);
        updatedEfeitoPagamento
            .descricao(UPDATED_DESCRICAO)
            .quantidade(UPDATED_QUANTIDADE)
            .montante(UPDATED_MONTANTE)
            .desconto(UPDATED_DESCONTO)
            .multa(UPDATED_MULTA)
            .juro(UPDATED_JURO)
            .data(UPDATED_DATA)
            .vencimento(UPDATED_VENCIMENTO)
            .quitado(UPDATED_QUITADO);
        EfeitoPagamentoDTO efeitoPagamentoDTO = efeitoPagamentoMapper.toDto(updatedEfeitoPagamento);

        restEfeitoPagamentoMockMvc.perform(put("/api/efeito-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(efeitoPagamentoDTO)))
            .andExpect(status().isOk());

        // Validate the EfeitoPagamento in the database
        List<EfeitoPagamento> efeitoPagamentoList = efeitoPagamentoRepository.findAll();
        assertThat(efeitoPagamentoList).hasSize(databaseSizeBeforeUpdate);
        EfeitoPagamento testEfeitoPagamento = efeitoPagamentoList.get(efeitoPagamentoList.size() - 1);
        assertThat(testEfeitoPagamento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testEfeitoPagamento.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testEfeitoPagamento.getMontante()).isEqualTo(UPDATED_MONTANTE);
        assertThat(testEfeitoPagamento.getDesconto()).isEqualTo(UPDATED_DESCONTO);
        assertThat(testEfeitoPagamento.getMulta()).isEqualTo(UPDATED_MULTA);
        assertThat(testEfeitoPagamento.getJuro()).isEqualTo(UPDATED_JURO);
        assertThat(testEfeitoPagamento.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testEfeitoPagamento.getVencimento()).isEqualTo(UPDATED_VENCIMENTO);
        assertThat(testEfeitoPagamento.isQuitado()).isEqualTo(UPDATED_QUITADO);

        // Validate the EfeitoPagamento in Elasticsearch
        verify(mockEfeitoPagamentoSearchRepository, times(1)).save(testEfeitoPagamento);
    }

    @Test
    @Transactional
    public void updateNonExistingEfeitoPagamento() throws Exception {
        int databaseSizeBeforeUpdate = efeitoPagamentoRepository.findAll().size();

        // Create the EfeitoPagamento
        EfeitoPagamentoDTO efeitoPagamentoDTO = efeitoPagamentoMapper.toDto(efeitoPagamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEfeitoPagamentoMockMvc.perform(put("/api/efeito-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(efeitoPagamentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EfeitoPagamento in the database
        List<EfeitoPagamento> efeitoPagamentoList = efeitoPagamentoRepository.findAll();
        assertThat(efeitoPagamentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EfeitoPagamento in Elasticsearch
        verify(mockEfeitoPagamentoSearchRepository, times(0)).save(efeitoPagamento);
    }

    @Test
    @Transactional
    public void deleteEfeitoPagamento() throws Exception {
        // Initialize the database
        efeitoPagamentoRepository.saveAndFlush(efeitoPagamento);

        int databaseSizeBeforeDelete = efeitoPagamentoRepository.findAll().size();

        // Delete the efeitoPagamento
        restEfeitoPagamentoMockMvc.perform(delete("/api/efeito-pagamentos/{id}", efeitoPagamento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EfeitoPagamento> efeitoPagamentoList = efeitoPagamentoRepository.findAll();
        assertThat(efeitoPagamentoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the EfeitoPagamento in Elasticsearch
        verify(mockEfeitoPagamentoSearchRepository, times(1)).deleteById(efeitoPagamento.getId());
    }

    @Test
    @Transactional
    public void searchEfeitoPagamento() throws Exception {
        // Initialize the database
        efeitoPagamentoRepository.saveAndFlush(efeitoPagamento);
        when(mockEfeitoPagamentoSearchRepository.search(queryStringQuery("id:" + efeitoPagamento.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(efeitoPagamento), PageRequest.of(0, 1), 1));
        // Search the efeitoPagamento
        restEfeitoPagamentoMockMvc.perform(get("/api/_search/efeito-pagamentos?query=id:" + efeitoPagamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(efeitoPagamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)))
            .andExpect(jsonPath("$.[*].montante").value(hasItem(DEFAULT_MONTANTE.intValue())))
            .andExpect(jsonPath("$.[*].desconto").value(hasItem(DEFAULT_DESCONTO.doubleValue())))
            .andExpect(jsonPath("$.[*].multa").value(hasItem(DEFAULT_MULTA.doubleValue())))
            .andExpect(jsonPath("$.[*].juro").value(hasItem(DEFAULT_JURO.doubleValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].vencimento").value(hasItem(DEFAULT_VENCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].quitado").value(hasItem(DEFAULT_QUITADO.booleanValue())));
    }
}
