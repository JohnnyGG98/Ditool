package controller;

import javax.swing.JOptionPane;
import model.Version;
import model.VersionBD;
import view.VtnDitool;

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

        VtnDitool vtnPrin = new VtnDitool();
        vtnPrin.setTitle("Ditool | Version instalada: ");
        VersionBD VBD = VersionBD.single();

        Version v = VBD.consultarUltimaVersion();
        if (v != null) {
            VtnDitoolCTR ctrVtn = new VtnDitoolCTR(
                    v,
                    vtnPrin,
                    false
            );
            ctrVtn.iniciar();
        } else {
            JOptionPane.showMessageDialog(
                    vtnPrin, "Posiblemente no tengamos acceso al servidor. \n"
                    + "Verifique su conexion e intentelo nuevamente. \n"
                    + "Debe contactar con el administrador de l"
                    + "a base de datos o su proveedor de internet."
            );
            System.exit(0);
        }

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
