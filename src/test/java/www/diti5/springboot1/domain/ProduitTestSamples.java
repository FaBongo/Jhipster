package www.diti5.springboot1.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProduitTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Produit getProduitSample1() {
        return new Produit().id(1L).nom("nom1").description("description1");
    }

    public static Produit getProduitSample2() {
        return new Produit().id(2L).nom("nom2").description("description2");
    }

    public static Produit getProduitRandomSampleGenerator() {
        return new Produit().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
