package fr.eni.encheres.groupe_2.bll;

import fr.eni.encheres.groupe_2.bo.Article;
import fr.eni.encheres.groupe_2.bo.Categorie;
import fr.eni.encheres.groupe_2.bo.Enchere;
import fr.eni.encheres.groupe_2.bo.Utilisateur;
import fr.eni.encheres.groupe_2.dal.DAO;
import fr.eni.encheres.groupe_2.dal.DaoFactory;
import fr.eni.encheres.groupe_2.dal.EncheresDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ArticleManager {

    private static ArticleManager instance;

    private static DAO<Article> articleDAO = DaoFactory.articleDao();

    private static EncheresDAO encheresFeatureUtilisateur = DaoFactory.encheresFeatureUtilisateur();

    private static DAO<Enchere> enchereDAO = DaoFactory.enchereDAO();

    /**
     * Instance de Article Manager
     * @return une instance du manager
     */
    public static ArticleManager getInstance() {
        if (instance == null) {
            return instance = new ArticleManager();
        }
        return instance;
    }

    /**
     * Constructeur prive
     */
    private ArticleManager() {
    }

    /**
     * Charge le catalogue d'article existant en BDD
     * @return toute la liste d'article en BDD
     */
    private static List<Article> catalogueArticle(){
        return articleDAO.selectALL();
    }

    /**
     * Permet d'acceder au catalogue hors du controller
     * @return toute la liste d'article stocker en static
     */
    public List<Article> catalogueEnchereOuverte(){
        List<Article> enchereouverte =new ArrayList<>();
        List<Article> toutLesArticles=catalogueArticle();
        for (Article a:toutLesArticles
             ) {
            Date today = new Date();
            Date yesterday = new Date(today.getTime()-(1000 * 60 * 60 * 24));
            if(a.getDateDebutEncheres().before(today) && a.getDateFinEncheres().after(yesterday) ){

                enchereouverte.add(a);
            }
        }

        return enchereouverte ;
    }

    /**
     * Permet la selection d'un article par son ID
     * @param id int envoyer par le controller
     * @return L'article selectionner
     */
    public Article getSelectedArticle(int id) {
        Article article = null;
        List<Article> listeDesArticle = catalogueArticle();
        for (Article a:listeDesArticle
             ) {
            if(a.getNoArticle()==id){
                article=a;
            }
        }
    return article;
    }

    /**
     * filtre la liste des article par categorie
     * @param idCategorie int id de la categorie
     * @return la liste filtre , si Toute categorie , renvoie la liste du catalogue
     */
    public List<Article> filteredByCategorie(int idCategorie) {
        List<Article> listeafiltrer = catalogueArticle();
        List<Article> listefiltre = new ArrayList<>();
        if(idCategorie==0){
            return listeafiltrer;
        }
        for (Article a : listeafiltrer){
            if (a.getNoCategorie()==idCategorie){
            listefiltre.add(a);
             }
        }
        return listefiltre;
    }

    /**
     * Filtre les aricle par nom ( lettre contenues dans le titre de l'article)
     * @param motClef le nom de l'article (String)
     * @param idCategorie et l'id de se categorie (si O = toutes les categories)
     * @return La liste filtre des articles
     */
    public List<Article> filteredListArticlesByName(String motClef , int idCategorie) {
        List<Article> listeAfiltrer = filteredByCategorie(idCategorie);
        List<Article> listeFiltre = new ArrayList<>();

        for (Article b : listeAfiltrer
        ) {
            if (b.getNomArticle().toLowerCase().contains(motClef.toLowerCase())){
                listeFiltre.add(b);
            }

        }
        return listeFiltre;
    }

    /**
     *Filtre les Article disponible pour les encheres ouverte a partir du jour de requete
     * @param idCategorie l'id de la categorie (si O = toutes les categories)
     * @param motClef   le nom de l'article (String) si renseigner
     * @return liste encheres ouverte
     */
    public List<Article> filteredListByEnchereOuverte(int idCategorie,String motClef){
        List<Article> listeafiltrer = filteredListArticlesByName(motClef,idCategorie);
        List<Article> listarenvoyer = new ArrayList<>();

            for (Article a:listeafiltrer
                 ) {

                Date today = new Date();
                Date yesterday = new Date(today.getTime()-(1000 * 60 * 60 * 24));
              // Date tomorrow = new Date(today.getTime()+ (1000 * 60 * 60 * 24));

                if(a.getDateDebutEncheres().before(today) && a.getDateFinEncheres().after(yesterday)){
                    listarenvoyer.add(a);
                }
            }

        return listarenvoyer;
    };

    /**
     * Filtre la liste des articles (des enchers ouverte) par numero d'article
     * @param listeDesIdArticles
     * @return liste des articles
     */
    public List<Article> filteredListByIdArticle(List<Integer> listeDesIdArticles){
        List<Article> listeafiltrer = catalogueEnchereOuverte();
        List<Article> listeFiltre =new ArrayList<>();
        for (Article a:listeafiltrer
             ) {
           if (listeDesIdArticles.contains(a.getNoArticle())){
               listeFiltre.add(a);
           }
        }
        return listeFiltre;
    }

    /**
     * filtre la liste des article dont les encheres sont terminer
     * @return liste des articles
     */
    public List<Article> filteredByStatusTermnine(int idUtilisateur){
        List<Article> listeafiltrer =catalogueArticle();
        List<Article> listeFiltrer=new ArrayList<>();
        Date today = new Date();
        for (Article a:listeafiltrer
             ) {
            if (a.getDateFinEncheres().before(today) && a.getUtilisateur().getNoUtilisateur() == idUtilisateur){
                listeFiltrer.add(a);
            }
        }
        return listeFiltrer;
    }

    /**
     * filtre la liste des article dont les encheres sont pas commencer
     * @return liste des articles
     */
    public List<Article> filteredByStatusNonCommencer(){
        List<Article> listeafiltrer =catalogueArticle();
        List<Article> listeFiltrer=new ArrayList<>();
        Date today = new Date();
        //TODO verifier les methode de date
        for (Article a:listeafiltrer
             ) {
            if(a.getDateDebutEncheres().after(today)){
                listeFiltrer.add(a);
            }
        }
        return listeFiltrer;
    }

    /**
     * filtre la liste des articles dont l'utlisateur est le vendeur
     * @param idUtilisateur int noUtilisteur ( vendeur )
     * @return liste des articles
     */
    public List<Article> filteredByMesArticles(int idUtilisateur){
        List<Article> toutLesArticles=catalogueArticle();
        List<Article> listeFiltrer=new ArrayList<>();
        for (Article a:toutLesArticles
             ) {
            if(a.getUtilisateur().getNoUtilisateur()==idUtilisateur){
                listeFiltrer.add(a);
            }
        }
        return listeFiltrer;
    }
    public List<Article> filteredByMesAchats(int idUtilisateur){
        List<Article> listeDesArticles= filteredByStatusTermnine(idUtilisateur);
        List<Enchere> listeDesEnchere = enchereDAO.selectALL();
        List<Article> mesAchats = new ArrayList<>();
        for (Article a:listeDesArticles
             ) {
            if(a.getPrixVente()>0){
                for (Enchere e:listeDesEnchere
                     ) {
                    if(a.getNoArticle()==e.getNo_article() && a.getPrixVente()==e.getMontantEnchere() && a.getNoArticle()==idUtilisateur)
                        mesAchats.add(a);
                }
            }
        }
        return mesAchats;
    }

    /**
     * Ajoute un nouvel article a vendre en BDD
     * @param article Article renseigner par l'utlisateur
     * @throws BuissnessException Renvoye une erreur a l'utlistateur en cas de lever d'exception
     */
    public void addNewArticle(Article article) throws BuissnessException {

        if(article.getDateDebutEncheres().after(article.getDateFinEncheres())){
            throw new BuissnessException(CodeErrorBll.CHAMP_INVALIDE);
        }
        else {
            articleDAO.addNew(article);
        }

    }

    /**
     * Verifie a la connextion de l'utlisateur si ses produit mis en vente sont en ecnhere termnie et recupere le
     * montant le plus eleve des encheres faite , si aucune enchere sur l'article , la valeur de vente finale passe a -1
     * @param idUtilisateur le numero de l'utlisteur qui se connecte
     */
    public int verifEnchereFini(int idUtilisateur) {
        List<Article> mesArticles = filteredByMesArticles(idUtilisateur);
        List<Enchere> listeDesEncheres = enchereDAO.selectALL();
        Date today = new Date();
        int montant =0;
        int totalGagneVenteFini=0;
        for (Article a:mesArticles) {
            if(a.getDateFinEncheres().before(today) && a.getPrixVente()==0) {
                montant = 0;
                for(Enchere e:listeDesEncheres) {
                    // Si le numero de l'article correspond a l'"enchere et le montant est plus eleve
                    if(e.getNo_article() == a.getNoArticle() && e.getMontantEnchere() > montant){
                            montant = e.getMontantEnchere();
                    }
                }
                if(montant > 0){
                    // on peut crediter pour cet article
                    int nouveauTotal = montant + EnchereManager.creditDisponible(idUtilisateur);
                    totalGagneVenteFini = totalGagneVenteFini+montant;
                    encheresFeatureUtilisateur.updatePrixVente(a.getNoArticle(),montant);
                    encheresFeatureUtilisateur.updateCredit(idUtilisateur,nouveauTotal);
                } else {
                    // il n'y a pas eu d'enchere on credite a -1 pour par boucler dessus
                    encheresFeatureUtilisateur.updatePrixVente(a.getNoArticle(),-1);
                }
            }
        }
        return totalGagneVenteFini;
    }

    /***
     * Rembouses les utlisateurs ayant encherie sur un article en status non termnier et ouvert
     * si le possesseur des articles ferme son compte
     * @param idUtilisateur le numero de l'utlisateur qui cloture son compte
     */
    public void rembourseEncherisseur(int idUtilisateur) {
        List<Article> mesArticles = filteredByMesArticles(idUtilisateur);
        List<Enchere> listeDesEncheres = enchereDAO.selectALL();
        Date today = new Date();
        Date yesterday = new Date(today.getTime()-(1000 * 60 * 60 * 24));
        int montant =0;
        int idEncherisseur=0;
        for (Article a:mesArticles
             ) {
            if( a.getDateFinEncheres().after(yesterday)){
                montant =0 ;
                idEncherisseur=0;
                for (Enchere e:listeDesEncheres
                     ) {
                    if(e.getNo_article()==a.getNoArticle() && e.getMontantEnchere()>montant){
                        //retrouve l'id du meuilleur encherisseur et le montant de son enchere
                        montant=e.getMontantEnchere();
                        idEncherisseur=e.getNo_utilisateur();
                    }
                }
                if(montant>0){
                    // remet a jour les credit de l'encherisseur
                    int nouveauTotal = montant + EnchereManager.creditDisponible(idEncherisseur);
                    encheresFeatureUtilisateur.updateCredit(idEncherisseur,nouveauTotal);
                }
            }
        }

    }

    /**
     * defini si l'enchere est accecible a l'utilisateur (enchere ouverte)
     * @param id le numero de l'article
     *
     * @return bollean oui ou non sur ouverture encheres
     */
    public boolean isOpen(int id) {
        boolean isOpen =false;
        Date today = new Date();
        Date yesterday = new Date(today.getTime()-(1000 * 60 * 60 * 24));
        Article article = getSelectedArticle(id);
        if(article.getDateDebutEncheres().before(today) && article.getDateFinEncheres().after(yesterday)){
            isOpen=true;
        }
        return isOpen;
    }

    public List<Article> allCatalogue() {
        return catalogueArticle();
    }
}

