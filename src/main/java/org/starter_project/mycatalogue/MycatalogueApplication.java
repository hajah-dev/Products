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
    
    @Autowired
    private ProduitRepository produitRepository;

    public static void main(String[] args) {
        SpringApplication.run(MycatalogueApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("**************************");     
    }
}
