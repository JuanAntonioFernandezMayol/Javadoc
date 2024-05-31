
package javadoc;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * La clase principal de la aplicación Gourmet Manager.
 */
public class App {

    /**
     * Método principal que inicia la aplicación.
     *
     * @param args Los argumentos de la línea de comandos que se pasan a la
     *             aplicación.
     */
    public static void main(String[] args) {

        /**
         * Crea un nuevo JFrame con el título "Gourmet Manager". Configura el
         * comportamiento de cierre por defecto y el tamaño del marco.
         * Crea un nuevo JPanel con un GridLayout de 3 filas y 2 columnas.
         * Crea varios botones (restauranteButton, chefButton, platoButton, menuButton,
         * reservaButton) cada uno con un ActionListener que llama a un método
         * específico cuando se hace clic en el botón.
         * Añade los botones al panel.
         * Añade el panel al contenido del marco.
         * Hace visible el marco.
         */

        JFrame frame = new JFrame("Gourmet Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JButton restauranteButton = new JButton("Gestión de Restaurantes");
        restauranteButton.addActionListener(e -> showRestauranteScreen());

        JButton chefButton = new JButton("Gestión de Chefs");
        chefButton.addActionListener(e -> showChefScreen());

        JButton platoButton = new JButton("Gestión de Platos");
        platoButton.addActionListener(e -> showPlatoScreen());

        JButton menuButton = new JButton("Gestión de Menús");
        menuButton.addActionListener(e -> showMenuScreen());

        JButton reservaButton = new JButton("Gestión de Reservas");
        reservaButton.addActionListener(e -> showReservaScreen());

        panel.add(restauranteButton);
        panel.add(chefButton);
        panel.add(platoButton);
        panel.add(menuButton);
        panel.add(reservaButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    /**
     * Obtiene una conexión a la base de datos.
     *
     * @return La conexión a la base de datos.
     * @throws SQLException Si ocurre un error al conectarse a la base de datos.
     */

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
        String url = "jdbc:mysql://localhost:3306/ejerciciofinal";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Muestra la pantalla de gestión de restaurantes.
     */
    public static void showRestauranteScreen() {
        JFrame frame = new JFrame("Gestión de Restaurantes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        viewRestaurantes(panel);

        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreField = new JTextField();

        JLabel direccionLabel = new JLabel("Dirección:");
        JTextField direccionField = new JTextField();

        JLabel telefonoLabel = new JLabel("Teléfono:");
        JTextField telefonoField = new JTextField();

        JButton addButton = new JButton("Añadir Restaurante");
        addButton.addActionListener(e -> {
            addRestaurante(nombreField.getText(), direccionField.getText(),
                    telefonoField.getText());

            // Limpiar el panel
            panel.removeAll();
            panel.revalidate();
            panel.repaint();

            // Mostrar la lista de restaurantes actualizada
            viewRestaurantes(panel);

            // Limpiar los campos de entrada
            nombreField.setText("");
            direccionField.setText("");
            telefonoField.setText("");
        });

        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(direccionLabel);
        panel.add(direccionField);
        panel.add(telefonoLabel);
        panel.add(telefonoField);
        panel.add(addButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    /**
     * Añade un nuevo restaurante a la base de datos.
     *
     * @param nombre    El nombre del restaurante.
     * @param direccion La dirección del restaurante.
     * @param telefono  El teléfono del restaurante.
     */
    public static void addRestaurante(String nombre, String direccion, String telefono) {
        if (nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = getConnection()) {
            String query = "INSERT INTO restaurante (nombre, direccion, telefono) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.setString(3, telefono);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Restaurante añadido correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo añadir el restaurante", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra la lista de restaurantes en el panel dado.
     *
     * @param panel El panel donde se mostrarán los restaurantes.
     */
    public static void viewRestaurantes(JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM restaurante";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String direccion = resultSet.getString("direccion");
                String telefono = resultSet.getString("telefono");

                JLabel restauranteLabel = new JLabel("Nombre: " + nombre + ", Dirección: " + direccion
                        + ", Teléfono: " + telefono);
                panel.add(restauranteLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo obtener los restaurantes", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra la pantalla de gestión de chefs.
     */
    public static void showChefScreen() {
        JFrame frame = new JFrame("Gestión de Chefs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        viewChefs(panel);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreField = new JTextField();

        JLabel especialidadLabel = new JLabel("Especialidad:");
        JTextField especialidadField = new JTextField();

        JLabel restauranteIdLabel = new JLabel("ID del Restaurante:");
        JTextField restauranteIdField = new JTextField();

        JButton addButton = new JButton("Añadir Chef");
        addButton.addActionListener(e -> {
            addChef(nombreField.getText(), especialidadField.getText(),
                    Integer.parseInt(restauranteIdField.getText()));

            panel.removeAll();
            panel.revalidate();
            panel.repaint();

            viewChefs(panel);

            nombreField.setText("");
            especialidadField.setText("");
            restauranteIdField.setText("");
        });

        inputPanel.add(nombreLabel);
        inputPanel.add(nombreField);
        inputPanel.add(especialidadLabel);
        inputPanel.add(especialidadField);
        inputPanel.add(restauranteIdLabel);
        inputPanel.add(restauranteIdField);
        inputPanel.add(addButton);

        panel.add(inputPanel);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    /**
     * Muestra la lista de chefs en el panel dado.
     *
     * @param panel El panel donde se mostrarán los chefs.
     */
    public static void viewChefs(JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM chef";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String especialidad = resultSet.getString("especialidad");
                int restauranteId = resultSet.getInt("restaurante_id");

                JLabel chefLabel = new JLabel("Nombre: " + nombre + ", Especialidad: " + especialidad
                        + ", ID del Restaurante: " + restauranteId);
                panel.add(chefLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo obtener los chefs", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Añade un nuevo chef a la base de datos.
     *
     * @param nombre        El nombre del chef.
     * @param especialidad  La especialidad del chef.
     * @param restauranteId El ID del restaurante al que pertenece el chef.
     */
    public static void addChef(String nombre, String especialidad, int restauranteId) {
        if (nombre.isEmpty() || especialidad.isEmpty() || restauranteId <= 0) {
            JOptionPane.showMessageDialog(null,
                    "Todos los campos son obligatorios y el ID del Restaurante debe ser positivo", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = getConnection()) {
            String query = "INSERT INTO chef (nombre, especialidad, restaurante_id) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombre);
            stmt.setString(2, especialidad);
            stmt.setInt(3, restauranteId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Chef añadido correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo añadir el chef", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra la pantalla de gestión de platos.
     */
    public static void showPlatoScreen() {
        JFrame frame = new JFrame("Gestión de Platos");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        viewPlatos(panel);

        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreField = new JTextField();

        JLabel tipoLabel = new JLabel("Tipo:");
        JComboBox<String> tipoComboBox = new JComboBox<>(new String[] { "Entrante", "Principal", "Postre" });

        JLabel precioLabel = new JLabel("Precio:");
        JTextField precioField = new JTextField();

        JLabel restauranteIdLabel = new JLabel("ID del Restaurante:");
        JTextField restauranteIdField = new JTextField();

        JButton addButton = new JButton("Añadir Plato");
        addButton.addActionListener(e -> addPlato(nombreField.getText(), (String) tipoComboBox.getSelectedItem(),
                Double.parseDouble(precioField.getText()), Integer.parseInt(restauranteIdField.getText()), panel));

        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(tipoLabel);
        panel.add(tipoComboBox);
        panel.add(precioLabel);
        panel.add(precioField);
        panel.add(restauranteIdLabel);
        panel.add(restauranteIdField);
        panel.add(addButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    /**
     * Muestra la lista de platos en el panel dado.
     *
     * @param panel El panel donde se mostrarán los platos.
     */
    public static void addPlato(String nombre, String tipo, double precio, int restauranteId, JPanel panel) {
        if (nombre.isEmpty() || tipo.isEmpty() || precio <= 0 || restauranteId <= 0) {
            JOptionPane.showMessageDialog(null,
                    "Todos los campos son obligatorios, el precio debe ser positivo y el ID del Restaurante debe ser positivo",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = getConnection()) {
            String query = "INSERT INTO plato (nombre, tipo, precio, restauranteId) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombre);
            stmt.setString(2, tipo);
            stmt.setDouble(3, precio);
            stmt.setInt(4, restauranteId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Plato añadido correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            viewPlatos(panel);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo añadir el plato", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Añade un nuevo plato a la base de datos.
     *
     * @param nombre      El nombre del plato.
     * @param descripcion La descripción del plato.
     * @param precio      El precio del plato.
     * @param chefId      El ID del chef que creó el plato.
     */
    public static void viewPlatos(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM plato";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String tipo = resultSet.getString("tipo");
                double precio = resultSet.getDouble("precio");
                int restauranteId = resultSet.getInt("restauranteId");

                JLabel platoLabel = new JLabel("Nombre: " + nombre + ", Tipo: " + tipo + ", Precio: " + precio
                        + ", Restaurante ID: " + restauranteId);
                panel.add(platoLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo obtener los platos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Muestra la pantalla de gestión de menús.
     */
    public static void showMenuScreen() {
        JFrame frame = new JFrame("Gestión de Menús");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        viewMenus(panel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreField = new JTextField();

        JLabel plato1IdLabel = new JLabel("ID del Plato 1:");
        JTextField plato1IdField = new JTextField();

        JLabel plato2IdLabel = new JLabel("ID del Plato 2:");
        JTextField plato2IdField = new JTextField();

        JLabel precioRebajadoLabel = new JLabel("Precio Rebajado:");
        JTextField precioRebajadoField = new JTextField();

        JLabel restauranteIdLabel = new JLabel("ID del Restaurante:");
        JTextField restauranteIdField = new JTextField();

        JButton addButton = new JButton("Añadir Menú");
        addButton.addActionListener(e -> addMenu(nombreField.getText(), Integer.parseInt(plato1IdField.getText()),
                Integer.parseInt(plato2IdField.getText()), Double.parseDouble(precioRebajadoField.getText()),
                Integer.parseInt(restauranteIdField.getText())));

        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(plato1IdLabel);
        panel.add(plato1IdField);
        panel.add(plato2IdLabel);
        panel.add(plato2IdField);
        panel.add(precioRebajadoLabel);
        panel.add(precioRebajadoField);
        panel.add(restauranteIdLabel);
        panel.add(restauranteIdField);
        panel.add(addButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

    }

    /**
     * Muestra la lista de menús en el panel dado.
     *
     * @param panel El panel donde se mostrarán los menús.
     */
    public static void viewMenus(JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM menu";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                int plato1Id = resultSet.getInt("plato1Id");
                int plato2Id = resultSet.getInt("plato2Id");
                double precioRebajado = resultSet.getDouble("precioRebajado");
                int restauranteId = resultSet.getInt("restauranteId");

                JLabel menuLabel = new JLabel("Nombre: " + nombre + ", Plato 1 ID: " + plato1Id + ", Plato 2 ID: "
                        + plato2Id + ", Precio Rebajado: " + precioRebajado + ", Restaurante ID: " + restauranteId);
                panel.add(menuLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo obtener los menús", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Añade un nuevo menú a la base de datos.
     *
     * @param nombre        El nombre del menú.
     * @param descripcion   La descripción del menú.
     * @param restauranteId El ID del restaurante al que pertenece el menú.
     */
    public static void addMenu(String nombre, int plato1Id, int plato2Id, double precioRebajado,
            int restauranteId) {
        if (nombre.isEmpty() || plato1Id <= 0 || plato2Id <= 0 || precioRebajado <= 0 || restauranteId <= 0) {
            JOptionPane.showMessageDialog(null,
                    "Todos los campos son obligatorios, los IDs y el precio deben ser positivos", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = getConnection()) {
            String query = "INSERT INTO menu (nombre, plato1_id, plato2_id, precio_rebajado, restaurante_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombre);
            stmt.setInt(2, plato1Id);
            stmt.setInt(3, plato2Id);
            stmt.setDouble(4, precioRebajado);
            stmt.setInt(5, restauranteId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Menú añadido correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo añadir el menú", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra la pantalla de gestión de reservas.
     */
    public static void showReservaScreen() {
        JFrame frame = new JFrame("Gestión de Reservas");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        viewReservas(panel);

        JLabel fechaLabel = new JLabel("Fecha:");
        JTextField fechaField = new JTextField();

        JLabel clienteNombreLabel = new JLabel("Nombre del Cliente:");
        JTextField clienteNombreField = new JTextField();

        JLabel clienteTelefonoLabel = new JLabel("Teléfono del Cliente:");
        JTextField clienteTelefonoField = new JTextField();

        JLabel restauranteIdLabel = new JLabel("ID del Restaurante:");
        JTextField restauranteIdField = new JTextField();

        JButton addButton = new JButton("Añadir Reserva");
        addButton.addActionListener(e -> addReserva(fechaField.getText(), clienteNombreField.getText(),
                clienteTelefonoField.getText(), Integer.parseInt(restauranteIdField.getText()), panel));

        panel.add(fechaLabel);
        panel.add(fechaField);
        panel.add(clienteNombreLabel);
        panel.add(clienteNombreField);
        panel.add(clienteTelefonoLabel);
        panel.add(clienteTelefonoField);
        panel.add(restauranteIdLabel);
        panel.add(restauranteIdField);
        panel.add(addButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    /**
     * Muestra la lista de reservas en el panel dado.
     *
     * @param panel El panel donde se mostrarán las reservas.
     */
    public static void viewReservas(JPanel panel) {
        panel.removeAll(); // Limpiar el panel antes de añadir las nuevas reservas
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM reserva";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String fecha = resultSet.getString("fecha");
                String clienteNombre = resultSet.getString("clienteNombre");
                String clienteTelefono = resultSet.getString("clienteTelefono");
                int restauranteId = resultSet.getInt("restauranteId");

                JLabel reservaLabel = new JLabel("Fecha: " + fecha + ", Nombre del cliente: " + clienteNombre
                        + ", Teléfono del cliente: " + clienteTelefono + ", Restaurante ID: " + restauranteId);
                panel.add(reservaLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo obtener las reservas", "Error", JOptionPane.ERROR_MESSAGE);
        }
        panel.revalidate(); // Revalidar el panel después de añadir las nuevas reservas
        panel.repaint(); // Repintar el panel después de añadir las nuevas reservas
    }

    /**
     * Añade una nueva reserva a la base de datos.
     *
     * @param clienteId     El ID del cliente que realiza la reserva.
     * @param restauranteId El ID del restaurante donde se realiza la reserva.
     * @param fecha         La fecha de la reserva.
     * @param hora          La hora de la reserva.
     */
    public static void addReserva(String fecha, String clienteNombre, String clienteTelefono, int restauranteId,
            JPanel panel) {
        if (fecha.isEmpty() || clienteNombre.isEmpty() || clienteTelefono.isEmpty() || restauranteId <= 0) {
            JOptionPane.showMessageDialog(null,
                    "Todos los campos son obligatorios y el ID del Restaurante debe ser positivo",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = getConnection()) {
            String query = "INSERT INTO reserva (fecha, clienteNombre, clienteTelefono, restauranteId) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, fecha);
            stmt.setString(2, clienteNombre);
            stmt.setString(3, clienteTelefono);
            stmt.setInt(4, restauranteId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Reserva añadida correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            viewReservas(panel); // Actualizar la vista después de añadir una reserva
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo añadir la reserva", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
