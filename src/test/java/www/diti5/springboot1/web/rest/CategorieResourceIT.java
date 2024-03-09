package www.diti5.springboot1.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import www.diti5.springboot1.IntegrationTest;
import www.diti5.springboot1.domain.Categorie;
import www.diti5.springboot1.repository.CategorieRepository;
import www.diti5.springboot1.repository.EntityManager;

/**
 * Integration tests for the {@link CategorieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CategorieResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DEPTH = 1;
    private static final Integer UPDATED_DEPTH = 2;

    private static final Long DEFAULT_NBRE_PRODUIT = 1L;
    private static final Long UPDATED_NBRE_PRODUIT = 2L;

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Categorie categorie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createEntity(EntityManager em) {
        Categorie categorie = new Categorie()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .depth(DEFAULT_DEPTH)
            .nbreProduit(DEFAULT_NBRE_PRODUIT);
        return categorie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createUpdatedEntity(EntityManager em) {
        Categorie categorie = new Categorie()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .depth(UPDATED_DEPTH)
            .nbreProduit(UPDATED_NBRE_PRODUIT);
        return categorie;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Categorie.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        categorie = createEntity(em);
    }

    @Test
    void createCategorie() throws Exception {
        int databaseSizeBeforeCreate = categorieRepository.findAll().collectList().block().size();
        // Create the Categorie
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeCreate + 1);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCategorie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCategorie.getDepth()).isEqualTo(DEFAULT_DEPTH);
        assertThat(testCategorie.getNbreProduit()).isEqualTo(DEFAULT_NBRE_PRODUIT);
    }

    @Test
    void createCategorieWithExistingId() throws Exception {
        // Create the Categorie with an existing ID
        categorie.setId(1L);

        int databaseSizeBeforeCreate = categorieRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieRepository.findAll().collectList().block().size();
        // set the field null
        categorie.setNom(null);

        // Create the Categorie, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCategoriesAsStream() {
        // Initialize the database
        categorieRepository.save(categorie).block();

        List<Categorie> categorieList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Categorie.class)
            .getResponseBody()
            .filter(categorie::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(categorieList).isNotNull();
        assertThat(categorieList).hasSize(1);
        Categorie testCategorie = categorieList.get(0);
        assertThat(testCategorie.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCategorie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCategorie.getDepth()).isEqualTo(DEFAULT_DEPTH);
        assertThat(testCategorie.getNbreProduit()).isEqualTo(DEFAULT_NBRE_PRODUIT);
    }

    @Test
    void getAllCategories() {
        // Initialize the database
        categorieRepository.save(categorie).block();

        // Get all the categorieList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(categorie.getId().intValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].depth")
            .value(hasItem(DEFAULT_DEPTH))
            .jsonPath("$.[*].nbreProduit")
            .value(hasItem(DEFAULT_NBRE_PRODUIT.intValue()));
    }

    @Test
    void getCategorie() {
        // Initialize the database
        categorieRepository.save(categorie).block();

        // Get the categorie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, categorie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(categorie.getId().intValue()))
            .jsonPath("$.nom")
            .value(is(DEFAULT_NOM))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.depth")
            .value(is(DEFAULT_DEPTH))
            .jsonPath("$.nbreProduit")
            .value(is(DEFAULT_NBRE_PRODUIT.intValue()));
    }

    @Test
    void getNonExistingCategorie() {
        // Get the categorie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCategorie() throws Exception {
        // Initialize the database
        categorieRepository.save(categorie).block();

        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();

        // Update the categorie
        Categorie updatedCategorie = categorieRepository.findById(categorie.getId()).block();
        updatedCategorie.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).depth(UPDATED_DEPTH).nbreProduit(UPDATED_NBRE_PRODUIT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCategorie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCategorie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCategorie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCategorie.getDepth()).isEqualTo(UPDATED_DEPTH);
        assertThat(testCategorie.getNbreProduit()).isEqualTo(UPDATED_NBRE_PRODUIT);
    }

    @Test
    void putNonExistingCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, categorie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCategorieWithPatch() throws Exception {
        // Initialize the database
        categorieRepository.save(categorie).block();

        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();

        // Update the categorie using partial update
        Categorie partialUpdatedCategorie = new Categorie();
        partialUpdatedCategorie.setId(categorie.getId());

        partialUpdatedCategorie.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).depth(UPDATED_DEPTH);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategorie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCategorie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCategorie.getDepth()).isEqualTo(UPDATED_DEPTH);
        assertThat(testCategorie.getNbreProduit()).isEqualTo(DEFAULT_NBRE_PRODUIT);
    }

    @Test
    void fullUpdateCategorieWithPatch() throws Exception {
        // Initialize the database
        categorieRepository.save(categorie).block();

        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();

        // Update the categorie using partial update
        Categorie partialUpdatedCategorie = new Categorie();
        partialUpdatedCategorie.setId(categorie.getId());

        partialUpdatedCategorie.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).depth(UPDATED_DEPTH).nbreProduit(UPDATED_NBRE_PRODUIT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategorie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCategorie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCategorie.getDepth()).isEqualTo(UPDATED_DEPTH);
        assertThat(testCategorie.getNbreProduit()).isEqualTo(UPDATED_NBRE_PRODUIT);
    }

    @Test
    void patchNonExistingCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, categorie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().collectList().block().size();
        categorie.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categorie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCategorie() {
        // Initialize the database
        categorieRepository.save(categorie).block();

        int databaseSizeBeforeDelete = categorieRepository.findAll().collectList().block().size();

        // Delete the categorie
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, categorie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Categorie> categorieList = categorieRepository.findAll().collectList().block();
        assertThat(categorieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
