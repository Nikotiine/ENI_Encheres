<%--
  Created by IntelliJ IDEA.
  User: niko
  Date: 26/07/2022
  Time: 09:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${param.lang}" scope="session"/>
<fmt:setBundle basename="message_jsp" var="message"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/confirm?action=${confirmModal}" method="post" class="form">


    <div class="modal is-active">
        <div class="modal-background"></div>
        <div class="modal-card">
            <header class="modal-card-head">
                <p class="modal-card-title"><fmt:message key="modal_confirmation_confirmer" bundle="${message}"/></p>

            </header>
            <section class="modal-card-body">
                <h2> <fmt:message key="modal_confirmation_password_confirm" bundle="${message}"/> </h2>
                <label for="password"><fmt:message key="sign_up_password" bundle="${message}"/></label>
                <input type="password" name="password" id="password">
                <input name="idUtilisateur" value="${login.noUtilisateur}" hidden>

            </section>
            <footer class="modal-card-foot">
                <button class="button is-success" type="submit" name="valider"><fmt:message
                        key="formulaire_vente_confirm" bundle="${message}"/></button>
                <a href="${pageContext.request.contextPath}/encheres/" class="button"><fmt:message key="sign-up-cancel"
                                                                                                   bundle="${message}"/></a>
            </footer>
        </div>
    </div>
</form>
</body>
</html>