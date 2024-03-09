package www.diti5.springboot1.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import www.diti5.springboot1.domain.Categorie;

/**
 * Spring Data R2DBC repository for the Categorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieRepository extends ReactiveCrudRepository<Categorie, Long>, CategorieRepositoryInternal {
    @Query("SELECT * FROM categorie entity WHERE entity.parent_id = :id")
    Flux<Categorie> findByParent(Long id);

    @Query("SELECT * FROM categorie entity WHERE entity.parent_id IS NULL")
    Flux<Categorie> findAllWhereParentIsNull();

    @Override
    <S extends Categorie> Mono<S> save(S entity);

    @Override
    Flux<Categorie> findAll();

    @Override
    Mono<Categorie> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CategorieRepositoryInternal {
    <S extends Categorie> Mono<S> save(S entity);

    Flux<Categorie> findAllBy(Pageable pageable);

    Flux<Categorie> findAll();

    Mono<Categorie> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Categorie> findAllBy(Pageable pageable, Criteria criteria);
}
