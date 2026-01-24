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
        Arma a = new Arma();
        a.setArmaId(1);
        Armadura ar = new Armadura();
        ar.setArmaduraId(1);
        Piso pi = new Piso();
        pi.setPisoId(1);
        
        Personaje p = new Personaje();
        p.setPersonajeIdUsuario(u);
        p.setPersonajeNombre("Roberto");
        p.setPersonajeVidaMaxima(30);
        p.setPersonajeDanoBase(33);
        p.setPersonajeDefensaBase(33);
        p.setPersonajeDinero(33);
        p.setPersonajeArma(a);
        p.setPersonajeArmadura(ar);
        p.setPersonajePiso(pi);
       
        
        
        
        
        
        try {
            CADGD cad = new CADGD();

            System.out.println(cad.leerHabilidad(1));
            System.out.println(cad.ModificarPersonaje(1, p));
            
        } catch (ExcepcionGD ex) {
            System.out.println(ex);
        }
    }
}
