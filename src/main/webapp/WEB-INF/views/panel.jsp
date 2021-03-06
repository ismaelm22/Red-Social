<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<head>
<script src="http://code.jquery.com/jquery-3.2.1.min.js" integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="   crossorigin="anonymous"></script>
<script type="text/javascript" src="js/index.js"></script>
    <title>Red Social - Bienvenido</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet" media="screen">
</head>
<script language="JavaScript" type="text/javascript">
		if ("${mensaje}" != "") {
			alert("${mensaje}");
		}
	</script>
<body >
    <div id="navBar">
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" >Intravita</a>
                </div>
                <ul class="nav navbar-nav">
                    <li id="liPublicacion" ><a id="aPublicacion" >Publicaciones</a></li>
                    <li id="liAmistad" ><a id="aAmistad" >Amigos</a></li>
                    <li id="liMP"><a id="aMP" >Gente</a></li>
                    <c:if test = "${persona.isEsAdmin() == true}">
                    <li id="liTodasPublicaciones" ><a id="aTodasPublicaciones" >Todas Publicaciones</a></li>
                    <li id="liPanel" class="active"><a id="aPanel"> Panel</a></li>
                    </c:if>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li id="liPerfil"><a id="aPerfil" ><span class="glyphicon glyphicon-user"></span> Perfil</a></li>
                    <li id="liLogout"><a id="aLogout" href="http://localhost:8080/redSocial/"><span href="http://localhost:8080/redSocial/" class="glyphicon glyphicon-log-in"></span> Logout</a></li>
                </ul>
            </div>
        </nav>

    </div>
    
<style>
	.subencabezado {  /*nombre del estilo o botón*/
		border: 2px solid grey;  /*borde: estilo y color*/
		margin-top: 10px;
		font: normal normal 18px quicksand;  /*fuente*/
		color: black;  /*color de la fuente*/
		letter-spacing: 2px; /*separación entre las letras*/
		text-align: center; /*alineación del texto*/
		text-transform: uppercase; /*texto se transforma en mayúsculas*/
		padding: 6px;  /*tamaño del fondo*/
		border-radius: 30px 0px 30px 0px; /*ángulos de las 4 esquinas del borde/fondo*/
	}
	
	.mensaje{
		background: #ffffff ;
		border: 2px solid grey;
		font: normal normal 12px quicksand;  /*fuente*/
		color:black;  /*color de la fuente*/
		letter-spacing: 2px; /*separación entre las letras*/
		text-align: center; /*alineación del texto*/
		text-transform: uppercase; /*texto se tpersonaransforma en mayúsculas*/
		padding: 30px;  /*tamaño del fondo*/
		border-radius: 30px; /*forma la borde del fondo*/
	}
</style>

	
        <!--  PANEL BOTONES INVISIBLES ;D -->
    <div id="divBotonesInvisibles" style="display:none">
    	<form action="amigos" method="get">
            <button  id="holaAmigos" style="display:none; padding-bottom:10px; float: right;margin: auto;" class="btn btn-warning" type="submit" value="Amigos" name="amigos" >Volver</button>
        </form>
        <form action="gente" method="get">
            <button  id="holaGente" style="display:none; padding-bottom:10px; float: right;margin: auto;" class="btn btn-warning" type="submit" value="Gente" name="Gente" >Volver</button>
        </form>
    	<form action="menu" method="get">
            <button  id="holaMenu" style="display:none; padding-bottom:10px; float: right;margin: auto;" class="btn btn-warning" type="submit" value="Menu" name="menu" >Volver</button>
        </form>
        <form action="perfil" method="get">
            <button  id="holaPerfil" style="display:none; padding-bottom:10px; float: right;margin: auto;" class="btn btn-warning" type="submit" value="Perfil" name="perfil" >Volver</button>
        </form>
    	<form action="todasPublicaciones" method="get">
            <button  id="holatodasPublicaciones" style="display:none; padding-bottom:10px; float: right;margin: auto;" class="btn btn-warning" type="submit" value="TodasPublicaciones" name="todasPublicaciones" >Volver</button>
        </form>
        <form action="panel" method="get">
            <button  id="holaPanel" style="display:none; padding-bottom:10px; float: right;margin: auto;" class="btn btn-warning" type="submit" value="Panel" name="panel" >Volver</button>
        </form>
    </div>
    <!--  FIN PANEL BOTONES INVISIBLES ;D -->
    <div id="divTodasPublicaciones" style="height:100%;width:100%; margin-top:-20px; position:absolute;" class="col-lg-12 col-md-12 col-xs-12">
	<div class="subencabezado" style="width:100%">Panel Administración</div>
	<div class="panel panel-warning">
      <div class="panel-heading">¡Ten Cuidado!</div>
      <div class="panel-body">¡Recuerda, vas a poder cambiar los permisos de los usuarios!</div>

    </div>
    
	<div id="panelPanel" style="width:100%;height:80%">
	      <c:forEach var="listValue" items="${listUsuarios}">
				<div id="panelUsuario-${listValue.getUsername()}" class="mensaje" style="margin-top:10px;height:100px; width:100%" >
				<div id="usuario-${listValue.getUsername()}" style="text-align: left" class="col-md-6 col-lg-6 col-xs-6"> ${listValue.getUsername()}</div>
						<!--  PROMOCIONAR USUARIOS -->
				<div class="col-md-6 col-lg-6 col-xs-6"  id="panel-${listValue.getUsername()}">
						<form class="col-md-2 col-lg-2 col-xs-2" style="margin-right: 40px;" action="promocionarUsuario" method="post">
						<input name="promocionar" value="${listValue.getUsername()}" style="display:none">
						<button id="promocionar-${listValue.getUsername()}" class="btn btn-success" name="promocionar-'${listValue.getUsername()}">Promocionar</button>
						</form>
						<!--  REVOCAR USUARIOS -->
						<form class="col-md-2 col-lg-2 col-xs-2" action="revocarUsuario" method="post">
						<input name="revocar" value="${listValue.getUsername()}" style="display:none">
						<button id="revocar-${listValue.getUsername()}" class="btn btn-warning" name="revocar-${listValue.getUsername()}">Revocar</button>
						</form>
						<!--  ELIMINAR USUARIOS -->
						<form class="col-md-2 col-lg-2 col-xs-2" action="eliminarUsuario" method="post">
						<input name="eliminar" value="${listValue.getUsername()}" style="display:none">
						<button id="eliminar-${listValue.getUsername()}" class="btn btn-danger" name="eliminar-${listValue.getUsername()}">Eliminar</button>
						</form>
				</div>
				</div>
			</c:forEach>
	</div>
    </div>
    <script type="text/javascript">
    
    $('#promocionar-${listValue.getUsername()}').click(function(){
    	alert('HPLA');
    });
    $('#btnPublicar').click(function(){
    	var texto=$('#textareaPublicacion').val();
    	$('#panel').append('<div id="publicacion" style="margin-top:10px; margin-left:10px; height:100px; width:90%;border-style:solid" ><div id="mensaje" class="col-md-6 col-lg-6 col-xs-6">'+texto+'</div><div id="perfil" class="col-lg-3 col-md-3 col-xs-3"><p><c:out value="${persona}"/>${persona.getNombre()}</p></div></div>');
    });

    $('#btnAdjuntarFoto').click(function(){
    	alert('Funcionalidad No Disponible');
    });
    $('#liPublicacion').click(function(){
		$('#holaMenu').click();
    });

    $('#aPublicacion').click(function(){
    	$('#holaMenu').click();
    });

    $('#liAmistad').click(function(){
    	$('#holaAmigos').click();
    });

    $('#aAmistad').click(function(){
    	$('#holaAmigos').click();
    });

    $('#liMP').click(function(){
    	$('#holaGente').click();
    });
    $('#liTodasPublicaciones').click(function(){
    	$('#holatodasPublicaciones').click();
    });
	
    $('#aTodasPublicaciones').click(function(){
    	$('#holatodasPublicaciones').click();
    });
    $('#aMP').click(function(){
    	$('#holaGente').click();
    });
    $('#liPerfil').click(function(){
    	$('#holaPerfil').click();
    });
    $('#aPerfil').click(function(){
    	$('#holaPerfil').click();
    });
    $('#liLogout').click(function(){
    	$('#holaMenu2').click();
    });
    $('#aLogout').click(function(){
    	$('#holaMenu2').click();
    });
    $('#liPanel').click(function(){
    	$('#holaPanel').click();
    });
    $('#aPanel').click(function(){
    	$('#holaPanel').click();
    });
</script>
</body>
</html>

