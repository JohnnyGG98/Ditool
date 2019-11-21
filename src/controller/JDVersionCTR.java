package controller;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Version;
import model.VersionBD;
import view.JDVersion;
import view.VtnPrincipal;

/**
 *
 * @author Johnny
 */
public class JDVersionCTR {

    private final JDVersion jd;
    private ArrayList<Version> versiones;
    private final VersionBD ver = VersionBD.single();
    private DefaultTableModel mdTbl;
    private int posFila;

    public JDVersionCTR(VtnPrincipal vtnPrin) {
        this.jd = new JDVersion(vtnPrin, false);
        jd.setLocationRelativeTo(vtnPrin);
        jd.setVisible(true);
    }

    public void iniciar() {
        //Formato de la tabla
        formatoTbl();
        //Iniciar el formulario 
        iniciarAcciones();
        cargarDatos();
    }

    private void iniciarAcciones() {
        jd.getBtnIngresar().addActionListener(e -> clickIngresar());
        jd.getBtnEditar().addActionListener(e -> clickEditar());
        jd.getBtnEliminar().addActionListener(e -> clickEliminar());
        jd.getCbxEliminados().addActionListener(e -> clickEliminados());
    }

    private void formatoTbl() {
        String[] t = {"Autor", "Nombre", "Version"};
        String[][] d = {};
        iniciarTbl(t, d, jd.getTblVersiones());
    }

    private void iniciarTbl(String[] titulo, String[][] datos, JTable tbl) {
        mdTbl = new DefaultTableModel(datos, titulo) {
            @Override
            public final boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbl.setModel(mdTbl);
    }

    private void clickEliminados() {
        if (jd.getCbxEliminados().isSelected()) {
            cargarEliminados();
            jd.getBtnEditar().setEnabled(false);
            jd.getBtnEliminar().setEnabled(false);
        } else {
            cargarDatos();
            jd.getBtnEditar().setEnabled(true);
            jd.getBtnEliminar().setEnabled(true);
        }
    }

    private void cargarEliminados() {
        versiones = ver.cargarVersionesEliminadas();
        llenarTbl(versiones);
    }

    public void cargarDatos() {
        versiones = ver.cargarVersionesActivas();
        llenarTbl(versiones);
    }

    private void llenarTbl(ArrayList<Version> versiones) {
        mdTbl.setRowCount(0);
        if (versiones != null) {
            versiones.forEach(v -> {
                Object[] val = {v.getNombreSinExtension(), v.getUsername(), v.getVersion()};
                mdTbl.addRow(val);
            });
        }
    }

    private void clickIngresar() {
        FrmVersionCTR ctr = new FrmVersionCTR(this);
        ctr.iniciar();
    }

    private void clickEditar() {
        posFila = jd.getTblVersiones().getSelectedRow();
        if (posFila >= 0) {
            FrmVersionCTR ctr = new FrmVersionCTR(this);
            ctr.iniciar();
            ctr.editar(versiones.get(posFila).getId());
        }
    }

    private void clickEliminar() {
        posFila = jd.getTblVersiones().getSelectedRow();
        if (posFila >= 0) {
            if (ver.eliminar(versiones.get(posFila).getId())) {
                cargarDatos();
            }
        }
    }

}
