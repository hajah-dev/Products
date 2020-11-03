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

    @GetMapping(path="/user/products")
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
    @GetMapping("/admin/delete")
    public String delete(Long id, int page, String motCle){
        
        produitRepository.deleteById(id);
        return "redirect:/user/products?page="+page+"&motCle="+motCle;
    }

    @GetMapping("/admin/formProduit")
    public String form(Model model){
        model.addAttribute("produit", new Produit());
        return "formproduit";
    }

    @PostMapping("/admin/save")
    public String save(@Valid Produit produit, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()) return "formproduit";
       
        produitRepository.save(produit);
        return "redirect:/user/products";
    }

    @GetMapping("/admin/edit")
    public String edit(Model model,Long id){
       
        Produit produit = produitRepository.findById(id).get();
        model.addAttribute("produit", produit);
        return "EditProduit";
    }

    @GetMapping("/")
    public String def(){
        return "redirect:/user/products";
    }

    @GetMapping("/403")
    public String notAutorized(){
        return "403";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
