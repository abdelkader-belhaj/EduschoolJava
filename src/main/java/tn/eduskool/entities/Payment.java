package tn.eduskool.entities;

import java.time.LocalDateTime;

public class Payment {
    private int id;
    private String stripeSessionId;
    private double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String status;
    private String cardNumber;
    private String cardExpiration;
    private String cardCVV;

    public Payment() {
    }

    public Payment(double amount, LocalDateTime paymentDate, String paymentMethod) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    public Payment(int id, String stripeSessionId, double amount,
            LocalDateTime paymentDate, String paymentMethod, String status,
            String cardNumber, String cardExpiration, String cardCVV) {
        this.id = id;
        this.stripeSessionId = stripeSessionId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.cardNumber = cardNumber;
        this.cardExpiration = cardExpiration;
        this.cardCVV = cardCVV;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStripeSessionId() {
        return stripeSessionId;
    }

    public void setStripeSessionId(String stripeSessionId) {
        this.stripeSessionId = stripeSessionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiration() {
        return cardExpiration;
    }

    public void setCardExpiration(String cardExpiration) {
        this.cardExpiration = cardExpiration;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", stripeSessionId='" + stripeSessionId + '\'' +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardExpiration='" + cardExpiration + '\'' +
                ", cardCVV='" + cardCVV + '\'' +
                '}';
    }
}
