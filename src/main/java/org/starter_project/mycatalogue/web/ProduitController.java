package org.starter_project.mycatalogue.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.starter_project.mycatalogue.dao.ProduitRepository;
import org.starter_project.mycatalogue.entities.Produit;

import javax.validation.Valid;
import java.util.List;

/**
 * Comme la classe qu'on va définir est un controler, on va
 * utiliser l'annotation @Controller
 */
@Controller
public class ProduitController {
    @Autowired
    private ProduitRepository produitRepository;

    @GetMapping(path="/products")
    public String products(Model model,
                           @RequestParam(name="page", defaultValue = "0") int page1,
                           @RequestParam(name="motCle", defaultValue = "") String mc) {
        Page<Produit> pageProduit=produitRepository.findByDesignationContains(mc,PageRequest.of(page1,12));
        model.addAttribute("totalTrouve",pageProduit.getTotalElements());

        model.addAttribute("listProduits",pageProduit.getContent());

        model.addAttribute("pages", new int[pageProduit.getTotalPages()]);

        model.addAttribute("currentPage",page1);

        /**
         * Pour garder le mot clé chercher dans la zone de text
         */
        model.addAttribute("motCle", mc);
        return "products"; // products.html
    }
    @GetMapping("/delete")
    public String delete(Long id, int page, String motCle){
        /**
         * On peut ne pas mettre @RequestParam parce que par défaut, Spring
         * cherche un paramètre qui s'appelle id.(Le paramètre porte le même nom que
         * la variable)
         */
        produitRepository.deleteById(id);
        // Dans le code suivant on va demander à Spring de nous rediriger vers
        // "/products" et la page courante où on a suprrimé un produit en gardant
        // le mot clé dans le cas où il s'agirait d'une suppression après une recherche.
        return "redirect:/products?page="+page+"&motCle="+motCle;
    }


    /**
     * Validation:
     * Pour procéder aux validations: quand on appel le formulaire, on a besoin d'y utiliser
     * le Model (interface appartenant à Spring) comme argument. Une fois qu'on ait
     * défini l'attribut qu'on va exploiter dans formproduit.html, on va y faire pour chaque
     * champ "un data-binding"
     */
    @GetMapping("/formProduit")
    public String form(Model model){
        model.addAttribute("produit", new Produit());
        return "formproduit";
    }

    @PostMapping("/save")
    public String save(@Valid Produit produit, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()) return "formproduit";
        // Si il y a erreur Spring va placer automatiquement bindingResult (erreur) dans Model model.
        produitRepository.save(produit);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String edit(Model model,Long id){
        // On met .get à la fin de la méthode findById(id) pour qu'elle retourne un produit.
        Produit produit = produitRepository.findById(id).get();
        model.addAttribute("produit", produit);
        return "EditProduit";
    }
}
