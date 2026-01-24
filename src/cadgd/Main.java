/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadgd;

import pojosgd.*;
import cadgd.CADGD;

/**
 *
 * @author DAM203
 */
public class Main 
{
    public static void main(String[] args) 
    {
        Usuario u = new Usuario();
        u.setUsuarioIdUsuario(2);
        Arma a = new Arma();
        a.setArmaId(1);
        Armadura ar = new Armadura();
        ar.setArmaduraId(1);
        Piso pi = new Piso();
        pi.setPisoId(1);
        
        Personaje p = new Personaje();
        p.setPersonajeIdUsuario(u); 
        p.setPersonajeNombre("Paco");
        p.setPersonajeVidaMaxima(30);
        p.setPersonajeDanoBase(33);
        p.setPersonajeDefensaBase(33);
        p.setPersonajeDinero(33);
        p.setPersonajeArma(a);
        p.setPersonajeArmadura(ar);
        p.setPersonajePiso(pi);

        Objeto o = new Objeto();
        o.setObjetoIdObjeto(3);
        o.setObjetoNombre("Daga");
        o.setObjetoPrecio(5);
        o.setObjetoUsos(4);
       
        Personajeobjeto po = new Personajeobjeto();
        po.setPersonajeObjetoIdUsuario(p);
        po.setPersonajeObjetoIdObjeto(o);
        po.setPersonajeObjetoUsosRestantes(5);
        
        
        
        try {
            CADGD cad = new CADGD();

//            System.out.println(cad.leerHabilidad(1));
            System.out.println(cad.ModificarPersonaje(1, p));
//            System.out.println(cad.insertarPersonajeObjeto(po));
            
            
            
            
        } catch (ExcepcionGD ex) {
            System.out.println(ex);
        }
    }
}
