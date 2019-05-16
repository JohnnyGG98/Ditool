package ctr;

import java.io.File;
import javax.swing.JOptionPane;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 *
 * @author Johnny
 */
public class Descomprime {

    private final String carpeta;
    private final String nombre;
    private boolean segIntento = false;

    public Descomprime(String salida, String archivo) {
        this.carpeta = salida;
        this.nombre = archivo;
    }

    public void descomprime() {
        File zipFile = new File(carpeta + nombre);
        ZipFile zf;
        try {
            zf = new ZipFile(zipFile);
            zf.extractAll(carpeta);
            eliminarZip(zipFile);
        } catch (ZipException ex) {
            System.out.println("No salio bien debemos informarlo: " + ex.getMessage());
            if (!segIntento) {
                errorAlDescromprimir();
            } else {
                errorPermiso();
            }
        }
    }

    private void errorAlDescromprimir() {
        int r = JOptionPane.showConfirmDialog(null, "No pudimos guardar el archivo. \n"
                + "¿Desea intentarlo de nuevo?");
        if (r == JOptionPane.YES_OPTION) {
            segIntento = true;
            descomprime();
        }
    }

    private void errorPermiso() {
        JOptionPane.showMessageDialog(null, "Tuvimos incovenientes la instalar el sistema, \n"
                + "debera hacerlo de forma manual. \n"
                + "Indicaciones: \n"
                + "1. Debera extraer el .zip que se encuentra en " + carpeta + "\n"
                + "2. Despues de extraer el zip debera eliminarlo.");
    }

    private void eliminarZip(File zipFile) {
        if (zipFile.delete()) {
            int r = JOptionPane.showConfirmDialog(null, "Actualizamos todo de forma correcta.\n "
                    + "¿Desea abrir el programa?");
            if (r == JOptionPane.YES_OPTION) {
                CMD.ejecutarJAR(nombre);
            }
        }
    }

}
