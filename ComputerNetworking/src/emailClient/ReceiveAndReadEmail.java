package emailClient;

import com.sun.mail.util.MailSSLSocketFactory;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.mail.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.util.Properties;
import java.util.Date;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


public class ReceiveAndReadEmail extends JFrame{

    private Socket socket = null;


    private JPanel contentPane;
    //private JTextField textField;
    public static void parseMessage(Message[] messages)throws MessagingException,IOException{
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");

        int cnt_mess = messages.length;
        for (int i = 0; i < Math.min(cnt_mess,10); i++){
            MimeMessage msg = (MimeMessage)messages[i];
            System.out.println("Subject: "+msg.getSubject());
            System.out.println("From: "+msg.getFrom());
           // StringBuffer content = new StringBuffer(300);
           // content = (StringBuffer)msg.getContent();
           // System.out.println("Content: "+content);
            System.out.println();
        }
    }

    public ReceiveAndReadEmail(String username, String password, String realm){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 498);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.activeCaption);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
    /*
        String server = "pop3.qq.com";
        try{
            socket = new Socket(server,110);

        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Connection to "+server+" established successfully!\n");
    */

        //open ssl-lock
        try {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();


                sf.setTrustAllHosts(true);

                Properties props = new Properties();
                props.setProperty("mail.store.protocol", "pop3");
                props.setProperty("mail.pop3.port", "995");
                //这里不能使用110端口因为不支持SSL
                props.setProperty("mail.pop3.host", "pop.qq.com");
                props.setProperty("mail.debug", "true");
                props.put("mail.pop3.ssl.enable", "true");
                props.put("mail.pop3.ssl.socketFactory", sf);

                //new a session instance;
                Session session = Session.getInstance(props);
                try {
                    Store store = session.getStore("pop3");
                    try{
                        store.connect(username, password);
                    }catch(Exception e){
                        e.printStackTrace();

                    }
                    //收件箱
                    try {
                        Folder folder = store.getFolder("INBOX");
                        if ( !folder.exists() ) System.exit(0);


                        folder.open(Folder.READ_WRITE);

                        System.out.println("unread: " + folder.getMessageCount());


                         Message[] messages = folder.getMessages();
                         parseMessage(messages);

            /*
            if(messages!=null&&messages.length>0) {
                for (Message message : messages) {
                    System.out.println(message.getSubject());
                }
            }
            */
                        folder.close(true);
                        store.close();
                    }catch(Exception e1){
                       e1.printStackTrace();
                    }
                }catch(NoSuchProviderException ex){
                    ex.printStackTrace();
                }
            }catch(GeneralSecurityException e){
                e.printStackTrace();
            }
    }


}
