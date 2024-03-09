package www.diti5.springboot1.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import www.diti5.springboot1.domain.Produit;

/**
 * Converter between {@link Row} to {@link Produit}, with proper type conversions.
 */
@Service
public class ProduitRowMapper implements BiFunction<Row, String, Produit> {

    private final ColumnConverter converter;

    public ProduitRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Produit} stored in the database.
     */
    @Override
    public Produit apply(Row row, String prefix) {
        Produit entity = new Produit();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        entity.setPrix(converter.fromRow(row, prefix + "_prix", Double.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCategorieId(converter.fromRow(row, prefix + "_categorie_id", Long.class));
        return entity;
    }
}
