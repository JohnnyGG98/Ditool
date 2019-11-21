package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Johnny
 */
public class BD {

    private static BD CON;

    public static BD single() {
        if (CON == null) {
            CON = new BD();
        }
        return CON;
    }

    private Connection getCon() {

        if (C.url == null) {
            C.url = utils.Utils.generarURL();
        }

        if (C.user == null) {
            C.user = utils.Utils.getUserBD();
        }

        if (C.pass == null) {
            C.pass = utils.Utils.getPassBD();
        }

        Connection c = null;

        try {
            Class.forName(C.DRIVER_POSTGRES);
            System.out.println("URL: " + C.url);
            System.out.println("Pass: " + C.pass);
            c = DriverManager.getConnection(C.url, C.user, C.pass);
        } catch (ClassNotFoundException e) {
            System.out.println("No pudimos conectarnos: " + e.getMessage());
        } catch (SQLException ex) {
            System.out.println("No nos pudimos conectar. " + ex.getMessage());
        }
        return c;
    }

    public PreparedStatement getPS(String sql) {
        PreparedStatement ps = null;
        Connection c = getCon();
        if (c != null) {
            try {
                ps = c.prepareStatement(sql);
            } catch (SQLException e) {
                System.out.println("No pudimos preparar el QUERY");
            }
        }
        return ps;
    }

    public PreparedStatement getPSParaID(String sql) {
        PreparedStatement ps = null;
        Connection c = getCon();
        if (c != null) {
            try {
                ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException e) {
                System.out.println("No pudimos preparar el QUERY");
            }
        }
        return ps;
    }

    public ResultSet getRes(PreparedStatement pst) {
        if (pst != null) {
            try {
                return pst.executeQuery();
            } catch (SQLException e) {
                System.out.println("No pudimos consultar. " + e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    public void cerrarCon(PreparedStatement pst) {
        try {
            pst.getConnection().close();
        } catch (SQLException e) {
            System.out.println("No cerramos conexion " + e.getMessage());
        }

    }

    public int getIdInsert(PreparedStatement ps) {
        int idGenerada = 0;
        try {
            idGenerada = ps.executeUpdate();
            if (C.M_DESARROLLO) {
                System.out.println("Afectamos a: " + idGenerada);
            }
            ResultSet res = ps.getGeneratedKeys();
            if (res.next()) {
                idGenerada = res.getInt(1);
            }
            ps.getConnection().close();
        } catch (SQLException e) {
            System.out.println("No pudimos ejecutar en la base de datos. " + e.getMessage());
        }
        //Devolvemos la id generada
        return idGenerada;
    }

    public boolean execute(PreparedStatement ps) {
        int res = 0;
        try {
            res = ps.executeUpdate();
            if (C.M_DESARROLLO) {
                System.out.println("Afectamos a: " + res);
            }
            ps.getConnection().close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "No pudimos guardar en la "
                    + "base de datos "
                    + e.getMessage()
            );
        }
        //Si es mayor a 0 sabemos que afecto a un registro
        return res > 0;
    }

    public ResultSet getRes(String sql) {
        PreparedStatement ps = getPS(sql);
        if (ps != null) {
            try {
                ResultSet rs = ps.executeQuery();
                return rs;
            } catch (SQLException e) {
                System.out.println("No logramos consultar: " + e.getMessage());
            }
        }
        return null;
    }

    //Metodos comunes 
    public boolean prepararExecuteElim(String SQL, int id, boolean elim) {
        boolean res = false;
        PreparedStatement ps = getPS(SQL);
        try {
            ps.setBoolean(1, elim);
            ps.setInt(2, id);
            res = execute(ps);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "No eliminamos: " + e.getMessage()
            );
        }
        return res;
    }

    public boolean eliminadoNoLogico(String SQL, int id) {
        boolean res = false;
        PreparedStatement ps = getPS(SQL);
        try {
            ps.setInt(1, id);
            res = execute(ps);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "No borramos: " + e.getMessage()
            );
        }
        return res;
    }

}
