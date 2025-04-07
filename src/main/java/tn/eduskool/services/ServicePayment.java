package tn.eduskool.services;
import tn.eduskool.entities.Payment;
import tn.eduskool.tools.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class ServicePayment {

    // Créer un paiement
    public static boolean createPayment(String stripeSessionId, double amount, LocalDateTime paymentDate,
                                        String paymentMethod, String status, String cardNumber,
                                        String cardExpiration, String cardCVV) {
        String sql = "INSERT INTO payment (stripe_session_id, amount, payment_date, payment_method, status, card_number, card_expiration, card_cvv) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stripeSessionId);
            stmt.setDouble(2, amount);
            stmt.setTimestamp(3, Timestamp.valueOf(paymentDate));
            stmt.setString(4, paymentMethod);
            stmt.setString(5, status);
            stmt.setString(6, cardNumber);
            stmt.setString(7, cardExpiration);
            stmt.setString(8, cardCVV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupérer tous les paiements
    public static List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payment";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Payment p = new Payment(
                        rs.getInt("id"),
                        rs.getString("stripe_session_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("payment_date").toLocalDateTime(),
                        rs.getString("payment_method"),
                        rs.getString("status"),
                        rs.getString("card_number"),
                        rs.getString("card_expiration"),
                        rs.getString("card_cvv")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Mettre à jour un paiement
    public static boolean updatePayment(int id, String stripeSessionId, double amount, LocalDateTime paymentDate,
                                        String paymentMethod, String status, String cardNumber,
                                        String cardExpiration, String cardCVV) {
        String sql = "UPDATE payment SET stripe_session_id = ?, amount = ?, payment_date = ?, payment_method = ?, status = ?, card_number = ?, card_expiration = ?, card_cvv = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stripeSessionId);
            stmt.setDouble(2, amount);
            stmt.setTimestamp(3, Timestamp.valueOf(paymentDate));
            stmt.setString(4, paymentMethod);
            stmt.setString(5, status);
            stmt.setString(6, cardNumber);
            stmt.setString(7, cardExpiration);
            stmt.setString(8, cardCVV);
            stmt.setInt(9, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un paiement
    public static boolean deletePayment(int id) {
        String sql = "DELETE FROM payment WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
