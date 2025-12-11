package com.practice.server.application.service;

import com.practice.server.application.model.entity.Reservation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email enviado a {}", to);
        } catch (MessagingException e) {
            log.error("Error al enviar email a {}: {}", to, e.getMessage(), e);
        }
    }

    public String buildReservationEmail(String guestName, Reservation reservation) {
        return """
    <html>
      <body style="margin:0; padding:0; font-family: Arial, sans-serif; background-color:#f4f4f4;">
        <table role="presentation" cellpadding="0" cellspacing="0" width="100%%" style="padding:20px 0;">
          <tr>
            <td align="center">
              <table role="presentation" cellpadding="0" cellspacing="0" width="600" style="background-color:#ffffff; border-radius:8px; overflow:hidden;">
                <tr>
                  <td style="padding:30px; text-align:left; color:#333333;">
                    <h2 style="color:#5fa8ff; margin-top:0;">Hola %s 👋</h2>
                    <p>Gracias por tu reserva en <strong>%s</strong>.</p>
                    <p><strong>Detalles de la reserva:</strong></p>
                    <ul>
                      <li>Habitación: %s</li>
                      <li>Check-in: %s</li>
                      <li>Check-out: %s</li>
                      <li>Huéspedes: %d</li>
                      <li>Total: %.2f €</li>
                      <li>Código de confirmación: <strong>%s</strong></li>
                    </ul>
                    <p>Con este código podrás consultar o modificar tu reserva.</p>
                  </td>
                </tr>
                <tr>
                  <td style="background-color:#f9f9f9; padding:15px; text-align:center; font-size:12px; color:#888888;">
                    Si no realizaste esta reserva, ignora este mensaje.
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </body>
    </html>
    """.formatted(
                guestName,
                reservation.getHotel().getName(),
                reservation.getRoom().getRoomNumber(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getGuests(),
                reservation.getTotalPrice(),
                reservation.getConfirmationCode()
        );
    }


}
