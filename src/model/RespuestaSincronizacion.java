package model;

public class RespuestaSincronizacion
{
    private String direccionIP;
    private String atributoS;
    private char estado;

    public RespuestaSincronizacion(String direccionIP, String atributoS, char estado)
    {
        this.direccionIP = direccionIP;
        this.atributoS = atributoS;
        this.estado = estado;
    }

    public RespuestaSincronizacion()
    {
        this.direccionIP = "";
        this.atributoS = "";
        this.estado = '0';
    }
    
    
    //<editor-fold desc="GYS">
    public String getDireccionIP()
    {
        return direccionIP;
    }

    public void setDireccionIP(String direccionIP)
    {
        this.direccionIP = direccionIP;
    }

    public String getAtributoS()
    {
        return atributoS;
    }

    public void setAtributoS(String atributoS)
    {
        this.atributoS = atributoS;
    }

    public char getEstado()
    {
        return estado;
    }

    public void setEstado(char estado)
    {
        this.estado = estado;
    }
    //</editor-fold>

    @Override
    public String toString()
    {
        return "RespuestaSincronizacion{" + "direccionIP=" + direccionIP + ", atributoS=" + atributoS + ", estado=" + estado + '}';
    }
    
   
}
