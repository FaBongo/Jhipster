package www.diti5.springboot1.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static www.diti5.springboot1.domain.CategorieTestSamples.*;
import static www.diti5.springboot1.domain.CategorieTestSamples.*;

import org.junit.jupiter.api.Test;
import www.diti5.springboot1.web.rest.TestUtil;

class CategorieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categorie.class);
        Categorie categorie1 = getCategorieSample1();
        Categorie categorie2 = new Categorie();
        assertThat(categorie1).isNotEqualTo(categorie2);

        categorie2.setId(categorie1.getId());
        assertThat(categorie1).isEqualTo(categorie2);

        categorie2 = getCategorieSample2();
        assertThat(categorie1).isNotEqualTo(categorie2);
    }

    @Test
    void parentTest() throws Exception {
        Categorie categorie = getCategorieRandomSampleGenerator();
        Categorie categorieBack = getCategorieRandomSampleGenerator();

        categorie.setParent(categorieBack);
        assertThat(categorie.getParent()).isEqualTo(categorieBack);

        categorie.parent(null);
        assertThat(categorie.getParent()).isNull();
    }
}
