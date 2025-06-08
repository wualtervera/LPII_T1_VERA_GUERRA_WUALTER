package com.cibertec.peliculas.ui;

import com.cibertec.peliculas.dao.AlquilerDAO;
import com.cibertec.peliculas.dao.ClienteDAO;
import com.cibertec.peliculas.dao.PeliculaDAO;
import com.cibertec.peliculas.model.Alquiler;
import com.cibertec.peliculas.model.Cliente;
import com.cibertec.peliculas.model.DetalleAlquiler;
import com.cibertec.peliculas.model.Pelicula;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlquilerFrame extends JFrame {

    private JComboBox<Cliente> cboClientes;
    private JComboBox<Pelicula> cboPeliculas;
    private JSpinner spnCantidad;
    private JTextArea txtDetalles;
    private JLabel lblTotal;
    private JButton btnAgregar, btnRegistrar, btnLimpiar;

    private ClienteDAO clienteDAO;
    private PeliculaDAO peliculaDAO;
    private AlquilerDAO alquilerDAO;

    private List<DetalleAlquiler> detallesTemp;
    private BigDecimal totalGeneral;

    public AlquilerFrame() {
        initComponents();
        initDAOs();
        cargarDatos();
        configurarEventos();
    }

    private void initComponents() {
        setTitle("Gestión de Alquiler de Películas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Configurar el panel principal con margen
        JPanel contenedorPrincipal = new JPanel(new BorderLayout(10, 10));
        contenedorPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel de selección (parte superior)
        JPanel panelSeleccion = crearPanelSeleccion();
        contenedorPrincipal.add(panelSeleccion, BorderLayout.NORTH);

        // Panel de detalles (centro)
        JPanel panelDetalles = crearPanelDetalles();
        contenedorPrincipal.add(panelDetalles, BorderLayout.CENTER);

        // Panel de total y botones (parte inferior)
        JPanel panelInferior = crearPanelInferior();
        contenedorPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(contenedorPrincipal, BorderLayout.CENTER);

        setSize(650, 550);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel crearPanelSeleccion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Selección de Película",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12)
        ));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Cliente
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCliente, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cboClientes = new JComboBox<>();
        cboClientes.setPreferredSize(new Dimension(350, 28));
        cboClientes.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(cboClientes, gbc);

        // Película
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblPelicula = new JLabel("Película:");
        lblPelicula.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblPelicula, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cboPeliculas = new JComboBox<>();
        cboPeliculas.setPreferredSize(new Dimension(350, 28));
        cboPeliculas.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(cboPeliculas, gbc);

        // Cantidad
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCantidad, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spnCantidad.setPreferredSize(new Dimension(80, 28));
        spnCantidad.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(spnCantidad, gbc);

        // Botón Agregar
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnAgregar = new JButton("Agregar");
        btnAgregar.setPreferredSize(new Dimension(100, 32));
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 11));
        btnAgregar.setBackground(new Color(70, 130, 180));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.add(btnAgregar, gbc);

        return panel;
    }

    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Detalles del Alquiler",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12)
        ));
        panel.setBackground(Color.WHITE);

        txtDetalles = new JTextArea(10, 40);
        txtDetalles.setEditable(false);
        txtDetalles.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtDetalles.setBackground(new Color(248, 248, 248));
        txtDetalles.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scrollDetalles = new JScrollPane(txtDetalles);
        scrollDetalles.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDetalles.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollDetalles.setBorder(BorderFactory.createLoweredBevelBorder());

        panel.add(scrollDetalles, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBackground(Color.WHITE);

        // Panel del total
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelTotal.setBackground(Color.WHITE);
        panelTotal.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Total",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12)
        ));

        JLabel lblTotalTexto = new JLabel("TOTAL A PAGAR:");
        lblTotalTexto.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalTexto.setForeground(new Color(70, 130, 180));

        lblTotal = new JLabel("S/. 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(new Color(220, 20, 60));

        panelTotal.add(lblTotalTexto);
        panelTotal.add(lblTotal);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(Color.WHITE);

        btnRegistrar = new JButton("Registrar Alquiler");
        btnRegistrar.setPreferredSize(new Dimension(150, 35));
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(34, 139, 34));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorder(BorderFactory.createRaisedBevelBorder());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setPreferredSize(new Dimension(100, 35));
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 12));
        btnLimpiar.setBackground(new Color(220, 20, 60));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(BorderFactory.createRaisedBevelBorder());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);

        panelInferior.add(panelTotal, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        return panelInferior;
    }

    private void initDAOs() {
        clienteDAO = new ClienteDAO();
        peliculaDAO = new PeliculaDAO();
        alquilerDAO = new AlquilerDAO();
        detallesTemp = new ArrayList<>();
        totalGeneral = BigDecimal.ZERO;
    }

    private void cargarDatos() {
        // Cargar clientes
        List<Cliente> clientes = clienteDAO.listarTodos();
        DefaultComboBoxModel<Cliente> modeloClientes = new DefaultComboBoxModel<>();
        for (Cliente cliente : clientes) {
            modeloClientes.addElement(cliente);
        }
        cboClientes.setModel(modeloClientes);

        // Configurar renderer para mostrar nombre del cliente
        cboClientes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) {
                    Cliente cliente = (Cliente) value;
                    setText(cliente.getNombre());
                }
                return this;
            }
        });

        // Cargar películas
        List<Pelicula> peliculas = peliculaDAO.listarTodos();
        DefaultComboBoxModel<Pelicula> modeloPeliculas = new DefaultComboBoxModel<>();
        for (Pelicula pelicula : peliculas) {
            modeloPeliculas.addElement(pelicula);
        }
        cboPeliculas.setModel(modeloPeliculas);

        // Configurar renderer para mostrar título de la película
        cboPeliculas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Pelicula) {
                    Pelicula pelicula = (Pelicula) value;
                    setText(pelicula.getTitulo() + " (Stock: " + pelicula.getStock() + ")");
                }
                return this;
            }
        });
    }

    private void configurarEventos() {
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarDetalle();
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarAlquiler();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
    }


    private void agregarDetalle() {
        try {
            Pelicula peliculaSeleccionada = (Pelicula) cboPeliculas.getSelectedItem();
            Integer cantidad = Integer.parseInt(spnCantidad.getValue().toString());

            if (peliculaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una película", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (cantidad > peliculaSeleccionada.getStock()) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DetalleAlquiler detalle = new DetalleAlquiler();
            detalle.setPelicula(peliculaSeleccionada); // Solo seteamos la película por ahora
            detalle.setCantidad(cantidad);

            detallesTemp.add(detalle);

            actualizarDetalles();
            calcularTotal();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar detalle: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarDetalles() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-30s %8s %10s %12s%n", "PELÍCULA", "CANT.", "PRECIO", "SUBTOTAL"));
        sb.append("─".repeat(65)).append("\n");

        for (DetalleAlquiler detalle : detallesTemp) {
            double precio = 5.00; // Precio fijo por simplicidad
            double subtotal = detalle.getCantidad() * precio;

            sb.append(String.format("%-30s %8d %10.2f %12.2f%n",
                    detalle.getPelicula().getTitulo().length() > 30 ?
                            detalle.getPelicula().getTitulo().substring(0, 27) + "..." :
                            detalle.getPelicula().getTitulo(),
                    detalle.getCantidad(),
                    precio,
                    subtotal
            ));
        }

        txtDetalles.setText(sb.toString());
    }

    private void calcularTotal() {
        totalGeneral = BigDecimal.ZERO;
        for (DetalleAlquiler detalle : detallesTemp) {
            BigDecimal subtotal = new BigDecimal(detalle.getCantidad() * 5.00); // Precio fijo
            totalGeneral = totalGeneral.add(subtotal);
        }
        lblTotal.setText("S/. " + String.format("%.2f", totalGeneral.doubleValue()));
    }

    private void registrarAlquiler() {
        try {
            // Validaciones
            if (cboClientes.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (detallesTemp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Agregue al menos una película", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear alquiler
            Alquiler alquiler = new Alquiler();
            alquiler.setCliente((Cliente) cboClientes.getSelectedItem());
            alquiler.setFecha(LocalDate.now());
            alquiler.setTotal(totalGeneral);

            // CORRECCIÓN: Usar el método helper para establecer relaciones
            for (DetalleAlquiler detalle : detallesTemp) {
                alquiler.addDetalle(detalle);
            }

            // Guardar en base de datos
            alquilerDAO.guardar(alquiler);

            // Actualizar stock
            for (DetalleAlquiler detalle : detallesTemp) {
                Pelicula pelicula = detalle.getPelicula();
                pelicula.setStock(pelicula.getStock() - detalle.getCantidad());
                peliculaDAO.actualizar(pelicula);
            }

            JOptionPane.showMessageDialog(this, "Alquiler registrado exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarDatos(); // Recargar películas con stock actualizado

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar alquiler: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        if (cboClientes.getItemCount() > 0) cboClientes.setSelectedIndex(0);
        if (cboPeliculas.getItemCount() > 0) cboPeliculas.setSelectedIndex(0);
        spnCantidad.setValue(1);
        txtDetalles.setText("");
        lblTotal.setText("S/. 0.00");
        detallesTemp.clear();
        totalGeneral = BigDecimal.ZERO;
    }


    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        SwingUtilities.invokeLater(() -> {
            new AlquilerFrame().setVisible(true);
        });
    }
}