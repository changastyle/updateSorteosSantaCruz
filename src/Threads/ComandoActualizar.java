
package Threads;

import model.RespuestaSincronizacion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Listaip;

public class ComandoActualizar extends Thread
{
    private Listaip listaip;
    private int tiempoEsperaParaSaberSiSincronizo = 10 * 1000;
    private String comandoSincronizacion = "";
    private String comandoConsulta = "";
    public ComandoActualizar(Listaip listaip)
    {
        this.listaip = listaip;
        comandoSincronizacion = "/usr/bin/smartview -i " + listaip.getId().getIp() + " -b0 -c syncSorteos";
        comandoConsulta = "/usr/bin/smartview -i " +  listaip.getId().getIp()  +  " -b0 -c statusComandos";
    }
    @Override
    public void run()
    {
        ArrayList<String> arrayDeLineasDeRespuestaAlaSincronizacion = new ArrayList<String>();
        
        try
        {
            Process procSincro = Runtime.getRuntime().exec(comandoSincronizacion);
            
            Thread.sleep(tiempoEsperaParaSaberSiSincronizo);
            
            Process procConsulta = Runtime.getRuntime().exec(comandoConsulta);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(procConsulta.getInputStream()));

            System.out.println("RESPUESTA DE LA SINCRO:");
            String line = "";
            
            int cantidadLineas = 0;
            while((line = reader.readLine()) != null) 
            {
                arrayDeLineasDeRespuestaAlaSincronizacion.add(line);
                cantidadLineas++;
                System.out.print(line + " | fin linea.\n");
            }
            
            if(cantidadLineas > 1)
            {
               ArrayList<RespuestaSincronizacion> arrDeRespuestasAlaSincronizacion = procesarRespuestaSincronizacion(arrayDeLineasDeRespuestaAlaSincronizacion);
            
                for( RespuestaSincronizacion respuestaSincronizacion :arrDeRespuestasAlaSincronizacion)
                {
                    //System.out.println("" + respuestaSincronizacion.toString());

                    if(respuestaSincronizacion.getAtributoS().equalsIgnoreCase("SSor"))
                    {
                        if(respuestaSincronizacion.getEstado()=='O')
                        {
                            System.out.println("SINCRONIZO OK : " + respuestaSincronizacion.getDireccionIP());
                            this.listaip.setEstado('O');
                        }
                        else
                        {
                            System.out.println("NO SINCRONIZO:  " + respuestaSincronizacion.getDireccionIP());
                            this.listaip.setEstado('E');
                        }
                        
                    }
                } 
            }
            else
            {
                this.listaip.setEstado('E');
            }
            System.out.println("VOY A GRABAR:" + this.listaip.toString());
            daos.ListaipsDAO.update(this.listaip);
            
        } 
        catch (IOException ex)
        {
            System.out.println("ERROR: " + listaip.getId().getIp() + " IOEXCEPTION. (NO ENCONTRO EL COMANDO): " +  comandoSincronizacion);
        } 
        catch (InterruptedException ex)
        {
            System.out.println("ERROR: " + listaip.getId().getIp() + "  NO pudo esperar para saber si logro sincronizar, el proceso fue interrumpido");
        } 
    }

    private ArrayList<RespuestaSincronizacion> procesarRespuestaSincronizacion(ArrayList<String> arrayDeLineasDeRespuestaAlaSincronizacion)
    {
        ArrayList<RespuestaSincronizacion> arrRespuestas = new ArrayList<RespuestaSincronizacion> ();
        
        boolean primeraLinea = true;
        for(String lineaActual : arrayDeLineasDeRespuestaAlaSincronizacion)
        {
           String acumulador = "";
           boolean checkpointUNO = false;
           boolean checkpointDOS = false;
           
           if(!primeraLinea)
           {
                RespuestaSincronizacion respuestaSincronizacionAUX = new RespuestaSincronizacion();
                for(int i = 0; i < lineaActual.length() ; i++)
                {
                    //System.out.println("acumulador = " + acumulador);
                    
                    if(lineaActual.charAt(i) == ':' || i == (lineaActual.length() - 1) )
                    {
                        //System.out.println("Encontre = ");
                        if(!checkpointUNO)
                        {
                            respuestaSincronizacionAUX.setDireccionIP(acumulador);
                            //System.out.println("Checkpoint 1 = " +  respuestaSincronizacionAUX.toString() + " -> " + acumulador);
                            acumulador = "";
                            checkpointUNO = true;
                        }
                        else if(!checkpointDOS)
                        {
                            respuestaSincronizacionAUX.setAtributoS(acumulador);
                            //System.out.println("Checkpoint 2 = " +  respuestaSincronizacionAUX.toString() + " -> " + acumulador);
                            acumulador = "";
                            checkpointDOS = true;
                        }
                        else if(i == (lineaActual.length()-1))
                        {
                            
                            respuestaSincronizacionAUX.setEstado(lineaActual.charAt(i));
                            //System.out.println("Checkpoint 3 = " +  respuestaSincronizacionAUX.toString() + " -> " + acumulador);
                            arrRespuestas.add(respuestaSincronizacionAUX);
                            
                            //System.out.println("Agrege: " + respuestaSincronizacionAUX.toString());
                        }
                    }
                    else
                    {
                        acumulador += lineaActual.charAt(i);
                    }
                } 
           }
           else
           {
               primeraLinea = false;
           }
        }
        
        return arrRespuestas;
    }

    @Override
    public String toString()
    {
        return "ComandoActualizar{" + "listaip=" + listaip + ", tiempoEsperaParaSaberSiSincronizo=" + tiempoEsperaParaSaberSiSincronizo + ", comandoSincronizacion=" + comandoSincronizacion + ", comandoConsulta=" + comandoConsulta + '}';
    }
    
}
