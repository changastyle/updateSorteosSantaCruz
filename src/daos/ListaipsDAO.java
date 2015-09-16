package daos;

import java.util.ArrayList;
import java.util.List;
import model.Listaip;

public class ListaipsDAO
{
    public static ArrayList<Listaip> findAllListaIps()
    {
        ArrayList<Listaip> arr = new ArrayList<Listaip>();
        List<Object> listaProvisoria = AbstractDAO.findAll(model.Listaip.class);
        
        for(Object o : listaProvisoria)
        {
            Listaip listaip = (Listaip) o ;
            
            arr.add(listaip);
        }
        return arr;
    }
    public static boolean update(Listaip listaip)
    {
        boolean logroUpdatear = false;
        
        daos.AbstractDAO.update(listaip);
        
        return logroUpdatear;
    }
}
