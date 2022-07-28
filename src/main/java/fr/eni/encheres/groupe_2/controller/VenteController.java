package fr.eni.encheres.groupe_2.controller;

import fr.eni.encheres.groupe_2.bll.ArticleManager;
import fr.eni.encheres.groupe_2.bll.BuissnessException;
import fr.eni.encheres.groupe_2.bll.RetraitManager;
import fr.eni.encheres.groupe_2.bo.Article;
import fr.eni.encheres.groupe_2.bo.Retrait;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "VenteController", value = "/vente")
public class VenteController extends HttpServlet {
    ArticleManager articleManager = ArticleManager.getInstance();
    RetraitManager retraitManager = RetraitManager.getInstance();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
RequestDispatcher rd = request.getRequestDispatcher("/jspvente");
rd.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd= null;
        if(request.getSession().getAttribute("login")!=null){
            java.util.Date today = new java.util.Date();
            java.util.Date yesterday = new java.util.Date(today.getTime()-(1000 * 60 * 60 * 24));
            rd = request.getRequestDispatcher("/jspvente");
            int idUtilisateur = Integer.parseInt(request.getParameter("id"));
            String nomArticle = request.getParameter("nomArticle");
            String description = request.getParameter("description");
            int idCategorie = Integer.parseInt(request.getParameter("categorie"));
            int prixInitial = Integer.parseInt(request.getParameter("prix"));
            //TODO:Verfier que l'input prix est bioen en numeric
            Date datedebut = Date.valueOf(request.getParameter("datedebut"));
            Date datefin = Date.valueOf(request.getParameter("datefin"));
            String rue = request.getParameter("rue");
            String ville = request.getParameter("ville");
            String codepostal = request.getParameter("codepostal");
            if(idCategorie==0){
                rd = request.getRequestDispatcher("/jspvente");
                request.setAttribute("error", 30004);
                rd.forward(request,response);return;
            }

            if(datefin.before(datedebut)){
                rd = request.getRequestDispatcher("/jspvente");
                request.setAttribute("error", 30002);
                rd.forward(request,response);return;
            }
            if(datedebut.before(yesterday)){
                rd = request.getRequestDispatcher("/jspvente");
                request.setAttribute("error", 30003);
                rd.forward(request,response);return;
            }
            Article article = new Article(nomArticle,description,datedebut,datefin,prixInitial,idUtilisateur,idCategorie);

            try {
                articleManager.addNewArticle(article);
                Retrait retrait = new Retrait(article.getNoArticle(),rue,ville,codepostal);
                retraitManager.addNew(retrait);
                request.setAttribute("succes", 50003);
            } catch (BuissnessException e) {
                request.setAttribute("error", Integer.parseInt(e.getMessage()));
            }
            rd.forward(request,response);
        }
        else {
            request.getRequestDispatcher("/loginpage");
            rd.forward(request,response);
        }

    }


}
