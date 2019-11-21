package model;

import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import utils.dependencias.CBD;

/**
 *
 * @author alumno
 */
public class VersionBD extends CBD {

    private static VersionBD VBD;

    public static VersionBD single() {
        if (VBD == null) {
            VBD = new VersionBD();
        }
        return VBD;
    }

    public boolean guardar(Version v) {
        String nsql = "INSERT INTO public.\"Versiones\"(usu_username,\n"
                + "  version, nombre, url, notas)\n"
                + "  VALUES(?, ?, ?, ?, ?);";
        PreparedStatement ps = CON.getPS(nsql);

        try {
            ps.setString(1, v.getUsername());
            ps.setString(2, v.getVersion());
            ps.setString(3, v.getNombre());
            ps.setString(4, v.getUrl());
            ps.setString(5, v.getNotas());

            if (CON.execute(ps)) {
                JOptionPane.showMessageDialog(null, "Se guardo correctamente la version.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo guardar la version.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("No pudimos preparar el statement: " + e.getMessage());
            return false;
        }
    }

    public boolean editar(Version v) {
        String nsql = "UPDATE public.\"Versiones\" SET \n"
                + "  usu_username = ?, \n"
                + "  version = ?, \n"
                + "  nombre = ?, \n"
                + "  url = ?,\n"
                + "  notas = ?\n"
                + "  WHERE id_version = ?;";
        PreparedStatement ps = CON.getPS(nsql);

        try {
            ps.setString(1, v.getUsername());
            ps.setString(2, v.getVersion());
            ps.setString(3, v.getNombre());
            ps.setString(4, v.getUrl());
            ps.setString(5, v.getNotas());
            ps.setInt(6, v.getId());

            if (CON.execute(ps)) {
                JOptionPane.showMessageDialog(null, "Se edito correctamente la version.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo editar la version.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("No pudimos preparar el statement: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idVersion) {
        String nsql = "UPDATE public.\"Versiones\"\n"
                + "  SET version_activa = false\n"
                + "  WHERE id_version = ?;";
        PreparedStatement ps = CON.getPS(nsql);
        try {
            ps.setInt(1, idVersion);
            if (CON.execute(ps)) {
                JOptionPane.showMessageDialog(null, "Se elimino correctamente la version.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo eliminar la version.");
                return false;
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("No pudimos eliminar: " + e.getMessage());
            return false;
        }
    }

    public boolean activar(int idVersion) {
        String nsql = "UPDATE public.\"Versiones\"\n"
                + "  SET version_activa = true\n"
                + "  WHERE id_version = ?;";
        PreparedStatement ps = CON.getPS(nsql);
        try {
            ps.setInt(1, idVersion);
            if (CON.execute(ps)) {
                JOptionPane.showMessageDialog(null, "Se activo correctamente la version.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo activar la version.");
                return false;
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("No pudimos eliminar: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Version> cargarVersionesActivas() {
        String sql = "SELECT id_version, usu_username,\n"
                + "version, nombre\n"
                + "FROM public.\"Versiones\"\n"
                + "WHERE version_activa = true\n"
                + "ORDER BY fecha DESC;";
        PreparedStatement ps = CON.getPS(sql);
        return consultarParaTbl(ps);
    }

    public ArrayList<Version> cargarVersionesEliminadas() {
        String sql = "SELECT id_version, usu_username,\n"
                + "version, nombre\n"
                + "FROM public.\"Versiones\"\n"
                + "WHERE version_activa = false\n"
                + "ORDER BY fecha DESC;";
        PreparedStatement ps = CON.getPS(sql);
        return consultarParaTbl(ps);
    }

    private ArrayList<Version> consultarParaTbl(PreparedStatement ps) {
        ArrayList<Version> versiones = null;
        try {
            ResultSet rs = ps.executeQuery();
            versiones = new ArrayList<>();
            while (rs.next()) {
                Version v = new Version();
                v.setId(rs.getInt("id_version"));
                v.setNombre(rs.getString("nombre"));
                v.setVersion(rs.getString("version"));
                v.setUsername(rs.getString("usu_username"));

                versiones.add(v);
            }
        } catch (SQLException e) {
            System.out.println("No pudimos consultar versiones: " + e.getMessage());
        } finally {
            CON.cerrarCon(ps);
        }
        return versiones;
    }

    public Version existeVersion(String nombre, String version) {
        String sql = "SELECT id_version, usu_username \n"
                + "FROM public.\"Versiones\"\n"
                + "WHERE nombre ILIKE '%" + nombre + "%' \n"
                + "AND version = ? \n"
                + "AND version_activa = true;";
        Version v = null;
        PreparedStatement ps = CON.getPS(sql);
        try {
            ps.setString(1, version);
        } catch (SQLException e) {
            System.out.println("No pudimos preparar la consulta: " + e.getMessage());
        }

        try {
            ResultSet rs = ps.executeQuery();
            v = new Version();
            while (rs.next()) {
                v.setId(rs.getInt(1));
                v.setUsername(rs.getString(2));
            }
        } catch (SQLException e) {
            System.out.println("No pudimos consultar la version: " + e.getMessage());
        } finally {
            CON.cerrarCon(ps);
        }

        return v;
    }

    public Version consultarVersion(int idVersion) {
        String sql = "SELECT id_version, usu_username,\n"
                + "version, nombre, url, notas\n"
                + "FROM public.\"Versiones\"\n"
                + "WHERE id_version = ?;";
        Version v = null;
        PreparedStatement ps = CON.getPS(sql);
        try {
            ps.setInt(1, idVersion);
        } catch (SQLException e) {
            System.out.println("No pudimos preparar la consulta: " + e.getMessage());
        }

        try {
            ResultSet rs = ps.executeQuery();
            v = new Version();
            while (rs.next()) {
                v.setId(rs.getInt("id_version"));
                v.setUsername(rs.getString("usu_username"));
                v.setVersion(rs.getString("version"));
                v.setNombre(rs.getString("nombre"));
                v.setUrl(rs.getString("url"));
                v.setNotas(rs.getString("notas"));
            }
        } catch (SQLException e) {
            System.out.println("No pudimos consultar la version: " + e.getMessage());
        } finally {
            CON.cerrarCon(ps);
        }

        return v;
    }

    //Consultamos la ultima version de la base de datos:
    public Version consultarUltimaVersion() {
        Version v = null;
        String sql = "SELECT \n"
                + "id_version, \n"
                + "usu_username, \n"
                + "version,\n"
                + "nombre, \n"
                + "url, \n"
                + "notas, \n"
                + "fecha\n"
                + "FROM public.\"Versiones\"\n"
                + "WHERE version_activa = true\n"
                + "ORDER BY id_version DESC limit 1;";
        PreparedStatement ps = CON.getPS(sql);
        if (ps != null) {
            try {
                ResultSet rs = ps.executeQuery();
                v = new Version();
                while (rs.next()) {
                    v.setId(rs.getInt(1));
                    v.setUsername(rs.getString(2));
                    v.setVersion(rs.getString(3));
                    v.setNombre(rs.getString(4));
                    v.setUrl(rs.getString(5));
                    v.setNotas(rs.getString(6));
                    v.setFecha(rs.getTimestamp(7).toLocalDateTime());
                }
            } catch (SQLException e) {
                System.out.println("No se pudo consultar la version: " + e.getMessage());
            } finally {
                CON.cerrarCon(ps);
            }
        }
        return v;
    }

}
