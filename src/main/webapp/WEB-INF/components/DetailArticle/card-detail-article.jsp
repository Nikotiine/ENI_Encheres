<%--
  Created by IntelliJ IDEA.
  User: niko
  Date: 21/07/2022
  Time: 22:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setBundle basename="message_jsp" var="message"/>
<html>
<head>
    <title> <fmt:message key="detail_article_titre" bundle="${message}"/></title>
</head>
<body>
<c:if test="${!empty detailArticle}">
  <div class="card-logged">
    <div class="card-image-logged">
      <figure class="logged-card-image">
        <img src="${pageContext.request.contextPath}/img/wip.jpg" alt="">
      </figure>
    </div>
    <div class="card-content">
      <div class="media">
        <c:if test="${!empty login}">
          <a href="${pageContext.request.contextPath}/articles?id=${article.noArticle}">${article.nomArticle}</a>

        </c:if>
        <c:if test="${empty login}">
          <div class="media-content">
            <p>${article.nomArticle}</p>
          </div>
        </c:if>
      </div>
    </div>
  </div>
  <div class="details-articles">
   <div>
     <p>
       <b><fmt:message key="card_detail_article_nom_article" bundle="${message}"/></b>${detailArticle.nomArticle}
       <br>
       <b><fmt:message key="card_detail_article_description" bundle="${message}"/></b>${detailArticle.description}
       <br>
       <b><fmt:message key="card_detail_article_date_debut" bundle="${message}"/></b>${detailArticle.dateDebutEncheres}
       <br>
       <b><fmt:message key="card_detail_article_date_fin" bundle="${message}"/></b>${detailArticle.dateFinEncheres}
       <br>
       <b><fmt:message key="card_detail_article_prix_in" bundle="${message}"/></b>${detailArticle.prixInitial}
       <br>
       <b><fmt:message key="card_detail_article_montant_enchere" bundle="${message}"/></b>${meuilleurOffre}
       <br>
       <b><fmt:message key="card_detail_article_nom_vendeur" bundle="${message}"/></b>${detailArticle.utilisateur.pseudo}
       <br>
     </p>
   </div>
    <div>
      <c:if test="${!empty enchereOuverte}">
        <form method="post" action="${pageContext.request.contextPath}/details-encheres/">
          <input value="${detailArticle.noArticle}" name="noArticle" hidden>
          <input value="${detailArticle.prixInitial}" name="prixInitial" hidden>
          <input value="${login.noUtilisateur}" name="noUtilisateur" hidden>
          <label for="montant" ><b><fmt:message key="detail_article_montant_enchere" bundle="${message}"/></b></label>
          <input type="number" name="montant" placeholder="montant de votre enchere" id="montant" required>
          <button class="button is-info" name="encherir" type="submit" value="encherir">Encherir</button>
        </form>

      </c:if>

    </div>
  </div>
</c:if>
</body>
</html>
