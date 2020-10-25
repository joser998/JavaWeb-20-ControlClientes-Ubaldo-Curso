package web;

import datos.ClienteDaoJDBC;
import dominio.Cliente;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

//Lo que hace este Servlet es recuperar el listado de clientes, compartir esta informacion hacia 
//otro JSP que se va a llamar clientes.jsp
@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        /*Para ser capaces de visualizar la lista de los clientes en el front*/
        
        String accion = request.getParameter("accion");
        
        //si es diferente de nulo significa que se mando algo
        if(accion != null){
            //abrimos un switch dependiendo lo que pase seleccionamos una opcion
            switch(accion){
                case "editar":
                    //si el switch cai aqui, mandamos a llamar el metodo insertarCliente y 
                    //proporcionamos los objetos para poder acceder a la informacion del metodo
                    this.editarCliente(request, response);
                break;
                case "eliminar":
                        this.eliminarCliente(request, response);
                break;
                default:
                    //en caso de no entrar en ninguna otra opcion dentro del switch
                    //mandamos a llamar este metodo que viene siendo lo mismo del metodo doGet
                    //osea que solo nos muestre la pagina como lo hace el metodo doGet al principio
                    //de la aplicacion
                    this.accionDefault(request, response);
            }
        }else{
            //en caso no entre tampoco la condicion del if, entonces que haga lo mismo que el metodo
            //doGet osea el mismo codigo de este metodo invocado
            this.accionDefault(request, response);
        }
        /* ************************************************************************************* */
    }
    
    
    
    //Creamos funcion que va a recibir el tipo de clientes
    //mandamos la lista con los datos de los clientes al request que esta mas arriba
    private double calcularSaldoTotal(List <Cliente> clientes){
        double saldoTotal = 0;
        //recorremos cada uno de los elementos de nuestro listado
        for(Cliente cliente: clientes){
            saldoTotal += cliente.getSaldo();
        }
        
        return saldoTotal;
    }
    
    
    
    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Recuperamos el valor del idCliente para posteriormente eliminar este registro
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        
        Cliente cliente = new Cliente(idCliente);
        
        //Eliminamos el objeto de la base de datos
        int registrosModificados = new ClienteDaoJDBC().eliminar(cliente);
        System.out.println("registrosModificados = "+registrosModificados);
        
        //redirigimos hacia la accion por default, nos retorna a lo que hace el metodo Get en un inicio
        this.accionDefault(request, response);
    }
    
    
    
    
    
    
    //Este metodo es para poder seleccionar la opcion de editar en cada registro del cliente,
    //este icono ya esta hecho en Bootstrap
    private void editarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Cada vez que presionamos el boton editar nos retorna un id del cliente guardado
        //este id es el que debemos recuperar (aqui) y tratar para hacer la edicion
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        
        //utilizamos la capa de datos
        //creamos el objeto cliente - new Cliente(idCliente)
        //le pasamos como parametro al metodo encontrar - encontrar(new Cliente(idCliente))
        //este mismo metodo encontrar(new Cliente(idCliente)) - ejecuta el Query para encontrar el 
        //objeto cliente completo
        Cliente cliente = new ClienteDaoJDBC().encontrar(new Cliente(idCliente));
        //el valor de cliente en este caso si lo pones en este alcance, en el de request
        request.setAttribute("cliente", cliente);
        
        //definimos la pagina JSP que vamos a utilizar para hacer la modificacion
        //esta pagina lo que hara es desplegar la informacion del cliente seleccionado
        //para poder hacer la edicion ya con el objeto cliente recuperado
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        //para direccionarnos
        request.getRequestDispatcher(jspEditar).forward(request, response);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        
        //recuperamos nuestro parametro de accion
        String accion = request.getParameter("accion");
        
        //si es diferente de nulo significa que se mando algo
        if(accion != null){
            //abrimos un switch dependiendo lo que pase seleccionamos una opcion
            switch(accion){
                case "insertar":
                    //si el switch cai aqui, mandamos a llamar el metodo insertarCliente y 
                    //proporcionamos los objetos para poder acceder a la informacion del metodo
                    this.insertarCliente(request, response);
                break;
                
                case "modificar":
                    this.modificarCliente(request, response);
                break;
                default:
                    //en caso de no entrar en ninguna otra opcion dentro del switch
                    //mandamos a llamar este metodo que viene siendo lo mismo del metodo doGet
                    //osea que solo nos muestre la pagina como lo hace el metodo doGet al principio
                    //de la aplicacion
                    this.accionDefault(request, response);
            }
        }else{
            //en caso no entre tampoco la condicion del if, entonces que haga lo mismo que el metodo
            //doGet osea el mismo codigo de este metodo invocado
            this.accionDefault(request, response);
        }
    }
    
    
    
    //Definimos metodo insertarCliente para que se pueda llevar acabo la accion del switch 
    //del primer case insertar, y le pasamos las mismas exepciones y los mismos parametros 
    //del metodo doPost
    private void insertarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Recuperamos los valores del formulario agregarCliente:
        
        //en este caso el valor del getParameter es el mismo nombre que le pusimos a la variables
        //dentro del formulario de Bootstrap
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        //el metodo getParameter nos regresa un String por lo tanto no le podemos asignar un valor
        //de tipo String
        double saldo = 0; //le asignamos el valor de 0 suponiendo no se le halla asignado ningun valor
        String saldoString = request.getParameter("saldo");//recibimos el valor del formulario como String
        
        //Y hacemos una condicion para esta variable saldo
        //Si saldoString NO es diferente de null y ademas NO es igual a la cadena vacia, 
        //entonces hacemos la conversion del valor que estamos recibiendo
        if(saldoString != null && !"".equals(saldoString)){
            saldo = Double.parseDouble(saldoString); //Convertimos el valor del formulario
        }
        
        //Creamos el objeto de cliente con los datos que hemos recibido, 
        //La clase Cliente hace las veces de el "modelo" dentro de MVC
        //Ponemos todos nuestros valores dentro del constructor excepto el idCliente, pues esta se generara automaticamnete
        Cliente cliente = new Cliente(nombre, apellido, email, telefono, saldo);
        
        //Una vez construimos este objeto (el construido arriba), lo insertaremos dentro de la base de datos
        //para esto usamos la capa de datos
        //la variable registrosModificados es lo que nos va a regresar la llamada al metodo insertar
        //y creamos un nuevo objeto de tipo ClienteDaoJDBC
        //y de esta forma insertamos el nuevo objeto de tipo cliente
        int registrosModificados = new ClienteDaoJDBC().insertar(cliente);
        System.out.println("registrosModificados = "+registrosModificados);
        
        //redirigimos hacia la accion por default, nos retorna a lo que hace el metodo Get en un inicio
        this.accionDefault(request, response);
    }
    
    
    
    
    private void modificarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Recuperamos los valores del formulario agregarCliente:
        //en este caso si necesitamos recuperar el id del cliente pues con este se hara la modificacion
        //para la DB
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        
        //en este caso el valor del getParameter es el mismo nombre que le pusimos a la variables
        //dentro del formulario de Bootstrap
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        //el metodo getParameter nos regresa un String por lo tanto no le podemos asignar un valor
        //de tipo String
        double saldo = 0; //le asignamos el valor de 0 suponiendo no se le halla asignado ningun valor
        String saldoString = request.getParameter("saldo");//recibimos el valor del formulario como String
        
        //Y hacemos una condicion para esta variable saldo
        //Si saldoString NO es diferente de null y ademas NO es igual a la cadena vacia, 
        //entonces hacemos la conversion del valor que estamos recibiendo
        if(saldoString != null && !"".equals(saldoString)){
            saldo = Double.parseDouble(saldoString); //Convertimos el valor del formulario
        }
        
        //Creamos el objeto de cliente con los datos que hemos recibido, 
        //La clase Cliente hace las veces de el "modelo" dentro de MVC
        //Ponemos todos nuestros valores dentro del constructor excepto el idCliente, pues esta se generara automaticamnete
        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
        
        //Una vez construimos este objeto (el construido arriba), lo insertaremos dentro de la base de datos
        //para esto usamos la capa de datos
        //la variable registrosModificados es lo que nos va a regresar la llamada al metodo insertar
        //y creamos un nuevo objeto de tipo ClienteDaoJDBC
        //y de esta forma insertamos el nuevo objeto de tipo cliente
        //Modificamos objeto en la Base de Datos
        int registrosModificados = new ClienteDaoJDBC().actualizar(cliente);
        System.out.println("registrosModificados = "+registrosModificados);
        
        //redirigimos hacia la accion por default, nos retorna a lo que hace el metodo Get en un inicio
        this.accionDefault(request, response);
    }
    
    
    
    
    //este metodo es para la opcion default dentro del switch, en caso de no caer en ninguna 
    //entra a esta opcion
    private void accionDefault(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Reutilizacion de codigo del metodo doGet
        List<Cliente> clientes = new ClienteDaoJDBC().listar();
        System.out.println("clientes = " + clientes);
        //aqui usamos este alcance HttpSession ya que es mas durarero que el request, por lo tanto
        //se estara mostrando nuestra informacion por mas tiempo y no se perdera este alcance
        HttpSession sesion = request.getSession();
        sesion.setAttribute("clientes", clientes);
        sesion.setAttribute("totalClientes", clientes.size());
        sesion.setAttribute("saldoTotal", this.calcularSaldoTotal(clientes));
        //request.getRequestDispatcher("clientes.jsp").forward(request, response);
        //esta accion es para que una vez terminada la instruccion de insert etc, nos diriga
        //de nuevo a esta pag de clientes.jsp para que si el usuario desea recargar la pagina
        //no se agregue de nuevo el registro que ya habiamos pedido se insertara.
        response.sendRedirect("clientes.jsp");
    }
}