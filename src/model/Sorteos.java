package model;
public class Sorteos
{
    private String cSorte;
    private String cJuego;
    private char fileName;
    private String fSorte;
    private String servj;

    public Sorteos()
    {
        this.cSorte = "";
        this.cJuego = "";;
        this.fileName = 'c';
        this.fSorte = "";;
        this.servj = "";;
    }
    public Sorteos(String cSorte, String cJuego, char fileName, String fSorte, String servj)
    {
        this.cSorte = cSorte;
        this.cJuego = cJuego;
        this.fileName = fileName;
        this.fSorte = fSorte;
        this.servj = servj;
    }
    
    //<editor-fold desc="GYS">
    public String getcSorte()
    {
        return cSorte;
    }

    public void setcSorte(String cSorte)
    {
        this.cSorte = cSorte;
    }

    public String getcJuego()
    {
        return cJuego;
    }

    public void setcJuego(String cJuego)
    {
        this.cJuego = cJuego;
    }

    public char getFileName()
    {
        return fileName;
    }

    public void setFileName(char fileName)
    {
        this.fileName = fileName;
    }

    public String getfSorte()
    {
        return fSorte;
    }

    public void setfSorte(String fSorte)
    {
        this.fSorte = fSorte;
    }

    public String getServj()
    {
        return servj;
    }

    public void setServj(String servj)
    {
        this.servj = servj;
    }
    //</editor-fold>

    @Override
    public String toString()
    {
        return "Sorteos{" + "cSorte=" + cSorte + ", cJuego=" + cJuego + ", fileName=" + fileName + ", fSorte=" + fSorte + ", servj=" + servj + '}';
    }
}
