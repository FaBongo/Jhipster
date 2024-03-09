package www.diti5.springboot1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import www.diti5.springboot1.domain.Categorie;
import www.diti5.springboot1.repository.CategorieRepository;

/**
 * Service Implementation for managing {@link www.diti5.springboot1.domain.Categorie}.
 */
@Service
@Transactional
public class CategorieService {

    private final Logger log = LoggerFactory.getLogger(CategorieService.class);

    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    /**
     * Save a categorie.
     *
     * @param categorie the entity to save.
     * @return the persisted entity.
     */
    public Mono<Categorie> save(Categorie categorie) {
        log.debug("Request to save Categorie : {}", categorie);
        return categorieRepository.save(categorie);
    }

    /**
     * Update a categorie.
     *
     * @param categorie the entity to save.
     * @return the persisted entity.
     */
    public Mono<Categorie> update(Categorie categorie) {
        log.debug("Request to update Categorie : {}", categorie);
        return categorieRepository.save(categorie);
    }

    /**
     * Partially update a categorie.
     *
     * @param categorie the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Categorie> partialUpdate(Categorie categorie) {
        log.debug("Request to partially update Categorie : {}", categorie);

        return categorieRepository
            .findById(categorie.getId())
            .map(existingCategorie -> {
                if (categorie.getNom() != null) {
                    existingCategorie.setNom(categorie.getNom());
                }
                if (categorie.getDescription() != null) {
                    existingCategorie.setDescription(categorie.getDescription());
                }
                if (categorie.getDepth() != null) {
                    existingCategorie.setDepth(categorie.getDepth());
                }
                if (categorie.getNbreProduit() != null) {
                    existingCategorie.setNbreProduit(categorie.getNbreProduit());
                }

                return existingCategorie;
            })
            .flatMap(categorieRepository::save);
    }

    /**
     * Get all the categories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Categorie> findAll() {
        log.debug("Request to get all Categories");
        return categorieRepository.findAll();
    }

    /**
     * Returns the number of categories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return categorieRepository.count();
    }

    /**
     * Get one categorie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Categorie> findOne(Long id) {
        log.debug("Request to get Categorie : {}", id);
        return categorieRepository.findById(id);
    }

    /**
     * Delete the categorie by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Categorie : {}", id);
        return categorieRepository.deleteById(id);
    }
}
