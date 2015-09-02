package daos;

import java.util.ArrayList;
import java.util.List;
import model.Ultimosorteo;
public class UltimoSorteoDAO
{
    public static ArrayList<model.Ultimosorteo> findAllUltimosSorteos()
    {
        ArrayList<model.Ultimosorteo> arrRespuesta = new ArrayList<Ultimosorteo>();
        List<Object> listaProvisoria = AbstractDAO.findAll(model.Ultimosorteo.class);
        
        for(Object o : listaProvisoria)
        {
            Ultimosorteo ultimosorteo = (Ultimosorteo) o;
            arrRespuesta.add(ultimosorteo);
        }        
        
        return arrRespuesta;
    }
}
