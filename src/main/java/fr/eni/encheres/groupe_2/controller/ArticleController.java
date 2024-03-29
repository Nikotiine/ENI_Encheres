package fr.eni.encheres.groupe_2.controller;

import fr.eni.encheres.groupe_2.bll.ArticleManager;
import fr.eni.encheres.groupe_2.bll.EnchereManager;
import fr.eni.encheres.groupe_2.bo.Article;
import fr.eni.encheres.groupe_2.bo.Enchere;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ArticleController", value = "/articles/*")
public class ArticleController extends HttpServlet {
    ArticleManager managerArticle = ArticleManager.getInstance();
    EnchereManager managerEnchere = EnchereManager.getInstance();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd =null;
        if(request.getSession().getAttribute("login")!=null){
            if(request.getParameter("id")!=null){
                rd = request.getRequestDispatcher("/detailArticle");
                int id = Integer.parseInt(request.getParameter("id"));
                try {
                    Article  article = managerArticle.getSelectedArticle(id);
                    int montant = managerEnchere.montantMeuilleurOffre(id);
                    boolean isOpen = managerArticle.isOpen(id);
                    if(isOpen){
                        request.getSession().setAttribute("enchereOuverte",true);
                    }
                    if(!isOpen){
                        request.getSession().removeAttribute("enchereOuverte");
                    }
                    request.getSession().setAttribute("detailArticle", article);
                    request.getSession().setAttribute("meuilleurOffre",montant);
                    rd.forward(request,response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            rd=request.getRequestDispatcher("/loginpage");
        }
        rd.forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/accueil");
        if (request.getSession().getAttribute("login")!=null){
            int idUtilisateur = Integer.parseInt(request.getParameter("idUtilisateur"));
            boolean ouverte = request.getParameter("ouverte")!=null;
            boolean mesEncheres = request.getParameter("mes-encheres")!=null;
            boolean remporter =request.getParameter("remporte")!=null;
            boolean enCours =request.getParameter("en-cours")!=null;
            boolean nonDebuter =request.getParameter("non-debuter")!=null;
            boolean terminer =request.getParameter("terminer")!=null;
            int idCategorie = Integer.parseInt(request.getParameter("Categories"));
            String motClef = request.getParameter("search");
            try {
                List<Article> listDesArticles = new ArrayList<>();
                if(!ouverte && !mesEncheres && !remporter && !enCours && !nonDebuter && !terminer){

                    if(motClef.length()==0 && idCategorie==0){
                       listDesArticles=managerArticle.allCatalogue();
                    }

                    listDesArticles =managerArticle.filteredListArticlesByName(motClef,idCategorie);
                }
                if(ouverte){
                    listDesArticles =  managerArticle.filteredListByEnchereOuverte(idCategorie,motClef);

                }

              if(mesEncheres){
                 List<Integer> listeDeMesEncheres = managerEnchere.listeMesEncheres(idUtilisateur);
                 listDesArticles=managerArticle.filteredListByIdArticle(listeDeMesEncheres);
              }
              if(terminer){
                  listDesArticles=managerArticle.filteredByStatusTermnine(idUtilisateur);
              }
              if (nonDebuter){
                  listDesArticles=managerArticle.filteredByStatusNonCommencer();

              }
              if(enCours){
                  listDesArticles=managerArticle.filteredByMesArticles(idUtilisateur);
              }
              if(remporter){

                  listDesArticles=managerArticle.filteredByMesAchats(idUtilisateur);
              }

              request.setAttribute("articlesDisponible", listDesArticles);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        rd.forward(request,response);
    }
}
