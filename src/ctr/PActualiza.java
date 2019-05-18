package ctr;

import javax.swing.JOptionPane;
import md.ConectarDB;
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
        vtnPrin.setTitle("Version:");
        ConectarDB conecta = new ConectarDB("VERSION", "AZUL");

        Version v = conecta.consultarUltimaVersion();

        if (v != null) {
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
