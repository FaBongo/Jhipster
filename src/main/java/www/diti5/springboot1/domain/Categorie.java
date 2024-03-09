package www.diti5.springboot1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Categorie.
 */
@Table("categorie")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Categorie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nom")
    private String nom;

    @Column("description")
    private String description;

    @Column("depth")
    private Integer depth;

    @Column("nbre_produit")
    private Long nbreProduit;

    @Transient
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Categorie parent;

    @Column("parent_id")
    private Long parentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Categorie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Categorie nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Categorie description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDepth() {
        return this.depth;
    }

    public Categorie depth(Integer depth) {
        this.setDepth(depth);
        return this;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Long getNbreProduit() {
        return this.nbreProduit;
    }

    public Categorie nbreProduit(Long nbreProduit) {
        this.setNbreProduit(nbreProduit);
        return this;
    }

    public void setNbreProduit(Long nbreProduit) {
        this.nbreProduit = nbreProduit;
    }

    public Categorie getParent() {
        return this.parent;
    }

    public void setParent(Categorie categorie) {
        this.parent = categorie;
        this.parentId = categorie != null ? categorie.getId() : null;
    }

    public Categorie parent(Categorie categorie) {
        this.setParent(categorie);
        return this;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long categorie) {
        this.parentId = categorie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categorie)) {
            return false;
        }
        return getId() != null && getId().equals(((Categorie) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categorie{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", depth=" + getDepth() +
            ", nbreProduit=" + getNbreProduit() +
            "}";
    }
}
