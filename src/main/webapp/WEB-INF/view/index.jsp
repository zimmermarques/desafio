<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <link rel="stylesheet"href="https://use.fontawesome.com/releases/v5.4.1/css/all.css">
</head>

<body>
    <div class="container my-2">
        <div class="card">
            <div class="card-body">
                <div class="container my-6">
                    <p class="my-5">
                        <a href="/novo" class="btn btn-primary">
                            <i class="fas fa-folder-plus ml-2"> Add Projeto </i>
                        </a>
                    </p>
                    <div class="col-md-14">
                            <c:if test="${error != ''}">

                                <script>
                                    alert("${error}");    
                                </script>
                            </c:if>

                            <div>
                                <table class="table table-striped table-responsive-md">
                                    <thead>
                                        <tr>
                                          <th>ID</th>
                                          <th>Nome</th>
                                          <th>Classificação</th>
                                          <th>Data Início</th>
                                          <th>Gerente Responsável</th>
                                          <th>Previsão Término</th>
                                          <th>Data Real Término</th>
                                          <th>Orçamento Total</th>
                                          <th>Status</th>
                                          <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="p" items="${projetoLista}">
                                            <tr>
                                                <td>${p.id}</td>
                                             
                                                <td>${p.nome}</td>
                                                <td>${p.classificacao}</td>
                                                <td><fmt:formatDate pattern="dd/MM/yyyy" value="${p.dataInicio}" /></td>
                                                <td>${p.gerenteResponsavel}</td>
                                                <td><fmt:formatDate pattern="dd/MM/yyyy" value="${p.previsaoTermino}" /></td>
                                                <td><fmt:formatDate pattern="dd/MM/yyyy" value="${p.dataRealTermino}" /></td>
                                                <td>R$ ${p.orcamentoTotal}</td>
                                                <td>${p.ultimoStatusMomento.status}</td>
                                                <td>
                                                   <a href="/editar/${p.id}">Editar</a>
                                                
                                                      &nbsp;&nbsp;&nbsp;
                                                      
                                                   <a href="/remover/${p.id}" onclick="return confirm('Deseja Excluir?')">Remover</a>
                                                
                                                </td>
                                             
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                      
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>