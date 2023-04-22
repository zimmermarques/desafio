<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
            <html>

            <head>
                <meta charset="utf-8">
                <meta http-equiv="x-ua-compatible" content="ie=edge">
                <title>Edição de Projeto</title>
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
                <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.4.1/css/all.css">

                <script src="https://code.jquery.com/jquery-3.3.1.min.js"
                    crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js"
                    integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
                    crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js"
                    integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
                    crossorigin="anonymous"></script>
            </head>

            <body>
                <div class="container my-5">
                    <h3>Edição de Projeto</h3>
                    <div class="card">
                        <div class="card-body">
                            <div class="col-md-10">
                                <form:form action="/salvar" method="post" modelAttribute="projeto">

                                    <form:hidden path="id" id="id" />
                                    <form:hidden path="status" id="statusProjeto" />
                                    <div class="row">
                                        <div class="form-group col-md-8">
                                            <label class="col-form-label">Status Atual : ${statusProjeto}</label><br />
                                        </div>

                                        <div class="form-group col-md-8">
                                            <label for="nome" class="col-form-label">Nome</label><br />
                                            <form:input type="text" path="nome" id="nome" required="required"
                                                size="40" />
                                        </div>

                                        <div class="form-group col-md-8">
                                            <label for="risco"
                                                class="col-form-label">Classificação de Risco</label><br />
                                            <form:select path="risco" id="risco" required="required">
                                                <form:option value=""> --Selecione--</form:option>
                                                <form:option value="BAIXO">Baixo</form:option>
                                                <form:option value="MEDIO">Médio</form:option>
                                                <form:option value="ALTO">Alto</form:option>
                                            </form:select>
                                        </div>

                                        <div class="form-group col-md-8">
                                            <label for="dataInicio" class="col-form-label">Data de Início:</label><br />
                                            <form:input type="date" path="dataInicio" id="dataInicio"
                                                required="required" />
                                        </div>

                                        <div class="form-group col-md-8">
                                            <label for="gerenteId" class="col-form-label">Gerente
                                                Responsável:</label><br />
                                            <form:select path="gerenteId" id="gerenteId" required="required">    
                                                <form:option value="">Selecione...</form:option>
                                                <c:forEach var="mem" varStatus="memStatus" items="${pessoaLista}">
                                                    <form:option value="${mem.id}">${mem.nome}</form:option>
                                                </c:forEach>
                                            </form:select>    

                                        </div>

                                        <div class="form-group col-md-8">
                                            <label for="previsaoTermino" class="col-form-label">Previsão
                                                Término:</label><br />
                                            <form:input type="date" path="previsaoTermino" id="previsaoTermino"
                                                required="required" />
                                        </div>

                                        <div class="form-group col-md-8">
                                            <label for="dataRealTermino" class="col-form-label">Data Real de
                                                Término:</label><br />
                                            <form:input type="date" path="dataRealTermino" id="dataRealTermino" />
                                        </div>

                                        <div class="form-group col-md-8">
                                            <label for="orcamentoTotal" class="col-form-label">Orçamento
                                                Total:</label><br />
                                            <form:input type="number" min="0.00" max="100000000.00" step="0.01" path="orcamento" id="orcamento" required="required" />
                                        </div>

                                        <div class="form-group col-md-8">
                                            <label for="descricao" class="col-form-label">Descrição</label><br />
                                            <form:textarea path="descricao" rows="7" cols="25" id="descricao" />
                                        </div>

                                        <c:if test = "${statusProjeto != 'ENCERRADO'}">        
                                            <div class="col-md-6">
                                                <input type="submit" class="btn btn-primary" value=" Salvar ">
                                            </div>
                                        </c:if>

                                    </div>
                                    
                                    <br />

                                    <c:if test = "${projeto.id != null}">

                                        <button type="button" class="btn btn-primary" data-toggle="modal"
                                            data-target="#exampleModal" onclick="selecionarProjeto()">
                                            Dar Continuidade no Projeto...
                                        </button>

                                        <button type="button" class="btn btn-primary" data-toggle="modal"
                                            data-target="#membroModal" >
                                            Associar Membros
                                        </button>

                                    </c:if>
                                  

                                    <button type="button" class="btn btn-primary" 
                                            onclick="window.location.href='/';">
                                            Voltar
                                    </button>

                                    <script>
                                        function selecionarProjeto() {
                                                                  
                                            var id = $("#id").val();
                                            $.ajax({
                                                type: "GET",
                                                contentType: "application/json",
                                                url: "http://localhost:8080/selecionar-projeto/"+id,
                                                dataType: 'json',
                                                cache: false,
                                                timeout: 600000,
                                                success: function (data) {

                                                    if(data.statusProjeto==='ENCERRADO'){
                                                        $('#statusConteudo').html("Projeto Encerrado: ");
                                                    }else{
                                                        $('#nomeProjeto').html("Projeto: " + data.projeto.nome);
                                                        $('#statusAtual').html("Status Atual: " + data.projeto.status);
                                                        $('#proximoStatus').html(data.proximoStatus);
                                                    }


                                                    console.log("SUCCESS : ", data);

                                                },
                                                error: function (e) {

                                                    console.log("ERROR : ", e);

                                                }
                                            });

                                        }

                                        function darContinuidade(){
                                            var id = $("#id").val();
                                            
                                            var postPa = {
                                                "id" : id
                                            }

                                            $.ajax({
                                                type: "POST",
                                                contentType: "application/json",
                                                url: "http://localhost:8080/dar-continuidade/" + id,
                                                cache: false,
                                                timeout: 600000,
                                                success: function (data) {

                                                    window.location.href = "http://localhost:8080";
                                                    //console.log("SUCCESS : ", data);

                                                },
                                                error: function (e) {

                                                    console.log("ERROR : ", e);

                                                }
                                            });
                                        }

                                    </script>
                                </form:form>

                                <div class="modal fade" id="membroModal" tabindex="-1" role="dialog"
                                    aria-labelledby="membroModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="membroModalLabel">Membros do Projeto</h5>
                                                <button type="button" class="close" data-dismiss="modal"
                                                    aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <span style="font-weight: bolder;">Membros Atuais</span>
                                                <div class="checkbox-list">
                                                    <c:forEach var="membro" varStatus="membroStatus" items="${projeto.membroLista}">
                                                        <a href="/remover-membro-projeto/${membro.id}/${projeto.id}" onclick="return confirm('Deseja remover do projeto?')">
                                                            Remover
                                                        </a>
                                                        <c:out value="${membro.nome}" /><br>
                                                    </c:forEach>
                                                </div>                                                

                                                <br/>

                                                <hr class="hr" />

                                                <span style="font-weight: bolder;">Membros fora do Projeto</span> 
                                                <div class="checkbox-list">
                                                    <c:forEach var="mem" varStatus="memStatus" items="${membroLista}">
                                                        <a href="/adicionar-membro-projeto/${mem.id}/${projeto.id}" onclick="return confirm('Deseja adicionar no projeto?')">
                                                            Adicionar
                                                        </a>
                                                        <c:out value="${mem.nome}" /><br>
                                                    </c:forEach>
                                                </div>                                                

                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"
                                                    data-dismiss="modal">Fechar</button>
                                                
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog"
                                    aria-labelledby="exampleModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="exampleModalLabel">Continuidade do Projeto</h5>
                                                <button type="button" class="close" data-dismiss="modal"
                                                    aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <div id="statusConteudo">

                                                    
                                                    <div id="nomeProjeto"></div>
                                                    <div id="statusAtual"></div>
                                                    
                                                    <button type="button" class="btn btn-primary" onclick="darContinuidade()" >Prosseguir com Projeto para-><span id="proximoStatus" ></span></button>
                                                </div>

                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"
                                                    data-dismiss="modal">Fechar</button>
                                                
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </body>

            </html>