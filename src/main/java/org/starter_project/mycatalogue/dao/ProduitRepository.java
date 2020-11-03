package org.starter_project.mycatalogue.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.starter_project.mycatalogue.entities.Produit;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    //public Produit findById(Long id);
    /**
     * Pour Spring data "find" == SELECT FROM PRODUIT
     *                    "by" == WHERE
     *           "Designation" == DESIGNATION
     *              "Contains" == LIKE
     * "Designation" étant un attribut qui se trouve dans la class Produit.
     * Comme on veut que Spring nous retourne les produits en pagination,
     * on va spécifier Page comme retour de la méthode. Et quand on spécifie
     * Page comme retour de la méthode, il faut absolument ajouter un 2ème
     * paramètre à la méthode. Un paramètre de type Pageable. Une fois
     * qu'on ait défini cette méthode, on peut maintenant faire une recherche
     */
    public Page<Produit> findByDesignationContains(String mc, Pageable pageable);
    @Query("select p from Produit p where p.designation like :x and p.prix>:y")
    public Page<Produit> chercher(@Param("x") String mc, @Param("y") double prixMin, Pageable p);
}
