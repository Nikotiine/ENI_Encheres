package fr.eni.encheres.groupe_2.controller;

import fr.eni.encheres.groupe_2.bll.BuissnessException;
import fr.eni.encheres.groupe_2.bll.UtilisateurManager;
import fr.eni.encheres.groupe_2.bo.Utilisateur;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet (name= "UtilisateurControlleur", value="/utilisateurs/*")
public class UtilisateurControlleur extends HttpServlet {
    UtilisateurManager manager = UtilisateurManager.getInstance();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/signup");
        rd.forward(request,resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("");
    String pseudo = request.getParameter("pseudo");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        String rue = request.getParameter("rue");
        String ville = request.getParameter("ville");
        String codePostal = request.getParameter("codePostal");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        //TODO: faire une fonction qui compare les parametre recu entre password et confirm password => si pas pareil buissness excepiton
        Utilisateur utilisateur = new Utilisateur(pseudo,nom, prenom, email, telephone, rue, codePostal,ville, password);

        try {
    manager.newUtilisateur(utilisateur);

        }catch (BuissnessException e)
        {
            e.printStackTrace();
        }
    }
}
