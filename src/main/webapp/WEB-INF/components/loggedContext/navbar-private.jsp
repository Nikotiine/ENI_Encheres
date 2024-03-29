<%--
  Created by IntelliJ IDEA.
  User: niko
  Date: 21/07/2022
  Time: 21:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="message_jsp" var="message"/>
<c:set var="langue_choisie" value="${param.lang}" scope="session"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
<nav class="navbar is-info" role="navigation" aria-label="main navigation">
    <div class="navbar-brand is-info">
        <a class="navbar-item is-info" href="${pageContext.request.contextPath}/encheres/">
            <img src="../../img/logo.png" alt="LogoEniEncheres" id="toto" width="128" height="28">
        </a>

        <a role="button" class="navbar-burger" aria-label="menu" aria-expanded="false" data-target="navbarBasicExample" id="burger">
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
        </a>
    </div>

    <div id="navbar-public" class="navbar-menu is-info">
        <div class="navbar-start">



            <div class="navbar-item has-dropdown is-hoverable ">
                <p class="navbar-link">
                    <fmt:message key="navbar_public_choix_langue" bundle="${message}"/>
                </p>

                <div class="navbar-dropdown is-info">
                    <a href="/encheres?lang=fr" class="navbar-item">
                        <span> <img class="image is-24x24 mr-4" src="../../img/drapeau_fr.png" alt="drapeau francais"> </span>    <fmt:message key="navbar_public_francais" bundle="${message}"/>
                    </a>

                    <a href="/encheres?lang=en" class="navbar-item is-info">
                        <span> <img class="image is-24x24 mr-4" src="../../img/drapeau-en.png" alt="drapeau anglais"> </span>  <fmt:message key="navbar_public_anglais" bundle="${message}"/>
                    </a>

                </div>

            </div>
            <div class="navbar-item">
                <button class="button is-info" onclick="window.location.reload()" > recharger la page </button>

            </div>

        </div>

        <div class="navbar-end">
            <div class="navbar-item">
                <a class="navbar-item" href="${pageContext.request.contextPath}/encheres/">
                    <fmt:message key="navbar_private_auction" bundle="${message}"/>
                </a>
                <a class="navbar-item" href="${pageContext.request.contextPath}/vente">
                    <fmt:message key="navbar_private_sold" bundle="${message}"/>
                </a>
                <a class="navbar-item" href="${pageContext.request.contextPath}/utilisateurs?id=${login.noUtilisateur}">
                    <fmt:message key="navbar_private_profil" bundle="${message}"/>
                </a>
                <a class="button is-dark" href="${pageContext.request.contextPath}/login?logout">
                    <fmt:message key="navbar_private_logout" bundle="${message}"/>
                </a>
            </div>
        </div>
    </div>
</nav>
</body>
</html>
