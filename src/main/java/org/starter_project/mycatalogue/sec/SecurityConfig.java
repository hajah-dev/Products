package org.starter_project.mycatalogue.sec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * La méthode suivante veut dire que Spring connait les utilisateurs qui ont
         * droit d'accéder à l'application.
         * inMemoryAuthentication permet de spécifier statiquement les utilisateurs
         * qui ont le droit d'accès à l'application. Une fois qu'on ait
         * défini ces 2 utilisateurs lesquels un rôle précis leur sont attribués,
         * on peut tester le formulaire.
         * Mais en essayant de nous connecter en utilisant ces noms d'utilisateur et mot
         * de passe, le formulaire ne nous reconnait pas et refuse de nous laisser
         * accéder aux autres pages, car par défaut, Spring Sécurity utilise bcrypt.
         * Autrement dit le mot de passe qu'on a saisi est encodé. Donc:
         * - soit on dit Spring qu'il ne faut pas encoder le mot de passe qu'on a entré
         * - soit on encode le mote de passe nous même en utilisant bcrypt.
         */
        BCryptPasswordEncoder bcpe = getBCPE();
        System.out.println(bcpe.encode("1234"));
        /*auth.inMemoryAuthentication()
                .withUser("admin")
                .password(bcpe.encode("1234"))
                .roles("ADMIN", "USER");
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(bcpe.encode("abcd"))
                .roles("USER");
                */
        /**
         * Pour utiliser jdbc authentication
         * principal et credentials sont des termes propres à Spring Security
         */
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username as principal, password as credentials, active from users where username=?")
                .authoritiesByUsernameQuery("select username as principal, roles as role from users_roles where username=?")
                .rolePrefix("ROLE_")
                .passwordEncoder(bcpe);


        /**
         * Dans les codes précédents, on a encodé les mots de passe, mais on doit dire à
         * Spring Security que quand il va récupérer le mot de passe entré par l'utilisateur,
         * il doit le comparer avec celui qu'on a défini précédemment.
         */
        auth.inMemoryAuthentication().passwordEncoder(bcpe);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * En supprimant super.configure(http) qui a été appelé par défaut par cette
         * méthode, on aura plus d'authentification dans notre application parce qu'on
         * a désactivé la configuration par défaut.
         * Si on veut appliquer le formulaire d'authentification, il faut utiliser
         * le code suivant
         * - http.formLogin() => si comme si on disait à Spring qu'on besoin du formulaire
         * d'authentification. Par défaut cette méthode est configurée de manière à ce qu'on
         * ait pas besoin d'authentification pour parcourir les différents url de l'application.
         * Si on veut instaurer l'authentification pour toutes les requêtes:
         * - http.authorizeRequests().anyRequest().authenticated()
         * Pour attribuer les requêtes seulement pour un rôle spéficique (ex: ADMIN):
         * Dans le code suivant, on attribue les rôles d'enregistrer, de supprimer et
         * d'éditer uniquement à ADMIN.
         * - http.authorizeRequests().antMatchers("/save", "/delete", "/edit").hasRole("ADMIN")
         * Mais si on a un nombre élevé d'url dont l'accès est reservé uniquement pour un utilisateur
         * spécifique, on va:
         * - http.authorizeRequests().antMatchers("/admin/*").hasRole("ADMIN")
         * Avec le code précédent, tous les url qui contiennent /admin/* nécessitent une authentification
         * avec le rôle ADMIN. Mais pour que ça fonctionne, il faut changer dans >ProduitController
         */
        /**
         * Si on veut personaliser le formulaire d'authentification on utilise
         * --> http.formLogin().loginPage("/login");
         * Une fois que c'est fait dans:
         * - > ProduitController.java, on va ajouter une page répondant à l'url : /login
         * - > Créer la page login.html.
         */
        http.formLogin().loginPage("/login");
        http.formLogin();
        //http.authorizeRequests().anyRequest().authenticated();
        //http.authorizeRequests().antMatchers("/save", "/delete","/edit").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/admin/*").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/user/*").hasRole("USER");
        /**
         * Pour pouvoir personalisé les messages c'est à dire afin d'éviter
         * d'afficher les messages du genre (dans le cas où un utilisateur
         * accède à une page ou url dont il n'a pas le privilège)
         * There was an unexpected error (type=Forbidden, status=403).
         * En mettant 403 comme argument de la méthode accessDeniedPage,
         * on doit ajouter un @GetMapping("/403") où la méthode répondant
         * à cet URL doit returner une page 403.html.
         * Ce qui implque qu'on doit également créér une page 403.html
         */
        http.exceptionHandling().accessDeniedPage("/403");
    }

    /**
     *  Quand on a une méthode qui utilise @Bean, une fois que l'objet BCryptPasswordEncoder est
     *  créé, il est placé dans le context de Spring.
     *  Par exemple admettons qu'on est dans le contrôleur, si on veut injecter/ utiliser
     *  le même objet créé, il suffit de déclarer un objet BCryptPasswordEncoder et d'utiliser
     *  l'annotation @Autowired.
     */
    @Bean
    BCryptPasswordEncoder getBCPE(){
        return new BCryptPasswordEncoder();
    }
}
