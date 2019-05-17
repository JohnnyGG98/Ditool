package ctr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;
import md.Constantes;
import md.Version;
import vtn.VtnPrincipal;

/**
 *
 * @author alumno
 */
public class VtnCTR {

    private final Version version;
    private final VtnPrincipal vtnPrin;
    private boolean aCorrecto = true;

    public VtnCTR(Version version, VtnPrincipal vtnPrin) {
        this.version = version;
        this.vtnPrin = vtnPrin;
    }

    public void iniciar() {
        mostrarVtn();
        mostrarInformacion();
        if (validarVersion()) {
            if (aCorrecto) {
                comprobarVersion();
            }
        } else {
            JOptionPane.showMessageDialog(vtnPrin, "No se an publicado versiones.");
            ejecutarPrograma();
        }

    }

    private void mostrarInformacion() {
        File pv = new File(Constantes.V_DIR);
        Properties p = new Properties();
        if (pv.exists()) {
            if (comprobarRequisitos(p, pv)) {
                try {
                    p.load(new FileReader(pv));
                    vtnPrin.getLblAutor().setText(p.getProperty(Constantes.V_AUTOR));
                    vtnPrin.getLblNombre().setText(p.getProperty(Constantes.V_NOMBRE));
                    vtnPrin.getLblVersion().setText(p.getProperty(Constantes.V_VERSION));
                } catch (IOException e) {
                    System.out.println("No pudimos leer las propiedades de version: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(vtnPrin, "Su archivo de propiedades esta corrupto,\n"
                        + "vamos a descargar todo nuevamente.");
                aCorrecto = false;
                if (validarVersion()) {
                    crearVersion();
                    cerrarTodo();
                }

            }
        }

    }

    public void mostrarVtn() {
        vtnPrin.setVisible(true);
        vtnPrin.setLocationRelativeTo(null);
    }

    public void comprobarVersion() {
        File pv = new File(Constantes.V_DIR);
        Properties p = new Properties();
        if (pv.exists()) {
            if (comprobarRequisitos(p, pv)) {
                if (compara(p, pv)) {
                    JOptionPane.showMessageDialog(null, "Usted tiene la ultima version!");
                    ejecutarPrograma();
                } else {
                    //Aqui descargamos la versio nueva
                    crearVersion();
                }
            } else {
                //Si el archivo de version no contiene todo lo necesario se descarga
                crearVersion();
            }
        } else {
            crearVersion();
        }
        cerrarTodo();
    }

    public void crearVersion() {
        File pv = new File(Constantes.V_DIR);
        Properties p = new Properties();
        Descarga descarga = new Descarga(version);
        if (descarga.descargar()) {
            if (aCorrecto) {
                borrarVersionAnterior(p, pv);
            }
            crearPropiedades(p, pv);
            Descomprime uzip = new Descomprime(version);
            uzip.descomprime();
        }
    }

    private boolean compara(Properties p, File bd) {
        boolean igual = true;
        String pNombre = "";
        String pVersion = "";
        try {
            p.load(new FileReader(bd));
            pNombre = p.getProperty(Constantes.V_NOMBRE);
            pVersion = p.getProperty(Constantes.V_VERSION);
        } catch (IOException e) {
            System.out.println("No pudimos leer las propiedades de version: " + e.getMessage());
        }

        if (!pNombre.equals(version.getNombre())) {
            igual = false;
        }

        if (!pVersion.equals(version.getVersion())) {
            igual = false;
        }

        return igual;
    }

    private boolean crearPropiedades(Properties p, File bd) {
        boolean creado = false;
        try {
            FileOutputStream fo = new FileOutputStream(bd);
            p.setProperty(Constantes.V_AUTOR, version.getUsername());
            p.setProperty(Constantes.V_NOMBRE, version.getNombre());
            p.setProperty(Constantes.V_VERSION, version.getVersion());
            p.setProperty(Constantes.V_FECHA, version.getFecha().toString());
            p.setProperty(Constantes.V_NOTAS, version.getNotas());
            p.store(fo, "Descripcion de la version del sistema: ");
            creado = true;
        } catch (FileNotFoundException e) {
            System.out.println("No podemos escribir el archivo: " + e.getMessage());
        } catch (IOException ex) {
            System.out.println("No se pudo guardar el properties: " + ex.getMessage());
        }
        return creado;
    }

    private boolean comprobarRequisitos(Properties p, File bd) {
        boolean todas = true;
        try {
            p.load(new FileReader(bd));
            if (p.getProperty(Constantes.V_AUTOR) == null) {
                todas = false;
            }

            if (p.getProperty(Constantes.V_FECHA) == null) {
                todas = false;
            }

            if (p.getProperty(Constantes.V_NOMBRE) == null) {
                todas = false;
            }

            if (p.getProperty(Constantes.V_NOTAS) == null) {
                todas = false;
            }

            if (p.getProperty(Constantes.V_VERSION) == null) {
                todas = false;
            }

        } catch (IOException e) {
            System.out.println("Error al comprobar si tiene todas las constantes:" + e.getMessage());
        }
        return todas;
    }

    private void borrarVersionAnterior(Properties p, File bd) {
        try {
            p.load(new FileReader(bd));
            String pNombre = version.getNombreSinExtension(p.getProperty(Constantes.V_NOMBRE));
            if (pNombre.contains(version.getNombre())) {
                File va  = new File(pNombre + ".jar");
                if (va.exists()) {
                    va.delete();
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo encontrar la propiedad de nombre: " + e.getMessage());
        }
    }

    private void ejecutarPrograma() {
        File pv = new File(Constantes.V_DIR);
        Properties p = new Properties();
        try {
            p.load(new FileReader(pv));
            String pNombre = version.getNombreSinExtension(p.getProperty(Constantes.V_NOMBRE));
            CMD.ejecutarJAR(pNombre);
        } catch (IOException e) {
            System.out.println("No se pudo encontrar la propiedad de nombre: " + e.getMessage());
        }
    }

    private void cerrarTodo() {
        System.exit(0);
    }

    private boolean validarVersion() {
        return !(version.getFecha() == null
                || version.getId() == 0
                || version.getNombre() == null
                || version.getNotas() == null
                || version.getUrl() == null
                || version.getUsername() == null
                || version.getVersion() == null);
    }

}
