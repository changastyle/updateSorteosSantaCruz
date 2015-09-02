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
import model.Sorteo;
import model.Ultimosorteo;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;


public class UpdateSorteos
{

    public static void main(String[] args) throws Exception
    {
        //controller.Controller.imprimirSorteosFromDB();
       
        //controller.Controller.hacer();
        controller.Controller.unzip("carpetitaDeLosSorteos/7658.zip");

    }
}
