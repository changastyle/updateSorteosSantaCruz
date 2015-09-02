package daos;

import java.util.ArrayList;
import java.util.List;
import model.Instalaciones;

public class InstalacionesDAO
{
    private static Object model;
    public static ArrayList<model.Instalaciones> findAllInstalaciones()
    {
        ArrayList<model.Instalaciones> arrInstalaciones = new ArrayList<model.Instalaciones>();
        List<Object> listaProvisoria = (List<Object>) AbstractDAO.findAll(model.Instalaciones.class);
        
        for(Object o : listaProvisoria)
        {
            Instalaciones instalacionAUX = (model.Instalaciones) o;
            
            arrInstalaciones.add(instalacionAUX);
        }
        
        
        return arrInstalaciones;
    }
}
