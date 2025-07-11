package Projectpp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Projectpp extends JFrame {
    private JTextArea displayArea;
    private Map<String, String> vehicles; // Key: carId, Value: model
    private Map<String, Double> vehicleRates; // Key: carId, Value: hourly rate
    private List<Booking> bookings;
    private Color bgColor = new Color(240, 248, 255); // AliceBlue background
    private Color buttonColor = new Color(70, 130, 180); // SteelBlue buttons
    private Color textColor = new Color(25, 25, 112); // MidnightBlue text

    // Inner class to represent bookings
    private class Booking {
        String username;
        int hours;
        String carId;
        double totalCost;
        double discountApplied;

        public Booking(String username, int hours, String carId, double totalCost, double discountApplied) {
            this.username = username;
            this.hours = hours;
            this.carId = carId;
            this.totalCost = totalCost;
            this.discountApplied = discountApplied;
        }
    }

    public Projectpp() {
        setTitle("🚗 Vehicle Rental System 🚙");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgColor);

        // Create a header panel with image and title
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main display area with styling
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        displayArea.setForeground(textColor);
        displayArea.setBackground(new Color(255, 255, 240)); // Ivory background
        displayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Information"));
        add(scrollPane, BorderLayout.CENTER);

        // Button panel with styled buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Initialize data structures
        vehicles = new HashMap<>();
        vehicleRates = new HashMap<>();
        bookings = new ArrayList<>();

        // Add dummy vehicles with rates
        initializeDummyVehicles();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title label with styling
        JLabel titleLabel = new JLabel("🚗 Vehicle Rental System 🚙", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Add some decorative elements
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        iconPanel.setBackground(bgColor);
        iconPanel.add(createIconLabel("🚘"));
        iconPanel.add(createIconLabel("🚖"));
        iconPanel.add(createIconLabel("🚔"));
        headerPanel.add(iconPanel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JLabel createIconLabel(String icon) {
        JLabel label = new JLabel(icon);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        return label;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(bgColor);

        JButton viewBtn = createStyledButton("View Vehicles", "🔍");
        JButton bookBtn = createStyledButton("Book Vehicle", "📝");
        JButton historyBtn = createStyledButton("Booking History", "📋");
        JButton discountBtn = createStyledButton("Discount Info", "💰");
        JButton exitBtn = createStyledButton("Exit", "🚪");

        // Button actions
        viewBtn.addActionListener(e -> viewVehicles());
        bookBtn.addActionListener(e -> bookVehicle());
        historyBtn.addActionListener(e -> showHistory());
        discountBtn.addActionListener(e -> showDiscountInfo());
        exitBtn.addActionListener(e -> dispose());

        buttonPanel.add(viewBtn);
        buttonPanel.add(bookBtn);
        buttonPanel.add(historyBtn);
        buttonPanel.add(discountBtn);
        buttonPanel.add(exitBtn);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, String emoji) {
        JButton button = new JButton(emoji + " " + text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(buttonColor.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(buttonColor);
            }
        });

        return button;
    }

    private void initializeDummyVehicles() {
        String[] carModels = {
                "Toyota Camry", "Honda Civic", "Ford Mustang", "Chevrolet Tahoe",
                "BMW X5", "Mercedes C-Class", "Audi A4", "Tesla Model 3",
                "Jeep Wrangler", "Subaru Outback", "Volkswagen Golf", "Hyundai Sonata",
                "Kia Sportage", "Nissan Altima", "Mazda CX-5"
        };

        // Assign hourly rates to each vehicle (in dollars)
        double[] rates = {
                25.0, 20.0, 45.0, 50.0,  // Economy to Luxury SUVs
                60.0, 55.0, 50.0, 70.0,   // Premium vehicles
                40.0, 35.0, 30.0, 25.0,   // Mid-range vehicles
                30.0, 25.0, 35.0          // More options
        };

        for (int i = 0; i < 15; i++) {
            String carId = "C" + (i+1);
            vehicles.put(carId, carModels[i]);
            vehicleRates.put(carId, rates[i]);
        }
    }

    private void viewVehicles() {
        displayArea.setText("");
        displayArea.append("══════════════════════════════════════════\n");
        displayArea.append("          AVAILABLE VEHICLES\n");
        displayArea.append("══════════════════════════════════════════\n\n");

        for (Map.Entry<String, String> entry : vehicles.entrySet()) {
            String carId = entry.getKey();
            displayArea.append(" 🚗 Car ID: " + carId +
                    "\n   Model: " + entry.getValue() +
                    "\n   Rate: $" + vehicleRates.get(carId) + "/hour\n");
            displayArea.append("------------------------------------------\n");
        }
    }

    private void bookVehicle() {
        // Create a styled panel for the booking form
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(textColor);
        JTextField usernameField = new JTextField();

        JLabel hoursLabel = new JLabel("Rental Hours:");
        hoursLabel.setForeground(textColor);
        JTextField hoursField = new JTextField();

        JLabel carLabel = new JLabel("Car ID (e.g. C1):");
        carLabel.setForeground(textColor);
        JTextField carIdField = new JTextField();

        JLabel discountLabel = new JLabel("Discount Code (optional):");
        discountLabel.setForeground(textColor);
        JTextField discountField = new JTextField();

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(hoursLabel);
        panel.add(hoursField);
        panel.add(carLabel);
        panel.add(carIdField);
        panel.add(discountLabel);
        panel.add(discountField);

        int result = JOptionPane.showConfirmDialog(this, panel, "📋 Book a Vehicle",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String hoursStr = hoursField.getText().trim();
            String carId = carIdField.getText().trim().toUpperCase();
            String discountCode = discountField.getText().trim();

            if (username.isEmpty() || hoursStr.isEmpty() || carId.isEmpty()) {
                showError("Username, hours and car ID are required!");
                return;
            }

            try {
                int hours = Integer.parseInt(hoursStr);
                if (!vehicles.containsKey(carId)) {
                    showError("Invalid Car ID. Please check available vehicles.");
                    return;
                }

                if (hours <= 0) {
                    showError("Please enter a positive number for rental hours.");
                    return;
                }

                // Calculate base cost
                double hourlyRate = vehicleRates.get(carId);
                double baseCost = hourlyRate * hours;
                double discount = calculateDiscount(discountCode, hours, baseCost);
                double totalCost = baseCost - discount;

                // Create and store booking
                bookings.add(new Booking(username, hours, carId, totalCost, discount));

                // Show success message with details
                StringBuilder confirmation = new StringBuilder();
                confirmation.append("✅ Vehicle booked successfully!\n\n");
                confirmation.append("Car: ").append(vehicles.get(carId)).append("\n");
                confirmation.append("Hours: ").append(hours).append("\n");
                confirmation.append("Hourly Rate: $").append(hourlyRate).append("\n");
                confirmation.append("Base Cost: $").append(String.format("%.2f", baseCost)).append("\n");

                if (discount > 0) {
                    confirmation.append("Discount Applied: $").append(String.format("%.2f", discount)).append("\n");
                    confirmation.append("Discount Code: ").append(discountCode).append("\n");
                }

                confirmation.append("Total Cost: $").append(String.format("%.2f", totalCost)).append("\n\n");
                confirmation.append("Thank you, ").append(username).append("!");

                JOptionPane.showMessageDialog(this,
                        confirmation.toString(),
                        "Booking Confirmation",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException e) {
                showError("Please enter a valid number for rental hours.");
            }
        }
    }

    private double calculateDiscount(String discountCode, int hours, double baseCost) {
        if (discountCode.isEmpty()) {
            return 0.0;
        }

        // Check for valid discount codes
        switch (discountCode.toUpperCase()) {
            case "WELCOME10":
                return baseCost * 0.10; // 10% discount
            case "WEEKEND20":
                return baseCost * 0.20; // 20% discount on weekends
            case "LONGTERM":
                if (hours >= 24) {
                    return baseCost * 0.15; // 15% discount for rentals >= 24 hours
                }
                break;
            case "FIRST5":
                return Math.min(50.0, baseCost * 0.05); // 5% discount, max $50
            default:
                showError("Invalid discount code. Proceeding without discount.");
        }

        return 0.0;
    }

    private void showDiscountInfo() {
        String discountInfo = "💰 AVAILABLE DISCOUNT CODES 💰\n\n" +
                "1. WELCOME10 - 10% off your first booking\n" +
                "2. WEEKEND20 - 20% off for weekend rentals\n" +
                "3. LONGTERM - 15% off for rentals of 24+ hours\n" +
                "4. FIRST5 - 5% off (max $50)\n\n" +
                "Note: Only one discount code can be applied per booking.";

        JOptionPane.showMessageDialog(this, discountInfo, "Discount Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showHistory() {
        displayArea.setText("");
        displayArea.append("══════════════════════════════════════════\n");
        displayArea.append("          BOOKING HISTORY\n");
        displayArea.append("══════════════════════════════════════════\n\n");

        if (bookings.isEmpty()) {
            displayArea.append("No bookings yet. Be the first to book a vehicle!\n");
            return;
        }

        for (Booking booking : bookings) {
            displayArea.append(" 👤 User: " + booking.username + "\n");
            displayArea.append(" 🚗 Car: " + booking.carId + " (" + vehicles.get(booking.carId) + ")\n");
            displayArea.append(" ⏱ Hours: " + booking.hours + "\n");
            if (booking.discountApplied > 0) {
                displayArea.append(" 💰 Discount: $" + String.format("%.2f", booking.discountApplied) + "\n");
            }
            displayArea.append(" 💵 Total Cost: $" + String.format("%.2f", booking.totalCost) + "\n");
            displayArea.append("------------------------------------------\n");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                "❌ " + message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Projectpp frame = new Projectpp();
            frame.setVisible(true);
        });
    }
}