package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import utils.C;
import model.Version;
import view.VtnDitool;

/**
 *
 * @author alumno
 */
public class VtnDitoolCTR {

    private final Version version;
    private final VtnDitool vtnDitool;
    private boolean aCorrecto = true;
    private final boolean existeVtnPrincipal;
    private final ImageIcon icono;

    public VtnDitoolCTR(
            Version version,
            VtnDitool vtnPrin,
            boolean existeVtnPrincipal
    ) {
        this.icono = new ImageIcon(getClass().getResource("/img/update.png"));
        this.version = version;
        this.vtnDitool = vtnPrin;
        this.existeVtnPrincipal = existeVtnPrincipal;
        this.vtnDitool.setIconImage(icono.getImage());
    }

    public void iniciar() {
        mostrarVtn();
        mostrarInformacion();
        if (validarVersion()) {
            if (aCorrecto) {
                comprobarVersion();
            }
        } else {
            JOptionPane.showMessageDialog(
                    vtnDitool,
                    "No se an publicado versiones."
            );
            ejecutarPrograma();
        }

    }

    private void mostrarInformacion() {
        File pv = new File(C.V_DIR);
        Properties p = new Properties();
        if (pv.exists()) {
            if (comprobarRequisitos(p, pv)) {
                try {
                    p.load(new FileReader(pv));
                    vtnDitool.getLblAutor().setText(p.getProperty(C.V_AUTOR));
                    vtnDitool.getLblNombre().setText(version.getNombreSinExtension(p.getProperty(C.V_NOMBRE)));
                    vtnDitool.getLblVersion().setText(p.getProperty(C.V_VERSION));
                    //Ponemos la version del sistema en el titulo
                    vtnDitool.setTitle(vtnDitool.getTitle() + " " + p.getProperty(C.V_VERSION));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                            vtnDitool,
                            "No pudimos leer las propiedades de version: "
                            + e.getMessage()
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                        vtnDitool,
                        "Su archivo de propiedades esta corrupto,\n"
                        + "vamos a descargar todo nuevamente."
                );
                aCorrecto = false;
                if (validarVersion()) {
                    crearVersion();
                    cerrarTodo();
                }

            }
        }

    }

    private void mostrarVtn() {
        vtnDitool.setVisible(true);
        vtnDitool.setLocationRelativeTo(null);
    }

    private void comprobarVersion() {
        File pv = new File(C.V_DIR);
        Properties p = new Properties();
        if (pv.exists()) {
            if (comprobarRequisitos(p, pv)) {
                if (compara(p, pv)) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Esta instalada la ultima version del software."
                    );

                    if (existeVtnPrincipal) {
                        // Si existe una ventana principal 
                    } else {
                        ejecutarPrograma();
                    }
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

    private void crearVersion() {
        Object[] opt;

        if (existeVtnPrincipal) {
            opt = new Object[]{"Actualizar", "Abrir", "Salir"};
        } else {
            opt = new Object[]{"Actualizar", "Cancelar", "Salir"};
        }

        int s = JOptionPane.showOptionDialog(vtnDitool,
                "\n"
                + "¿Tiene una actualizacion pendiente del sistema?\n"
                + "Si no actualiza el sistema no tendra acceso a \n"
                + "todas las funciones del sistema.",
                "Actualizacion pendiente",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, opt, "Actualizar");
        switch (s) {
            case 0:
                actualizarSistema();
                break;
            case 1:
                if (existeVtnPrincipal) {
                    // Habilitamos el programa 
                } else {
                    ejecutarPrograma();
                }
                break;
            case 2:
                System.exit(0);
                break;
            default:
                if (existeVtnPrincipal) {
                    // Habilitamos el programa 
                }
                cerrarTodo();
                break;
        }
    }

    private void actualizarSistema() {
        vtnDitool.getLblFondo().setText("Descargando...");
        File pv = new File(C.V_DIR);
        Properties p = new Properties();
        Descarga descarga = new Descarga(version);
        if (descarga.descargar()) {
            vtnDitool.getLblFondo().setText("Descargado");
            crearPropiedades(p, pv);
            Descomprime uzip = new Descomprime(version);
            vtnDitool.getLblFondo().setText("Instalando...");
            uzip.descomprime();
        }
    }

    private boolean compara(Properties p, File bd) {
        boolean igual = true;
        String pNombre = "";
        String pVersion = "";
        try {
            p.load(new FileReader(bd));
            pNombre = p.getProperty(C.V_NOMBRE);
            pVersion = p.getProperty(C.V_VERSION);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    vtnDitool,
                    "No pudimos encontramos las propiedades de version: "
                    + e.getMessage()
            );
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
            p.setProperty(C.V_AUTOR, version.getUsername());
            p.setProperty(C.V_NOMBRE, version.getNombre());
            p.setProperty(C.V_VERSION, version.getVersion());
            p.setProperty(C.V_FECHA, version.getFecha().toString());
            p.setProperty(C.V_NOTAS, version.getNotas());
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
            if (p.getProperty(C.V_AUTOR) == null) {
                todas = false;
            }

            if (p.getProperty(C.V_FECHA) == null) {
                todas = false;
            }

            if (p.getProperty(C.V_NOMBRE) == null) {
                todas = false;
            }

            if (p.getProperty(C.V_NOTAS) == null) {
                todas = false;
            }

            if (p.getProperty(C.V_VERSION) == null) {
                todas = false;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    vtnDitool,
                    "Error al comprobar si tiene todas las constantes:"
                    + e.getMessage()
            );
        }
        return todas;
    }

    private void ejecutarPrograma() {
        File pv = new File(C.V_DIR);
        Properties p = new Properties();
        try {
            p.load(new FileReader(pv));
            String pNombre = version.getNombreSinExtension(p.getProperty(C.V_NOMBRE));
            CMD.ejecutarJAR(pNombre);
            cerrarTodo();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    vtnDitool,
                    "No se pudo encontrar la propiedad de nombre: "
                    + e.getMessage()
            );
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
