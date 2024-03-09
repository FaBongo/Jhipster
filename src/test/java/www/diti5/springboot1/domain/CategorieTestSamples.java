package www.diti5.springboot1.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CategorieTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Categorie getCategorieSample1() {
        return new Categorie().id(1L).nom("nom1").description("description1").depth(1).nbreProduit(1L);
    }

    public static Categorie getCategorieSample2() {
        return new Categorie().id(2L).nom("nom2").description("description2").depth(2).nbreProduit(2L);
    }

    public static Categorie getCategorieRandomSampleGenerator() {
        return new Categorie()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .depth(intCount.incrementAndGet())
            .nbreProduit(longCount.incrementAndGet());
    }
}
