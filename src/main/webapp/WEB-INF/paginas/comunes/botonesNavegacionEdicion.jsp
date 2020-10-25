<!-- Esta es para la navegacion de la pagina al momento de editar-->
<section id="actions" class="py-4 mb-4 bg-ligth">
    <div class="container">
        <div class="row">
            <div class="col-md-3">
                <!--agregamos un link al JSP de inicio-->
                <a href="index.jsp" class="btn btn-ligth btn-block">
                    <i class="fas fa-arrow-left"></i> Regresar al Inicio
                </a>
            </div>
            
            <div class="col-md-3">
                <button type="submit" class="btn btn-success btn-block">
                    <!--esto es para agregar un icono-->
                    <i class="fas fa-check"></i> Guardar Cliente
                </button>
            </div>
            
            <div class="col-md-3">
                <a href="${pageContext.request.contextPath}/ServletControlador?accion=eliminar&idCliente=${cliente.idCliente}"
                   class="btn btn-danger btn-block">
                    <i class="fas fa-trash"></i> Eliminar Cliente
                </a>
            </div>
        </div>
    </div>
</section>