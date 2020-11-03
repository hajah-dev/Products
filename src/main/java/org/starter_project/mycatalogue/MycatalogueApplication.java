package org.starter_project.mycatalogue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.starter_project.mycatalogue.dao.ProduitRepository;
import org.starter_project.mycatalogue.entities.Produit;

import java.util.List;

@SpringBootApplication
public class MycatalogueApplication implements CommandLineRunner {
    /**
     * Injection de dépendance en utilisant @Autowired ou un constructeur.
     */
    @Autowired
    private ProduitRepository produitRepository;

    public static void main(String[] args) {
        SpringApplication.run(MycatalogueApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /**
         * En redéfinissant cette méthode: SpringApplication.run() va démarrer, et quand
         * tout est chargé en mémoire, il va faire appel à cette méthode run(String...args)
         */
        System.out.println("**************************");
        produitRepository.save(new Produit(null, "Imprimante Dell", 6500, 11));
        produitRepository.save(new Produit(null, "Imprimante HP", 3200, 20));

//        produitRepository.save(new Produit(null, "Smart phone", 1750, 30));
//        produitRepository.save(new Produit(null, "Scaner HP", 850, 25));
//        produitRepository.save(new Produit(null, "Computer Office Dell", 1250, 15));

        /**
         * Le code suivant va nous retourner une liste de produit se trouvant à la page donnée
         * en paramètre de la fonction findAll() qui accepte un objet de type Pageable.
         * Donc dans l'argument de la fonction, on a défini le numéro de la page :0,
         * et sa taille ou le nombre d'enregistrement qu'on veut récupérer : 2 (dans notre exemple)
         * En utilisant findAll avec le paramètre de type Pageable, on obtient en retour un objet de type
         * page au lieu de la liste prononcée précédemment.
         */
        //Page<Produit> produits=produitRepository.chercher("%H%",1200, PageRequest.of(0,2));
        /**
         * Si on veut afficher les informations sur la page
         */
//        System.out.println(produits.getSize());
//        System.out.println(produits.getTotalElements());
//        System.out.println(produits.getTotalPages());
//        System.out.println(produits.getContent());
        /**
         * Pour parcourir la liste selon les pages définies précédamment.
         */
//        produits.getContent().forEach(p->{
//            System.out.println(p);
//        });





    }
}
