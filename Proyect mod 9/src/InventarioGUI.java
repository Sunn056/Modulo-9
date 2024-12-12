import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventarioGUI extends JFrame {
    private JTextField codigoField;
    private JTextField nombreField;
    private JTextField precioField;
    private JTextField stockField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField buscarNombreField;
    private JTextField codigoSalidaField;
    private JTextField precioSalidaField;
    private JTextField stockSalidaField;
    private JTextField cantidadSalidaField;
    private DefaultTableModel balanceTableModel;

    public InventarioGUI() {
        setTitle("Sistema de Gestion de Inventarios");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel de entrada de productos
        JPanel entradaPanel = new JPanel();
        entradaPanel.setLayout(new GridLayout(5, 2, 5, 5));
        entradaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        entradaPanel.setBackground(new Color(252, 216, 89));
        entradaPanel.add(new JLabel("Codigo del Producto:"));
        codigoField = new JTextField();
        entradaPanel.add(codigoField);
        entradaPanel.add(new JLabel("Nombre del Producto:"));
        nombreField = new JTextField();
        entradaPanel.add(nombreField);
        entradaPanel.add(new JLabel("Precio:"));
        precioField = new JTextField();
        entradaPanel.add(precioField);
        entradaPanel.add(new JLabel("Cantidad en Stock:"));
        stockField = new JTextField();
        entradaPanel.add(stockField);
        JButton agregarButton = new JButton("Agregar Producto");
        entradaPanel.add(agregarButton);

        // Panel de salida de productos
        JPanel salidaPanel = new JPanel();
        salidaPanel.setLayout(new GridLayout(6, 2, 5, 5));
        salidaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        salidaPanel.setBackground(new Color(252, 216, 89));
        salidaPanel.add(new JLabel("Buscar por Nombre:"));
        buscarNombreField = new JTextField();
        salidaPanel.add(buscarNombreField);
        salidaPanel.add(new JLabel("Codigo del Producto:"));
        codigoSalidaField = new JTextField();
        codigoSalidaField.setEditable(false);
        salidaPanel.add(codigoSalidaField);
        salidaPanel.add(new JLabel("Precio:"));
        precioSalidaField = new JTextField();
        precioSalidaField.setEditable(false);
        salidaPanel.add(precioSalidaField);
        salidaPanel.add(new JLabel("Cantidad en Stock:"));
        stockSalidaField = new JTextField();
        stockSalidaField.setEditable(false);
        salidaPanel.add(stockSalidaField);
        salidaPanel.add(new JLabel("Cantidad a Vender:"));
        cantidadSalidaField = new JTextField();
        salidaPanel.add(cantidadSalidaField);
        JButton venderButton = new JButton("Vender Producto");
        salidaPanel.add(venderButton);

        // Panel de balance
        JPanel balancePanel = new JPanel(new BorderLayout());
        String[] balanceColumnNames = {"Codigo", "Nombre", "Precio", "Total Agregado", "Total de Ventas", "Balance"};
        balanceTableModel = new DefaultTableModel(balanceColumnNames, 0);
        JTable balanceTable = new JTable(balanceTableModel);
        JScrollPane balanceScrollPane = new JScrollPane(balanceTable);
        balancePanel.add(balanceScrollPane, BorderLayout.CENTER);

        // Tabla para mostrar productos
        String[] columnNames = {"Codigo", "Nombre", "Precio", "Cantidad"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Añade paneles al panel de pestañas
        tabbedPane.addTab("Entrada de Productos", entradaPanel);
        tabbedPane.addTab("Salida de Productos", salidaPanel);
        tabbedPane.addTab("Balance", balancePanel);

        // Añade panel de pestañas y tabla al marco
        add(tabbedPane, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Accion del boton agregar
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });

        // boton vender
        venderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                venderProducto();
            }
        });

        // busqueda por nombre
        buscarNombreField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoPorNombre();
            }
        });

        // busqueda por codigo
        codigoField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoPorCodigo();
            }
        });
    }

    private void agregarProducto() {
        try {
            String codigo = codigoField.getText();
            String nombre = nombreField.getText();
            double precio = Double.parseDouble(precioField.getText());
            int stock = Integer.parseInt(stockField.getText());

            boolean productoExistente = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(codigo)) {
                    int stockActual = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
                    int nuevoStock = stockActual + stock;
                    tableModel.setValueAt(nuevoStock, i, 3);
                    balanceTableModel.setValueAt(nuevoStock, i, 3);
                    balanceTableModel.setValueAt(nuevoStock, i, 5); // Actualizar balance
                    productoExistente = true;
                    break;
                }
            }

            if (!productoExistente) {
                Object[] row = {codigo, nombre, precio, stock};
                tableModel.addRow(row);
                balanceTableModel.addRow(new Object[]{codigo, nombre, precio, stock, 0, stock});
            }

            //Limpia campos de texto
            codigoField.setText("");
            nombreField.setText("");
            precioField.setText("");
            stockField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores validos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void venderProducto() {
        try {
            int cantidadVender = Integer.parseInt(cantidadSalidaField.getText());
            int stockActual = Integer.parseInt(stockSalidaField.getText());
            if (cantidadVender > stockActual) {
                JOptionPane.showMessageDialog(this, "Cantidad a vender excede el stock disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int nuevaCantidad = stockActual - cantidadVender;
            stockSalidaField.setText(String.valueOf(nuevaCantidad));

            // Actualiza la tabla
            int selectedRow = table.getSelectedRow();
            tableModel.setValueAt(nuevaCantidad, selectedRow, 3);

            // Actualiza el balance
            for (int i = 0; i < balanceTableModel.getRowCount(); i++) {
                if (balanceTableModel.getValueAt(i, 0).equals(codigoSalidaField.getText())) {
                    int totalVentas = Integer.parseInt(balanceTableModel.getValueAt(i, 4).toString()) + cantidadVender;
                    int balance = Integer.parseInt(balanceTableModel.getValueAt(i, 3).toString()) - cantidadVender;
                    balanceTableModel.setValueAt(totalVentas, i, 4);
                    balanceTableModel.setValueAt(balance, i, 5);
                    break;
                }
            }

            //limpia campos de texto
            buscarNombreField.setText("");
            codigoSalidaField.setText("");
            precioSalidaField.setText("");
            stockSalidaField.setText("");
            cantidadSalidaField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad valida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProductoPorNombre() {
        String nombre = buscarNombreField.getText().toLowerCase();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String nombreProducto = tableModel.getValueAt(i, 1).toString().toLowerCase();
            if (nombreProducto.contains(nombre)) {
                codigoSalidaField.setText(tableModel.getValueAt(i, 0).toString());
                precioSalidaField.setText(tableModel.getValueAt(i, 2).toString());
                stockSalidaField.setText(tableModel.getValueAt(i, 3).toString());
                table.setRowSelectionInterval(i, i);
                buscarNombreField.setText(tableModel.getValueAt(i, 1).toString());
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void buscarProductoPorCodigo() {
        String codigo = codigoField.getText();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(codigo)) {
                nombreField.setText(tableModel.getValueAt(i, 1).toString());
                precioField.setText(tableModel.getValueAt(i, 2).toString());
                stockField.setText("");
                return;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventarioGUI().setVisible(true);
            }
        });
    }
}