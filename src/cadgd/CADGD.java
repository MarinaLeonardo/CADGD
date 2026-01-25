/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadgd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jdk.internal.org.objectweb.asm.Type;
import pojosgd.*;

/**
 *
 * @author DAM203
 */
public class CADGD {

    private Connection conexion;

    public CADGD() throws ExcepcionGD {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException ex) {
            ExcepcionGD e = new ExcepcionGD();
            e.setMensajeErrorBD(ex.getMessage());
            e.setMensajeErrorUsuario("Error general del sistema. Consulta con el administrador");
            throw e;
        }
    }

    private void conectarBD() throws ExcepcionGD {
        try {
            //this.conexion = DriverManager.getConnection("jdbc:oracle:thin:@172.16.210.1:1521:TEST", "GODSBATTLE", "kk"); //--> CLASE
            this.conexion = DriverManager.getConnection("jdbc:oracle:thin:@192.168.18.210:1521:test", "GD", "kk"); //--> CASA
            
        } catch (SQLException ex) {
            ExcepcionGD e = new ExcepcionGD();
            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setMensajeErrorUsuario("Error general del sistema. Consulta con el administrador");
            throw e;
        }
    }

    /* Iván:
            - Delete: Personaje
            - Select: Objetos
            - Update: Personajeobjeto
            - Insert: Personjahabilidad
     */
    
    
    
 /* Marina:
            - Delete: Usuario
            - Select: Habilidades
            - Update: Personaje
            - Insert: Personajeobjeto
     */
    
    //Habilidades leer
    public Habilidad leerHabilidad (Integer idHabilidad) throws ExcepcionGD{
        conectarBD();
        Habilidad habilidad = null;

        String dml = "select * from HABILIDAD where ID_HABILIDAD = "+idHabilidad;

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dml);
            
            if (resultado.next()) {
                habilidad = new Habilidad();
                habilidad.setHabilidadNombre(resultado.getString("NOMBRE"));
                habilidad.setHabilidadDescripcion(resultado.getString("DESCRIPCION"));
                habilidad.setHabilidadPrecio(resultado.getInt("PRECIO"));       
            }

            resultado.close();
            sentencia.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionGD exHR = new ExcepcionGD();
            exHR.setCodigoErrorBD(ex.getErrorCode());
            exHR.setMensajeErrorBD(ex.getMessage());
            exHR.setSentenciaSQL(dml);
            exHR.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
            throw exHR;
        }

        return habilidad;  
    }

    
    //Personaje modificar
    public Integer ModificarPersonaje (Integer idUsuario, Personaje personaje) throws ExcepcionGD{
        conectarBD();
        int registrosAfectados = 0;

        String dml = "update PERSONAJE set NOMBRE = ?, VIDA_MAXIMA = ?, DANO_BASE = ?, DEFENSA_BASE = ?, "
                + "DINERO = ?, ID_ARMA = ?, ID_ARMADURA = ?, ID_PISO = ? where ID_USUARIO = ?";
          try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            
            sentenciaPreparada.setString(1, personaje.getPersonajeNombre());
            sentenciaPreparada.setObject(2, personaje.getPersonajeVidaMaxima(), java.sql.Types.INTEGER);
            sentenciaPreparada.setObject(3, personaje.getPersonajeDanoBase(), java.sql.Types.INTEGER);
            sentenciaPreparada.setObject(4, personaje.getPersonajeDefensaBase(), java.sql.Types.INTEGER);
            sentenciaPreparada.setObject(5, personaje.getPersonajeDinero(), java.sql.Types.INTEGER);
            sentenciaPreparada.setObject(6, personaje.getPersonajeArma().getArmaId(), java.sql.Types.INTEGER);
            sentenciaPreparada.setObject(7, personaje.getPersonajeArmadura().getArmaduraId(), java.sql.Types.INTEGER);
            sentenciaPreparada.setObject(8, personaje.getPersonajePiso().getPisoId(), java.sql.Types.INTEGER);
            sentenciaPreparada.setObject(9, idUsuario, java.sql.Types.INTEGER);
            
            registrosAfectados = sentenciaPreparada.executeUpdate();

            sentenciaPreparada.close();
            conexion.close();

        } catch (SQLException ex) {

            ExcepcionGD e = new ExcepcionGD();
            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);

            switch (ex.getErrorCode()) {
               case 2290:
                    e.setMensajeErrorUsuario("La vida maxima, el daño base, la defensa base y el dinero no pueden ser negativos.");
                    break;
                case 2291:
                    e.setMensajeErrorUsuario("El usuario, el arma, la armadura o el piso no existen.");
                    break;
                case 1407:
                    e.setMensajeErrorUsuario("Todos los campos son obligatorios excepto el arma y la armadura.");
                    break;
                case 1:
                    e.setMensajeErrorUsuario("Este usuario ya tiene vinculado el mismo objeto, no se permite tener los objetos duplicados.");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el admiinistrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    
    //Personajeobjeto insertar
    public Integer insertarPersonajeObjeto(Personajeobjeto personajeObjeto) throws ExcepcionGD {
        conectarBD();
        int registrosAfectados = 0;
        String dml = "insert into PERSONAJEOBJETO (ID_USUARIO, ID_OBJETO, USOS_RESTANTES) values (?,?,?)";

        try {
            PreparedStatement sentencia = conexion.prepareStatement(dml);

            sentencia.setObject(1, personajeObjeto.getPersonajeObjetoIdUsuario().getPersonajeIdUsuario().getUsuarioIdUsuario(), java.sql.Types.INTEGER);
            sentencia.setObject(2, personajeObjeto.getPersonajeObjetoIdObjeto().getObjetoIdObjeto(), java.sql.Types.INTEGER);
            sentencia.setObject(3, personajeObjeto.getPersonajeObjetoUsosRestantes(), java.sql.Types.INTEGER);
           
            registrosAfectados = sentencia.executeUpdate();

            sentencia.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionGD e = new ExcepcionGD();

            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);
            
            switch (ex.getErrorCode()) {
                case 2290: 
                    e.setMensajeErrorUsuario("Los usos restantes no pueden ser menores que 0.");
                    break;
                case 2291:
                    e.setMensajeErrorUsuario("El objeto o el usuario seleccionados no existen.");
                    break;
                    
                case 1407:
                    e.setMensajeErrorUsuario("Todos los campos son obligatorios.");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
                    break;
            }

            throw e;
        } catch (Exception e) {
            System.out.println(e);

        }

        return registrosAfectados;
    }
    
    //Persobaje Delete
    public Integer eliminarPersonaje(Integer idUsuario) throws ExcepcionGD {
        int registrosAfectados = 0;
        String dml = "";
        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            dml = "delete PERSONAJE where ID_USUARIO = " + idUsuario;
            registrosAfectados = sentencia.executeUpdate(dml);
            sentencia.close();
            conexion.close();
        } catch (SQLException ex) {

            ExcepcionGD e = new ExcepcionGD();

            switch (ex.getErrorCode()) {
                case 2292:
                    e.setMensajeErrorUsuario("No se puede eliminar porque tiene objetos o habilidaddes asociados"); //QUITAR CUANDO PONGAMOS EL ON CASCADE
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
                    break;
            }

            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);

            throw e;
        }

        return registrosAfectados;
    }

    
    

    /* Kevin
            - Delete: Personajeobjeto
            - Select: Personaje
            - Update: Configuracion
            - Insert: Usuario
     */
    
    
    
 /* Simón
            - Delete: PersonajeHabilidad
            - Select: Enemigo
            - Update: Usuario   
            - Insert: Personaje
     */
}
