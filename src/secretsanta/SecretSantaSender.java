/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package secretsanta;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Braydon
 */
public class SecretSantaSender
{
    // Elf Names
    // http://christmas.namegeneratorfun.com/
    public static final String[][] INFO =
    {
        {"Name", "email@domain.com", "Elf Name"}
    };
    
    public static void main(String[] args) throws IOException
    {
        
        
        int p = INFO.length;
        
        boolean stop = false;
        
        int[] taken = new int[p];
            
        while(!stop)
        {
            System.out.println("Pick Names");
            Arrays.fill(taken, -1);
            int matched = 0;
            Random generator = new Random(new Date().getTime());
            int to = generator.nextInt(p);
            
            for(int from = 0; from < p; from++)
            {
                if(matched == p-1)
                {
                    if(taken[p-1] == -1)
                    {
                        break;
                    }
                }
                
                while(to == from || taken[to] != -1)
                {
                    to = generator.nextInt(p);
                }
                taken[to] = from;
                matched++;
                if(matched == p)
                    stop = true;
                to = generator.nextInt(p);
            }
        }
            System.out.println("Names Picked");

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
            String reciever = INFO[taken[j]][0];
            String elf = INFO[j][2];
            String elfEmail = elf.replace(" ", "") + "@Northpole.ca";

            try
            {

                Message message = new MimeMessage(session);
                Multipart multiPart = new MimeMultipart("alternative");
                
                message.setFrom(new InternetAddress(elfEmail, elf));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(giverEmail));
                message.setSubject("[Secret Santa Information]");

                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(parser(
                        "Happy Holidays " + giver + ".<br><br>" +
                                
                        "It's " + elf + " again, your holiday elf<br>" +
                        "Elves all over the North Pole are sending out request for people to help Santa this year<br>" +
                        "You are one of the lucky chosen to help spread holiday cheer to those around you<br>" +
                        "This Christams you will be <b>" + reciever + "'s</b> Secret Santa<br><br>" +
                                
                        "Have a <b>Merry Christmas</b> and a <b>Happy New Year</b><br>" +
                        "Sincerly " + elf),
                        "text/html; charset=utf-8");

                multiPart.addBodyPart(htmlPart);
                message.setContent(multiPart);

                Transport.send(message);

                System.out.println("Sent To: " + giver);

            } catch (MessagingException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static String parser(String orig)
    {
        String red = "<font color='red'>";
        String green = "<font color='green'>";
        String blue = "<font color='blue'>";
        String color[] = {red, green, blue};
        int c = 0;
        String end = "</font>";
        
        String out = "";
        
        String[] split = orig.split(" ");
        
        for (String split1 : split)
        {
            out += color[c] + split1 + end + " ";
            c = (c+1)%3;
        }
        
        return out;
    }
}
