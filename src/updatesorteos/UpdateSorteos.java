package updatesorteos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import model.Sorteos;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;


public class UpdateSorteos
{
    public final static String carpetaDeLosSorteos = "carpetitaDeLosSorteos";
    public static void main(String[] args) throws Exception
    {
        URL url = new URL("http://crisdian:8080/saaServices/services/smarttv/sorteos");  
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String str = in.readLine();
        int contador = 0;
        System.out.println(contador +" | " + str );
        
        JSONArray array = (JSONArray) JSONValue.parse(str);
        JSONObject objeto1 = (JSONObject) array.get(1);
        /*System.out.println("array.toString() = " + array.toString());
        System.out.println("array -> SIZE = " + array.size());
        
        System.out.println("objeto1.toString()= " + objeto1.toString());
        System.out.println("Propiedades delobjeto 1:");
        System.out.println("" + objeto1.get("c_sorte"));*/
        
        
        
        System.out.println("BAJADA DE ARCHIVOS:");
        
        for(model.Sorteos sorteo : mapeosDeSorteos(array))
        {
            URL website = new URL("http://crisdian:8080/saaServices/services/smarttv/extractos/" +  sorteo.getcSorte() );
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(carpetaDeLosSorteos + "/" + sorteo.getcSorte() + ".zip");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        

        
        /*URL url2 = new URL("http://crisdian:8080/saaServices/services/smarttv/extractos/7558");  
        BufferedReader bf = new BufferedReader(new InputStreamReader(url2.openStream()));
        
        String acc = "";
        String s;
        while(  ( s = bf.readLine()) != null)
        {
            System.out.println("" + s);
            acc += s;
        }
        File archi = new File("archi.zip");
        //FileInputStream archiFIS = new FileInputStream(archi);
        FileOutputStream archiFOS = new FileOutputStream(archi);
        
        byte [] b = acc.getBytes();
              
        archiFOS.write(b, 0, b.length);
        archiFOS.flush();*/
        //Unzip.unzip();
    }
    public static ArrayList<model.Sorteos> mapeosDeSorteos(JSONArray arrayRecibido)
    {
        ArrayList<model.Sorteos> arrDeSorteos = new ArrayList<model.Sorteos>();
        
        for(int i = 0 ; i < (arrayRecibido.size() ); i++)
        {
            JSONObject objetoJSONProvisorio = (JSONObject) arrayRecibido.get(i);
            model.Sorteos sorteoAux =  new Sorteos();
            sorteoAux.setFileName( (char) objetoJSONProvisorio.get("fileName").toString().charAt(0) );
            sorteoAux.setcSorte(String.valueOf(objetoJSONProvisorio.get("c_sorte")));
            sorteoAux.setcJuego(String.valueOf(objetoJSONProvisorio.get("c_juego")));
            sorteoAux.setfSorte(String.valueOf(objetoJSONProvisorio.get("f_sorte")));
            sorteoAux.setServj(String.valueOf(objetoJSONProvisorio.get("t_servj")));
            arrDeSorteos.add(sorteoAux);
            System.out.println("sorteoAUX= "+ sorteoAux.toString());
            //System.out.println("JSON OBJECT#"+i + ": " + objetoJSONProvisorio.toString());
        }
        
        return arrDeSorteos;
    }
    
}
