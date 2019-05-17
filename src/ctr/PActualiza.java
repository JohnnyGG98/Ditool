package ctr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import md.ConectarDB;
import md.Constantes;
import md.Version;

/**
 *
 * @author Johnny
 */
public class PActualiza {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //ConectarDB conecta = new ConectarDB("johnny", "DEV");
        System.out.println("Dirt: " + Constantes.getDir());
        Version v = new Version();
        v.setFecha(LocalDateTime.now());
        v.setId(0);
        v.setNombre("RestauranteM3A.zip");
        System.out.println("Nombre sin extension: " + v.getNombreSinExtension());
        v.setNotas("En esta version corregimos el error 1200.");
        v.setUrl("https://github.com/JohnnyGG98/PF-Restaurante-M3A/releases/download/Beta/RestauranteM3A.zip");
        v.setUsername("Master");
        v.setVersion("10");

        System.out.println("Ahora compararemos: ");
        v.comprobarVersion();
        System.out.println("--------------");

    }

}
