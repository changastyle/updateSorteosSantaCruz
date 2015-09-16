package controller;

import Threads.ComandoActualizar;
import daos.AbstractDAO;
import daos.UltimoSorteoDAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import model.*;

public class Controller
{
    //<editor-fold desc="Parametros">
    public static final String URLWebService = "http://scapp1:8080/saaServices/services/smarttv/sorteos";
    public static final int tiempoDeLoop = 1000 * 1 * 60;
    public static final int tiempoRecargarWScasoError = 5 * 60;
    public static final String carpetaDeLosSorteos = "carpetitaDeLosSorteos";
    public static boolean flagActualizarClientes;
    
    private static ArrayList<Instalaciones> arrDeInstalaciones;
    private static ArrayList<Listaip> arrDeListaips;
    private static ArrayList<Ultimosorteo> arrDeUltimosSorteos;
    private static ArrayList<Sorteo> arrSorteosFromWS;
    //</editor-fold>
    
    public static ArrayList<Sorteo> sorteosFromWebService()
    {
        ArrayList<Sorteo> arrDeSorteos = new ArrayList<Sorteo>();
        URL url = null;  
        try
        {
            url = new URL(URLWebService);
            if(url != null)
            {
                //1.Abro un lector de archivos del Stream del WS:
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                //2.Leo el archivo HTML:
                String str = in.readLine();
                
                //Muestro el contenido del archivo:
                /*int contador = 0;
                System.out.println(contador +" | " + str );*/
                
                //3.Parseo el contenido del Archivo en formato JSON a objetos Java Representativos(model.Sorteo):
                JSONArray array = (JSONArray) JSONValue.parse(str);
                arrDeSorteos = mapeoSorteosDB(array);
            }
        } 
        catch (MalformedURLException ex)
        {
            ex.printStackTrace();
            System.out.println("ERROR: Leyendo la URL del webservice:" + URLWebService );
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.out.println("ERROR: Abriendo el Input Stream de:" + url.toString() );
        }

        return arrDeSorteos;
    }
    private static ArrayList<model.Sorteo> mapeoSorteosDB(JSONArray arrayRecibido)
    {
        ArrayList<model.Sorteo> arrDeSorteos = new ArrayList<model.Sorteo>();
        
        for(int i = 0 ; i < (arrayRecibido.size() ); i++)
        {
            JSONObject objetoJSONProvisorio = (JSONObject) arrayRecibido.get(i);
            model.Sorteo sorteoAux =  new Sorteo();
            sorteoAux.setFileName( (char) objetoJSONProvisorio.get("fileName").toString().charAt(0) );
            sorteoAux.setcSorte(Integer.parseInt(String.valueOf(objetoJSONProvisorio.get("c_sorte"))));
            sorteoAux.setcJuego(String.valueOf(objetoJSONProvisorio.get("c_juego")));
            sorteoAux.setfSorte(String.valueOf(objetoJSONProvisorio.get("f_sorte")));
            sorteoAux.setServj(String.valueOf(objetoJSONProvisorio.get("t_servj")));
            arrDeSorteos.add(sorteoAux);
            //System.out.println("sorteoAUX= "+ sorteoAux.toString());
            //System.out.println("JSON OBJECT#"+i + ": " + objetoJSONProvisorio.toString());
        }
        
        return arrDeSorteos;
    }
    public static String imprimirArrayDeSorteos(ArrayList<Sorteo>  arrAImprimir)
    {
        String salida = "";
        
        for(Sorteo sorteo : arrAImprimir)
        {
            salida += "    " + sorteo.toString() + "\n";
        }
        
        return salida;
    }
    public static ArrayList<model.Ultimosorteo> findAllUltimosSorteos()
    {
        return daos.UltimoSorteoDAO.findAllUltimosSorteos();
    }
    public static ArrayList<model.Instalaciones> findAllInstalaciones()
    {
        return daos.InstalacionesDAO.findAllInstalaciones();
    }

    public static void pruebas()
    {
                        
        Controller.arrDeInstalaciones = imprimirInstalaciones();
        Controller.arrDeUltimosSorteos = imprimirSorteosFromDB();
        Controller.arrSorteosFromWS = imprimirSorteos();
        
        for(Sorteo sorteoWS : arrSorteosFromWS)
        {
            if( !existeSorteoEnDB(sorteoWS) )
            {
                creoSorteo(sorteoWS);
                if(traerZIP(sorteoWS))
                {
                    flagActualizarClientes = true;
                }
            }
            else if( existeSorteoEnDB(sorteoWS) && sorteoEstaDesactualizado(sorteoWS) )
            {
                actualizoSorteo(sorteoWS);
                if(traerZIP(sorteoWS))
                {
                    flagActualizarClientes = true;
                }
            }
            else
            {
                //ESTA TODO ACTUALIZADO y EXISTEN TODOS LOS JUEGOS.
            }
        }
    }
    public static void hacer()
    {
        flagActualizarClientes = false;
        int vueltas = 0;
        while(true)
        {
            try
            {
                vueltas++;
                
                Controller.arrDeInstalaciones = imprimirInstalaciones();
                Controller.arrDeUltimosSorteos = imprimirSorteosFromDB();
                Controller.arrSorteosFromWS = imprimirSorteos();
                
                
               
                for(Sorteo sorteoWS : arrSorteosFromWS)
                {
                    if( !existeSorteoEnDB(sorteoWS) )
                    {
                        if(traerZIP(sorteoWS))
                        {
                            creoSorteo(sorteoWS);
                            flagActualizarClientes = true;
                        }
                    }
                    else if( existeSorteoEnDB(sorteoWS) && sorteoEstaDesactualizado(sorteoWS) )
                    { 
                        if(traerZIP(sorteoWS))
                        {
                            flagActualizarClientes = true;
                            actualizoSorteo(sorteoWS);
                        }
                    }
                    else
                    {
                        //ESTA TODO ACTUALIZADO y EXISTEN TODOS LOS JUEGOS.
                    }
                }
                //EN EL CASO DE QUE HALLA QUE ACTUALIZAR, ENTONCES PONGO TODA LA LISTA DE IP COMO DESACTUALIZADA:
                if(flagActualizarClientes)
                {
                    for (Listaip listaip: findAllListaIPS())
                    {
                        listaip.setEstado('E');
                        daos.ListaipsDAO.update(listaip);
                    }
                }
                sincro();
                
                System.out.println("|---------------------------------- Vuelta: " + vueltas +" ----------------------------------|");
                Thread.sleep(tiempoDeLoop);
                //break;      //Provisorio:
            } 
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("ERROR: THREAD hacer clase Controller");
            }
        }
    }
    private static ArrayList<Listaip> findAllListaIPS()
    {
        return daos.ListaipsDAO.findAllListaIps();
    }
    private static ArrayList<Instalaciones> imprimirInstalaciones()
    {
        ArrayList<Instalaciones> arr = controller.Controller.findAllInstalaciones();
         
        System.out.println("INSTALACIONES:");
        
        for(model.Instalaciones instalacion : arr)
        {
            System.out.println("    " + instalacion.toString());
        }
        
        return arr;
    }
    private static  ArrayList<Sorteo> imprimirSorteos()
    {
        ArrayList<Sorteo> arr = controller.Controller.sorteosFromWebService();
        
        System.out.println("Sorteos FROM WS:");
        System.out.println( controller.Controller.imprimirArrayDeSorteos(arr));
        
        return arr;
        
    }
    public static ArrayList<Ultimosorteo> imprimirSorteosFromDB()
    {
        ArrayList<Ultimosorteo> arr = controller.Controller.findAllUltimosSorteos();
        System.out.println("ULTIMOS SORTEOS DB:");
        for(model.Ultimosorteo ultimoSorteo : arr)
        {
            System.out.println("    " + ultimoSorteo.toString());
        }
        return arr;
    }
    public static ArrayList<Listaip> imprimirListaIps()
    {
        ArrayList<Listaip> arr = controller.Controller.findAllListaIPS();
         
        //System.out.println("INSTALACIONES:");
        
        for(model.Listaip listaip : arr)
        {
            System.out.println("    " + listaip.toString());
        }
        return arr;
    }
    private static boolean existeSorteoEnDB(Sorteo sorteoWS)
    {
        boolean respuesta = false;
        
        for(Ultimosorteo sorteoDB: arrDeUltimosSorteos)
        {
            if(sorteoDB.getId().getCJuego() == Integer.parseInt(sorteoWS.getcJuego()))
            {
                if(sorteoDB.getId().getDName().charAt(0) == sorteoWS.getFileName())
                {
                    respuesta = true;
                }
            }
        }
        
        return respuesta;
    }

    private static void creoSorteo(Sorteo sorteoWS)
    {   
        Ultimosorteo ultimosorteo = new Ultimosorteo();
        ultimosorteo.setId(new UltimosorteoId(1, Integer.parseInt(sorteoWS.getcJuego()), String.valueOf(sorteoWS.getFileName()) ));
        ultimosorteo.setCSorte(sorteoWS.getcSorte());
        
        Timestamp stamp = new Timestamp( Long.parseLong(sorteoWS.getfSorte()));
        Date date = new Date(stamp.getTime());
        ultimosorteo.setFSorte(date);
        
        System.out.println("CREO SORTEO: " + ultimosorteo.toString());
        UltimoSorteoDAO.saveUltimoSorteo(ultimosorteo);
    }

    private static void actualizoSorteo(Sorteo sorteoWS)
    {
        
        
        Ultimosorteo ultimosorteo = new Ultimosorteo();
        ultimosorteo.setId(new UltimosorteoId(1, Integer.parseInt(sorteoWS.getcJuego()), String.valueOf(sorteoWS.getFileName()) ));
        ultimosorteo.setCSorte(sorteoWS.getcSorte());
        
        
        
        Timestamp stamp = new Timestamp( Long.parseLong(sorteoWS.getfSorte()));
        Date date = new Date(stamp.getTime());
        ultimosorteo.setFSorte(date);        
        
        System.out.println("ACTUALIZO SORTEO: " + ultimosorteo.toString());
        
        UltimoSorteoDAO.updateUltimoSorteo(ultimosorteo);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static boolean sorteoEstaDesactualizado(Sorteo sorteoWS)
    {
        boolean respuesta = false;
       
        if(existeSorteoEnDB(sorteoWS))
        {
            for(Ultimosorteo sorteoDB : arrDeUltimosSorteos)
            {
                if(sorteoDB.getId().getDName().charAt(0) == sorteoWS.getFileName())
                {
                    if(sorteoDB.getId().getCJuego() == Integer.parseInt(sorteoWS.getcJuego()))
                    {
                        if(sorteoDB.getCSorte() != sorteoWS.getcSorte())
                        {
                            respuesta = true;
                        }
                    }
                }
            }
        }
        
        
        return respuesta;  
    }

    private static boolean traerZIP(Sorteo sorteoWS)
    {
        boolean ok = false;
        try
        {
            URL urlJuego = new URL("http://crisdian:8080/saaServices/services/smarttv/extractos/" + sorteoWS.getcSorte());
            System.out.println("URL = " + urlJuego);
            ReadableByteChannel rbc = Channels.newChannel(urlJuego.openStream());
            FileOutputStream fos = new FileOutputStream(carpetaDeLosSorteos + "/" + sorteoWS.getcSorte() + ".zip");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            
            ok = true;
        }
        catch(MalformedURLException e)
        {
            System.out.println("ERROR: No se pudo leer la URL: " + e.toString() );
        }
        catch(FileNotFoundException e)
        {
            System.out.println("ERROR: No se encontró Archivo :" + e.toString() );
        }
        catch(IOException e)
        {
            System.out.println("ERROR: No se pudo leer: " + e.toString());
        }
        return ok;
    }
    
    public static boolean unzip(String archivo) throws IOException
    {
        boolean ok = false;
        byte[] buffer = new byte[2048];
        String zipFile = "";

        try
        {
            FileInputStream fInput = new FileInputStream(archivo);
             
            ZipInputStream zipInput = new ZipInputStream(fInput);
             
            ZipEntry entry = zipInput.getNextEntry();
            
            while(entry != null)
            {
                File file = new File("carpetaDescompresion/" + entry.getName());
                
                FileOutputStream fOutput = new FileOutputStream(file);
                int count = 0;
                
                while ((count = zipInput.read(buffer)) > 0)
                {
                    fOutput.write(buffer, 0, count);
                }
                
                fOutput.close();
                zipInput.closeEntry();
                entry = zipInput.getNextEntry();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return ok;
    }
    private static void sincro()
    {
        /* A MODO DE PRUEBA; LUEGO QUITAR:*/flagActualizarClientes = true;
        System.out.println("FLAG ACTUALIZAR CLIENTES: " + flagActualizarClientes);
        if(flagActualizarClientes)
        {
            System.out.println("ENTRE A SYNCRONIZAR:");
            for(Listaip ip : controller.Controller.findAllListaIPS())
            {
                //
                if(ip.getEstado() == 'E')
                {
                                   
                    System.out.println("" + ip.toString());

                    ComandoActualizar comandoActualizar = new ComandoActualizar(ip);

                    comandoActualizar.start(); 
                }

            }
        }
    }
}
