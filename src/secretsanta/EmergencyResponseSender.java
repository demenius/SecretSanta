/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package secretsanta;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import static secretsanta.SecretSantaSender.parser;
import static secretsanta.SecretSantaSender.INFO;

/**
 *
 * @author Braydon
 */
public class EmergencyResponseSender
{

    public static void main(String[] args) throws IOException
    {
        int p = INFO.length;

        for (int j = 0; j < p; j++)
        {
            Properties props = new Properties();
            props.put("mail.smtp.host", "shawmail.gv.shawcable.net");
            props.put("mail.smtp.socketFactory.port", "25");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.port", "25");

            Session session = Session.getInstance(props);
            
            String giverEmail = INFO[j][1];
             String giver = INFO[j][0];
             String elf = INFO[j][2];
             String elfEmail = elf.replace(" ", "") + "@Northpole.ca";

            try
            {

                Message message = new MimeMessage(session);
                Multipart multiPart = new MimeMultipart("alternative");
                
                message.setFrom(new InternetAddress(elfEmail, elf));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(giverEmail));
                message.setSubject("[Emergency Response Assessment Transmission]");

                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(parser(
                        "Happy Holidays " + giver + ".<br><br>" +
                        "I am " + elf + ", your holiday elf<br>" +
                        "This is an Emergency Response Assessment Transmission<br>" +
                        "I am sending this transmission to make sure it is going to your inbox and not your junk mail<br>" +
                        "If it is found in your junk mail mark it as safe to allow further transmissions through without fear of losing them<br>" +
                        "Thank you<br><br>" +
                        "Have a <b>Merry Christmas</b> and a <b>Happy New Year</b><br>" +
                        "Sincerly " + elf),
                        "text/html; charset=utf-8");

                multiPart.addBodyPart(htmlPart);
                message.setContent(multiPart);

                Transport.send(message);

                System.out.println("Done: " + giver);

            } catch (MessagingException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
    
    
}
