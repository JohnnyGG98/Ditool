package md;

import ctr.CMD;
import ctr.Descarga;
import ctr.Descomprime;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Johnny
 */
public class Version {

    private int id;
    private String username;
    private String version;
    private String url;
    private String nombre;
    private String notas;
    private LocalDateTime fecha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getNombreSinExtension() {
        return nombre.substring(0, nombre.lastIndexOf("."));
    }

    public String getNombreSinExtension(String nombre) {
        return nombre.substring(0, nombre.lastIndexOf("."));
    }

    public void comprobarVersion() {
        File pv = new File(Constantes.V_DIR);
        Properties p = new Properties();
        if (pv.exists()) {
            if (comprobarRequisitos(p, pv)) {
                if (compara(p, pv)) {
                    JOptionPane.showMessageDialog(null, "Usted tiene la ultima version!");
                    CMD.ejecutarJAR(getNombreSinExtension());
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

    }

    public void crearVersion() {
        File pv = new File(Constantes.V_DIR);
        Properties p = new Properties();
        Descarga descarga = new Descarga(this);
        if (descarga.descargar()) {
            borrarVersionAnterior(p, pv);
            crearPropiedades(p, pv);
            Descomprime uzip = new Descomprime(this);
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

        if (!pNombre.equals(nombre)) {
            igual = false;
        }

        if (!pVersion.equals(version)) {
            igual = false;
        }

        return igual;
    }

    private boolean crearPropiedades(Properties p, File bd) {
        boolean creado = false;
        try {
            FileOutputStream fo = new FileOutputStream(bd);
            p.setProperty(Constantes.V_AUTOR, username);
            p.setProperty(Constantes.V_NOMBRE, nombre);
            p.setProperty(Constantes.V_VERSION, version);
            p.setProperty(Constantes.V_FECHA, fecha.toString());
            p.setProperty(Constantes.V_NOTAS, notas);
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
            String pNombre = getNombreSinExtension(p.getProperty(Constantes.V_NOMBRE));
            if (pNombre.contains(nombre)) {
                File va = new File(pNombre + ".jar");
                if (va.exists()) {
                    va.delete();
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo encontrar la propiedad de nombre: " + e.getMessage());
        }
    }

}
