package www.diti5.springboot1.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static www.diti5.springboot1.domain.CategorieTestSamples.*;
import static www.diti5.springboot1.domain.ProduitTestSamples.*;

import org.junit.jupiter.api.Test;
import www.diti5.springboot1.web.rest.TestUtil;

class ProduitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Produit.class);
        Produit produit1 = getProduitSample1();
        Produit produit2 = new Produit();
        assertThat(produit1).isNotEqualTo(produit2);

        produit2.setId(produit1.getId());
        assertThat(produit1).isEqualTo(produit2);

        produit2 = getProduitSample2();
        assertThat(produit1).isNotEqualTo(produit2);
    }

    @Test
    void categorieTest() throws Exception {
        Produit produit = getProduitRandomSampleGenerator();
        Categorie categorieBack = getCategorieRandomSampleGenerator();

        produit.setCategorie(categorieBack);
        assertThat(produit.getCategorie()).isEqualTo(categorieBack);

        produit.categorie(null);
        assertThat(produit.getCategorie()).isNull();
    }
}
