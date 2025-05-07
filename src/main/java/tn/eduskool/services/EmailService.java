package tn.eduskool.services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private static final String FROM_EMAIL = "somraniaymen81@gmail.com"; // Ton email
    private static final String PASSWORD = "kigkxdawpsteraga"; // Ton mot de passe d'application
    private static final String SMTP_HOST = "smtp.gmail.com"; // Host SMTP
    private static final int SMTP_PORT = 587; // Port SMTP

    public void envoyerEmail(String destinataire, String sujet, String messageTexte) {
        // Configurer les propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS

        // Créer une session authentifiée
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            // Créer l'email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setText(messageTexte);

            // Envoyer le message
            Transport.send(message);

            System.out.println("Email envoyé avec succès !");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi d'email.");
        }
    }
}