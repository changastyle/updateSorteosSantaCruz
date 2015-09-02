package model;
public class Sorteo
{
    private int cSorte;
    private String cJuego;
    private char fileName;
    private String fSorte;
    private String servj;

    public Sorteo()
    {
        this.cSorte = 0;
        this.cJuego = "";;
        this.fileName = 'c';
        this.fSorte = "";;
        this.servj = "";;
    }
    public Sorteo(int cSorte, String cJuego, char fileName, String fSorte, String servj)
    {
        this.cSorte = cSorte;
        this.cJuego = cJuego;
        this.fileName = fileName;
        this.fSorte = fSorte;
        this.servj = servj;
    }
    
    //<editor-fold desc="GYS">
    public int getcSorte()
    {
        return cSorte;
    }

    public void setcSorte(int cSorte)
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
        return "Sorteo{" + "cSorte=" + cSorte + ", cJuego=" + cJuego + ", fileName=" + fileName + ", fSorte=" + fSorte + ", servj=" + servj + '}';
    }
}
