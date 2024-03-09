package www.diti5.springboot1.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import www.diti5.springboot1.domain.Categorie;

/**
 * Converter between {@link Row} to {@link Categorie}, with proper type conversions.
 */
@Service
public class CategorieRowMapper implements BiFunction<Row, String, Categorie> {

    private final ColumnConverter converter;

    public CategorieRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Categorie} stored in the database.
     */
    @Override
    public Categorie apply(Row row, String prefix) {
        Categorie entity = new Categorie();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setDepth(converter.fromRow(row, prefix + "_depth", Integer.class));
        entity.setNbreProduit(converter.fromRow(row, prefix + "_nbre_produit", Long.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        return entity;
    }
}
