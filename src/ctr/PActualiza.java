package ctr;

import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import md.ConectarDB;
import md.Constantes;
import md.Version;
import vtn.VtnPrincipal;

/**
 *
 * @author Johnny
 */
public class PActualiza {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (!iniciaEstilo("Windows")) {
            iniciaEstilo("Nimbus");
        }

        VtnPrincipal vtnPrin = new VtnPrincipal();
        ConectarDB conecta = new ConectarDB("VERSION", "AZUL");

        System.out.println("Dirt: " + Constantes.getDir());
        Version v = new Version();
        v.setFecha(LocalDateTime.now());
        v.setId(0);
        v.setNombre("RestauranteM3A.zip");
        System.out.println("Nombre sin extension: " + v.getNombreSinExtension());
        v.setNotas("En esta version corregimos el error 1200.");
        v.setUrl("https://github.com/JohnnyGG98/PF-Restaurante-M3A/releases/download/Beta/RestauranteM3A.zip");
        v.setUsername("Master");
        v.setVersion("1");

        v = conecta.consultarUltimaVersion();

        if (v != null) {
            System.out.println("--------------");
            System.out.println(v.getId());
            System.out.println(v.getFecha());
            System.out.println(v.getNombre());
            System.out.println(v.getNotas());
            System.out.println(v.getUrl());
            System.out.println(v.getUsername());
            System.out.println(v.getVersion());
            System.out.println("--------------");
            VtnCTR ctrVtn = new VtnCTR(v, vtnPrin);
            ctrVtn.iniciar();
        } else {
            JOptionPane.showMessageDialog(vtnPrin, "Posiblemente no tengamos acceso a internet. \n"
                    + "Verifique su conexion e intentelo de nuevo.");
        }

        System.exit(0);
    }

    public static boolean iniciaEstilo(String estilo) {
        boolean encontrado = false;
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (estilo.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    encontrado = true;
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println("No enconramos ninugn LOOK AND FIELD " + ex.getMessage());
        }
        return encontrado;
    }

}
