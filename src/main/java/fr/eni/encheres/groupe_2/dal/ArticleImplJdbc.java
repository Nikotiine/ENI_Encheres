package fr.eni.encheres.groupe_2.dal;

import fr.eni.encheres.groupe_2.bo.Article;
import fr.eni.encheres.groupe_2.bo.Categorie;
import fr.eni.encheres.groupe_2.bo.Utilisateur;
import javassist.bytecode.analysis.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleImplJdbc<ARTICLES_VENDUS> implements DAO<Article> {
   private final String SELECT_BY_ID_SQL = "SELECT * FROM dbo.ARTICLES_VENDUS WHERE no_article=?";
   private final  String SELECT_ALL_SQL = "SELECT * FROM dbo.ARTICLES_VENDUS JOIN UTILISATEURS U on U.no_utilisateur = ARTICLES_VENDUS.no_utilisateur JOIN CATEGORIES C on C.no_categorie = ARTICLES_VENDUS.no_categorie";

   private final String ADD_NEW_ARTICLE_SQL="INSERT INTO dbo.ARTICLES_VENDUS (nom_article,description,date_debut_encheres,date_fin_encheres,prix_initial,prix_vente,no_utilisateur,no_categorie) VALUES (?,?,?,?,?,?,?,?)";
    PreparedStatement ps;
    ResultSet rs;

    /**
     * Ajoute un article en BDD
     * @param object Article envoye par l'utlisiteur
     */
    @Override
    public void addNew(Article object) {
        PreparedStatement ps =null;
        ResultSet rs=null;
        try(Connection cnx = ConnectionProvider.getConnection()) {
            ps= cnx.prepareStatement(ADD_NEW_ARTICLE_SQL,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1,object.getNomArticle());
            ps.setString(2,object.getDescription());
            ps.setDate(3, (java.sql.Date) object.getDateDebutEncheres());
            ps.setDate(4, (java.sql.Date) object.getDateFinEncheres());
            ps.setInt(5,object.getPrixInitial());
            ps.setInt(6,object.getPrixVente());
            ps.setInt(7,object.getNoUtilisateur());
            ps.setInt(8,object.getNoCategorie());
            ps.executeUpdate();
            rs= ps.getGeneratedKeys();
            while (rs.next()){
                object.setNoArticle(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Article selectById(int id) {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Article article = null;
        try (Connection cnx = ConnectionProvider.getConnection()) {
            ps = cnx.prepareStatement(SELECT_BY_ID_SQL);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int noArticle = rs.getInt("no_article");
                String nomArticle = rs.getString("nom_article");
                String description = rs.getString("description");
                Date dateDebut = rs.getDate("date_debut_encheres");
                Date dateFin = rs.getDate("date_fin_encheres");
                int prixIn = rs.getInt("prix_initial");
                int prixVente = rs.getInt("prix_vente");
                int idUtilisateur = rs.getInt("no_utilisateur");
                int idCategorie = rs.getInt("no_categorie");
                article = new Article(noArticle, nomArticle, description, dateDebut, dateFin, prixIn, prixVente, idUtilisateur, idCategorie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return article;
    }


    @Override
    public void update(Article object) {

    }

    /**
     * Slectionne tous les articles en BDD et les renvoye au manager
     * @return List<Article> de tous les articles
     */
    @Override
    public List<Article> selectALL() {
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<Article> listArticle = new ArrayList<>();
        try (Connection cnx = ConnectionProvider.getConnection()){
            ps = cnx.prepareStatement(SELECT_ALL_SQL);
            rs = ps.executeQuery();
            while (rs.next()){
                int noArticle = rs.getInt("no_article");
                String nomArticle = rs.getString("nom_article");
                String description = rs.getString("description");
                Date dateDebut = rs.getDate("date_debut_encheres");
                Date dateFin = rs.getDate("date_fin_encheres");
                int prixIn = rs.getInt("prix_initial");
                int prixVente = rs.getInt("prix_vente");
                int idUtilisateur = rs.getInt("no_utilisateur");
                int idCategorie = rs.getInt("no_categorie");
                String pseudo = rs.getString("pseudo");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String telephone = rs.getString("telephone");
                String rue = rs.getString("rue");
                String code_postal = rs.getString("code_postal");
                String ville = rs.getString("ville");
                String libelle = rs.getString("libelle");
                Utilisateur vendeur = new Utilisateur(idUtilisateur, pseudo, nom, prenom, email,telephone,rue,code_postal,ville);
                Article article = new Article(noArticle,nomArticle,description,dateDebut,dateFin,prixIn,prixVente,vendeur,idCategorie);
                listArticle.add(article);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listArticle;
    }
}


