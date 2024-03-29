package fr.eni.encheres.groupe_2.bll;

import fr.eni.encheres.groupe_2.bo.Enchere;
import fr.eni.encheres.groupe_2.bo.Utilisateur;
import fr.eni.encheres.groupe_2.dal.DAO;
import fr.eni.encheres.groupe_2.dal.DaoFactory;
import fr.eni.encheres.groupe_2.dal.EncheresDAO;

import java.util.ArrayList;
import java.util.List;

public class EnchereManager {
    private static EnchereManager instance;
    private static DAO<Enchere> enchereDAO = DaoFactory.enchereDAO();
    private static DAO<Utilisateur> utilisateurDAO = DaoFactory.utilisateurDAO();
    private static EncheresDAO encheresFeatureUtilisateur = DaoFactory.encheresFeatureUtilisateur();

        /**
     * Instance de Enchere Manager
     * @return une instance du manager
     */
    public static EnchereManager getInstance(){
        if(instance==null){
            return instance = new EnchereManager();
        }
        return instance;
    }

    /**
     * Contructeur prive
     */
    private EnchereManager() {
    }

    /**
     * recupere catalogueEnchere Private en catalogue Public
     * @return catalogue
     */
    public List<Enchere> catalogue(){
        return catalogueEnchere();
    }


    /**
     * Stocke la liste complet des encheres qui sont en BDD
     * @return liste encheres
     */
    private static List<Enchere> catalogueEnchere(){
        return enchereDAO.selectALL();
    }

    /**
     * Faire une enchere , verifie si le montant est bien surperieur a l'enchere precedente`
     * @param enchere Enchere faite a partir de jsp card-detail-article
     * @throws BuissnessException remonte une erreur en cas de non coformite d'enchere
     */
    public void faireEnchere(Enchere enchere) throws BuissnessException {
        boolean nouvelleEnchere = nouvelleEnchere(enchere.getNo_article());
        boolean enchereValable = enchereValable(enchere.getNo_article(),enchere.getMontantEnchere());
        int creditDisponible = creditDisponible(enchere.getNo_utilisateur());
        boolean isDernierEncherisseur = false;
        if(!nouvelleEnchere){
           isDernierEncherisseur = isDernierEncherisseur(enchere.getNo_utilisateur(),enchere.getNo_article());
        }

        if(creditDisponible<enchere.getMontantEnchere()){
            throw new BuissnessException(CodeErrorBll.CREDIT_INSUFFISANT);
        }
        if(nouvelleEnchere){
            enchereDAO.addNew(enchere);

        }
        else if(enchereValable && !isDernierEncherisseur) {
            int idArticle = enchere.getNo_article();
            //Met à jour les credits l'ancien Encherisseur => lui rend sa mise
            Enchere ancienneEnchere =  getAncienneEnchere(idArticle);
            int creditAncienEncherisseur = creditDisponible (ancienneEnchere.getNo_utilisateur());
            int nouveauCredit = creditAncienEncherisseur + ancienneEnchere.getMontantEnchere();
            encheresFeatureUtilisateur.updateCredit(ancienneEnchere.getNo_utilisateur(),nouveauCredit);
            enchereDAO.addNew(enchere);

        }
        // Met à jour Credits Utilisateurs => debit la mise de son compte
        int creditRestant = creditDisponible-enchere.getMontantEnchere();
        encheresFeatureUtilisateur.updateCredit(enchere.getNo_utilisateur(),creditRestant);
    }

    /**
     * Permet de connaitre la derniere enchere faite sur cet article
     * @param idArticle le numero de l'article
     * @return un objet enchere alimenter avec la derniere enchere en date
     */
    private Enchere getAncienneEnchere(int idArticle) {
        List<Enchere> enchereList = catalogueEnchere();
        Enchere ancienneEnchere = null;

        for (Enchere e:enchereList) {
            if(e.getNo_article()==idArticle) {

                ancienneEnchere = e;
            }
        }return ancienneEnchere ;
    }

    /**
     * renvoie la meuilleure offre en cours sur un article donnee
     * @param id l'id de l'article
     * @return le montant de la meuilleur offre
     */
    public int  montantMeuilleurOffre(int id){
        List<Enchere> listeDesEncheresEnCours = catalogueEnchere();
        int montant =0;
        for (Enchere e:listeDesEncheresEnCours
             ) {
            if(e.getNo_article()==id){
                montant=e.getMontantEnchere();
            }
        }
        return montant;
    }




    /**
     * Defini si c'est une premire enchere ou si c'est un update d'une ancienne
     * @param idArticle l'id de l'article encheri
     * @return si oui ou no nc'est une nouvelle enchere a mettre en bdd
     */
    private boolean nouvelleEnchere(int idArticle){
        boolean nouvelleEnchere=true;
        List<Enchere> listeDesEncheresEnCours = catalogueEnchere();
        for (Enchere e:listeDesEncheresEnCours
             ) {
            if(e.getNo_article()==idArticle){
                nouvelleEnchere=false;
            }
        }
        return nouvelleEnchere;
    }

    /**
     * Filtre la liste des article mis en enchere par l'ulistateur
     * @param id id de l'utilisateur
     * @return la liste des id de ses produit mis en encheres
     */
    public List<Integer> listeMesEncheres(int id){
        List<Enchere> listeDesEncheres = catalogue();
        List<Integer> listeDesEnchresArenvoyer = new ArrayList<>();
        for (Enchere e:listeDesEncheres
             ) {
            if(e.getNo_utilisateur()==id){
                int idDeLenchers = e.getNo_article();
                listeDesEnchresArenvoyer.add(idDeLenchers);
            }
        }
        return listeDesEnchresArenvoyer;
    }

    /**
     * Verifie si le montant propose par l'utilisateur est superieur la la derniere meuilleur offre
     *
     * @param idArticle le numero de l'article encheri
     * @param montant le montant de l'enchere demander
     * @return si oui ou non c'est superieur
     * @throws BuissnessException remonte un message d'erreur dans la jsp
     */
    private boolean enchereValable(int idArticle , int montant) throws BuissnessException {
        List<Enchere> listeDesEncheresEnCours = catalogueEnchere();
        for (Enchere e:listeDesEncheresEnCours
             ) {
            if(e.getNo_article()==idArticle){
              if(e.getMontantEnchere()>montant){
                  throw new BuissnessException(CodeErrorBll.MONTANT_ENCHERE_INVALIDE);
              }
            }
        }
        return true;
    }

    /**
     * Veridier les credit dispo de l'utlisateur .
     * @param idUtilisateur
     * @return les credits disponibles
     */
    public static int  creditDisponible(int idUtilisateur){
        List<Utilisateur> utilisateurs = utilisateurDAO.selectALL();
        int creditDispo = 0;
        for (Utilisateur u: utilisateurs
             ) {
            if(u.getNoUtilisateur()==idUtilisateur){
                creditDispo=u.getCredit();
            }
        }
        return creditDispo;

    }

    /**
     * Verifie si user est dernier encherisseur pour empecher rencherir sur meme enchere
     * @param idUtilisateur
     * @param noArticle
     * @return true si user dernier
     * @throws BuissnessException
     */
  private boolean isDernierEncherisseur (int idUtilisateur, int noArticle) throws BuissnessException {
        boolean isDernier = false;
        List<Enchere> enchereList = catalogueEnchere();
        List<Enchere> listeMemeArticle = new ArrayList<>();
        for (Enchere e: enchereList) {
          if (e.getNo_article()==noArticle){
             listeMemeArticle.add(e);
          }
      }
        int index = listeMemeArticle.size();
      Enchere derniereEnchere = listeMemeArticle.get(index-1);
        if(derniereEnchere.getNo_utilisateur() == idUtilisateur) {
            throw new BuissnessException(CodeErrorBll.MEME_ENCHERISSEUR);
        }

      return  isDernier;

  }

}
