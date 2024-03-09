package www.diti5.springboot1.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import www.diti5.springboot1.domain.Categorie;
import www.diti5.springboot1.repository.rowmapper.CategorieRowMapper;
import www.diti5.springboot1.repository.rowmapper.CategorieRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the Categorie entity.
 */
@SuppressWarnings("unused")
class CategorieRepositoryInternalImpl extends SimpleR2dbcRepository<Categorie, Long> implements CategorieRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CategorieRowMapper categorieMapper;

    private static final Table entityTable = Table.aliased("categorie", EntityManager.ENTITY_ALIAS);
    private static final Table parentTable = Table.aliased("categorie", "parent");

    public CategorieRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CategorieRowMapper categorieMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Categorie.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.categorieMapper = categorieMapper;
    }

    @Override
    public Flux<Categorie> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Categorie> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CategorieSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CategorieSqlHelper.getColumns(parentTable, "parent"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(parentTable)
            .on(Column.create("parent_id", entityTable))
            .equals(Column.create("id", parentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Categorie.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Categorie> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Categorie> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Categorie process(Row row, RowMetadata metadata) {
        Categorie entity = categorieMapper.apply(row, "e");
        entity.setParent(categorieMapper.apply(row, "parent"));
        return entity;
    }

    @Override
    public <S extends Categorie> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
