/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;
import Entidades.AdministradorEntidad;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author le0jx
 */
public class FrameMain extends JFrame {

     private AdministradorEntidad administrador;

    private JLabel lblBienvenida;
    private JButton btnEventos;
    private JButton btnDashboardEvento;
    private JButton btnDashboardGeneral;
    private JButton btnCerrarSesion;

    public FrameMain(AdministradorEntidad administrador) {
        this.administrador = administrador;
        iniciarComponentes();
    }

    private void iniciarComponentes() {

        setTitle("TicketFan - Administrador");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setBorder(
                BorderFactory.createEmptyBorder(30, 60, 30, 60)
        );

        panel.setLayout(new GridLayout(5, 1, 10, 10));

        // Bienvenida
        lblBienvenida = new JLabel(
                "Bienvenido, " + administrador.getNombres(),
                SwingConstants.CENTER
        );

        // Botones
        btnEventos = new JButton("Administrar eventos");
        btnDashboardEvento = new JButton("Dashboard por evento");
        btnDashboardGeneral = new JButton("Dashboard general");
        btnCerrarSesion = new JButton("Cerrar sesión");

        // Agregar componentes
        panel.add(lblBienvenida);
        panel.add(btnEventos);
        panel.add(btnDashboardEvento);
        panel.add(btnDashboardGeneral);
        panel.add(btnCerrarSesion);

        add(panel);

        // Eventos de botones
        btnEventos.addActionListener(e -> abrirEventos());

        btnDashboardEvento.addActionListener(
                e -> abrirDashboardEvento()
        );

        btnDashboardGeneral.addActionListener(
                e -> abrirDashboardGeneral()
        );

        btnCerrarSesion.addActionListener(
                e -> cerrarSesion()
        );
    }

    private void abrirEventos() {

        FrmEventos ventana =
                new FrmEventos(administrador);

        ventana.setVisible(true);
    }

    private void abrirDashboardEvento() {

        FrmDashboardEvento ventana =
                new FrmDashboardEvento(administrador);

        ventana.setVisible(true);
    }

    private void abrirDashboardGeneral() {

        FrmDashboardGeneral ventana =
                new FrmDashboardGeneral(administrador);

        ventana.setVisible(true);
    }

    private void cerrarSesion() {

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {

            FrmLogin login = new FrmLogin();
            login.setVisible(true);

            dispose();
        }
    }
}
