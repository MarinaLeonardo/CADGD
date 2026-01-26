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
            this.conexion = DriverManager.getConnection("jdbc:oracle:thin:@172.16.210.1:1521:TEST", "GODSBATTLE", "kk");
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
    
    //Habilidades 
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
            exHR.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            throw exHR;
        }

        return habilidad;  
    }

    
    //Personaje
    public Integer ModificarPersonaje (Integer idPersonaje, Personaje personaje) throws ExcepcionGD{
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
            sentenciaPreparada.setObject(9, idPersonaje, java.sql.Types.INTEGER);
            
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
                    e.setMensajeErrorUsuario("La vida maxima, el daño base, la defensa base y el dinero no pueden ser negativos");
                    break;
                case 2291:
                    e.setMensajeErrorUsuario("El usuario, el arma, la armadura o el piso no existen.");
                    break;
                case 1407:
                    e.setMensajeErrorUsuario("Todos los campos son obligatorios excepto el arma y la armadura");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el admiinistrador.");
            }
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
            ######- Delete: PersonajeHabilidad
            ######- Select: Enemigo
            ######- Update: Usuario   
            ######- Insert: Personaje
     */
    
    
    //Select de Enemigo:
    
    public Enemigo leerEnemigo(Integer idEnemigo) throws ExcepcionGD {
    conectarBD();
    Enemigo enemigo = null;

    String dml = "select * from ENEMIGO where ID_ENEMIGO = " + idEnemigo;

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dml);

            if (resultado.next()) {
                enemigo = new Enemigo();
                enemigo.setEnemigoIdEnemigo(resultado.getInt("ID_ENEMIGO"));
                enemigo.setEnemigoNombre(resultado.getString("NOMBRE"));
                enemigo.setEnemigoVidaMaxima(resultado.getInt("VIDA_MAXIMA"));
                enemigo.setEnemigoDanoBase(resultado.getInt("DANO_BASE"));
                enemigo.setEnemigoDefensaBase(resultado.getInt("DEFENSA_BASE"));
                enemigo.setEnemigoDinero(resultado.getInt("DINERO"));
            }

            resultado.close();
            sentencia.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionGD exHR = new ExcepcionGD();
            exHR.setCodigoErrorBD(ex.getErrorCode());
            exHR.setMensajeErrorBD(ex.getMessage());
            exHR.setSentenciaSQL(dml);
            exHR.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            throw exHR;
        }

        return enemigo;
    }
    
    //Insert de Personaje:
    
    public void insertarPersonaje(Personaje personaje) throws ExcepcionGD {
    conectarBD();

    String dml = "INSERT INTO PERSONAJE "
            + "(ID_USUARIO, NOMBRE, VIDA_MAXIMA, DANO_BASE, DEFENSA_BASE, DINERO, ID_ARMA, ID_ARMADURA, ID_PISO) "
            + "VALUES ("
            + personaje.getPersonajeIdUsuario().getUsuarioIdUsuario() + ", '"
            + personaje.getPersonajeNombre() + "', "
            + personaje.getPersonajeVidaMaxima() + ", "
            + personaje.getPersonajeDanoBase() + ", "
            + personaje.getPersonajeDefensaBase() + ", "
            + personaje.getPersonajeDinero() + ", "
            + personaje.getPersonajeArma().getArmaId() + ", "
            + personaje.getPersonajeArmadura().getArmaduraId() + ", "
            + personaje.getPersonajePiso().getPisoId()
            + ")";

        try {
            Statement sentencia = conexion.createStatement();
            sentencia.executeUpdate(dml);

            sentencia.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionGD exHR = new ExcepcionGD();
            exHR.setCodigoErrorBD(ex.getErrorCode());
            exHR.setMensajeErrorBD(ex.getMessage());
            exHR.setSentenciaSQL(dml);
            exHR.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            throw exHR;
        }
    }
    
    
    //Update de Usuario:
    
    public Integer ModificarUsuario(Integer idUsuario, Usuario usuario) throws ExcepcionGD {
    conectarBD();
    int registrosAfectados = 0;

    String dml = "UPDATE USUARIO SET NOMBRE = ?, CORREO = ?, CONTRASENA = ?, ID_ROL = ? "
               + "WHERE ID_USUARIO = ?";

        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setString(1, usuario.getUsuarioNombre());
            sentenciaPreparada.setString(2, usuario.getUsuarioCorreo());
            sentenciaPreparada.setString(3, usuario.getUsuarioContrasena());
            sentenciaPreparada.setObject(4, usuario.getUsuarioIdRol().getRolIdRol(), java.sql.Types.INTEGER);
            sentenciaPreparada.setObject(5, idUsuario, java.sql.Types.INTEGER);

            registrosAfectados = sentenciaPreparada.executeUpdate();

            sentenciaPreparada.close();
            conexion.close();

        } catch (SQLException ex) {

            ExcepcionGD e = new ExcepcionGD();
            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);

            switch (ex.getErrorCode()) {
                case 1:
                    e.setMensajeErrorUsuario("El correo ya está registrado en el sistema.");
                    break;
                case 2291:
                    e.setMensajeErrorUsuario("El rol asignado no existe.");
                    break;
                case 1407:
                    e.setMensajeErrorUsuario("Todos los campos son obligatorios.");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }

        return registrosAfectados;
    }

    
    //Delete de PersonajeHabilidad:
    
    public Integer eliminarPersonajeHabilidad(Personajehabilidad ph) throws ExcepcionGD {
    conectarBD();
    int registrosAfectados = 0;

    String dml = "DELETE FROM PERSONAJEHABILIDAD WHERE ID_USUARIO = ? AND ID_HABILIDAD = ?";

        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            // Usamos los getters exactos de tus POJOs
            sentenciaPreparada.setObject(1, ph.getPersonajehabilidadIdUsuario().getPersonajeIdUsuario(), java.sql.Types.INTEGER);
            sentenciaPreparada.setObject(2, ph.getPersonajehabilidadIdHabilidad().getHabilidadId(), java.sql.Types.INTEGER);

            registrosAfectados = sentenciaPreparada.executeUpdate();

            sentenciaPreparada.close();
            conexion.close();

        } catch (SQLException ex) {

            ExcepcionGD e = new ExcepcionGD();
            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);

            switch (ex.getErrorCode()) {
                case 2291:
                    e.setMensajeErrorUsuario("El personaje o la habilidad no existen.");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
            }

            throw e;
        }

        return registrosAfectados;
    }

    

    

    
    
}
