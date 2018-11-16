package emailClient;

import com.sun.mail.util.MailSSLSocketFactory;

import json.JSONArray;
import json.JSONObject;

import com.sun.mail.util.QPDecoderStream;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.mail.*;
import javax.mail.util.ByteArrayDataSource;
import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.io.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Scanner;
import java.util.Date;
import java.util.Properties;
import java.awt.event.ActionEvent;
import java.util.Properties;
import java.util.Date;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

public class ReceiveAndReadEmail extends JFrame{

    private Socket socket = null;
    public int tot_email = 1;

    private JPanel contentPane;
    private JTextField textField;

    public ReceiveAndReadEmail(String username, String password, String realm){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 498);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.activeCaption);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);


        //JButton PageSelect = new JButton("Page");
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setText("Page : ");
        textPane.setFont(new Font("Consolas", Font.PLAIN, 15));
        textPane.setBackground(new Color(173, 216, 230));
        textPane.setBounds(10, 10, 85, 31);
        contentPane.add(textPane);



        //open ssl-lock
        try {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();


                sf.setTrustAllHosts(true);

                Properties props = new Properties();
                props.setProperty("mail.store.protocol", "pop3");
                props.setProperty("mail.pop3.port", "995");
                //这里不能使用110端口因为不支持SSL
                props.setProperty("mail.pop3.host", "pop.qq.com");
               // props.setProperty("mail.debug", "true");//debug mode
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

                        System.out.println("total number: " + folder.getMessageCount());


                        Message[] messages = folder.getMessages();
                        //int tot_mess = parseMessage(messages);
                        int tot_mess = messages.length;
                        String[] Number = new String[tot_mess];
                        for(int i = 0; i < tot_mess; i++){
                            Number[i] = Integer.toString(i + 1);
                        }
                        System.out.println("Total number 2: "+tot_mess);

                        JComboBox selPage = new JComboBox(Number);
                        selPage.setEditable(true);
                        selPage.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e){
                                int num_index = ((JComboBox)e.getSource()).getSelectedIndex();
                                System.out.println("Num index: "+num_index);
                                //Message[] temp_messages = new Message[1];
                                //temp_messages[0] = messages[tot_mess-1-num_index];
                                try{
                                    parseMessage(messages,num_index);
                                }catch(IOException ioex){
                                    ioex.printStackTrace();
                                }catch(MessagingException meex){
                                    meex.printStackTrace();
                                }

                            }
                        });
                        selPage.setFont(new Font("Consolas", Font.BOLD, 16));;
                        selPage.setBounds(125,10,85,40);
                        contentPane.add(selPage);

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


    public static void parseMessage(Message[] messages,int index) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");
        String DocuPath = "/Users/wangyiwen/Desktop/ComputerNetworking/wangyiwenemail/";
        File file = new File(DocuPath);
        if(!file.exists()){
            file.mkdir();
        }
        // 解析所有邮件
        int cnt_mess = messages.length;
        for(int i = index; i <= index; i++){
        //for (int i = index; i < Math.min(cnt_mess,20); i++){//最后是要全部解析的
            if(!messages[i].getFolder().isOpen()){
                messages[i].getFolder().open(Folder.READ_WRITE);
            }
            MimeMessage msg = (MimeMessage)messages[cnt_mess-1-i];//最新的邮件在最后面

            System.out.println("------------------解析第" + i + "封邮件-------------------- ");
            System.out.println("主题: " + getSubject(msg));
            System.out.println("发件人: " + getFrom(msg));
            System.out.println("收件人：" + getReceiveAddress(msg, null));
            System.out.println("发送时间：" + getSentDate(msg, null));
            System.out.println("是否已读：" + isSeen(msg));
            System.out.println("邮件优先级：" + getPriority(msg));
            System.out.println("是否需要回执：" + isReplySign(msg));
            System.out.println("邮件大小：" + msg.getSize() * 1024 + "kb");

            boolean isContainerAttachment = isContainAttachment(msg);


            System.out.println("是否包含附件：" + isContainerAttachment);
            if (isContainerAttachment) {
                saveAttachment(msg, DocuPath+msg.getSubject() + "_"+i+"_"); //保存附件
            }

            StringBuffer content = new StringBuffer(30);
          //  System.out.println("Text: "+MimeUtility.getEncoding(msg.getContent()));
            getMailTextContent(msg, content);
            String recordPath = DocuPath+i+"_mail.html";
            File fw = new File(recordPath);
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(fw));
                bw.write(content.toString());
                bw.flush();
                bw.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println("邮件正文：" + (content.length() > 100 ? content.substring(0,100) + "..." : content));
            System.out.println("------------------第" + i + "封邮件解析结束-------------------- ");
            System.out.println();

        }
        //return cnt_mess;
    }


    /**
     * 解析邮件
     * @param messages 要解析的邮件列表
     */
    public static void deleteMessage(Message ...messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");

        // 解析所有邮件
        for (int i = 0, count = messages.length; i < count; i++) {

            /**
             *   邮件删除
             */
            Message message = messages[i];
            String subject = message.getSubject();
            // set the DELETE flag to true
            message.setFlag(Flags.Flag.DELETED, true);
            System.out.println("Marked DELETE for message: " + subject);


        }
    }

    /**
     * 获得邮件主题
     * @param msg 邮件内容
     * @return 解码后的邮件主题
     */
    public static String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        return MimeUtility.decodeText(msg.getSubject());
    }

    /**
     * 获得邮件发件人
     * @param msg 邮件内容
     * @return 姓名 <Email地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        String from = "";
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("没有发件人!");

        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        from = person + "<" + address.getAddress() + ">";

        return from;
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>Message.RecipientType.TO  收件人</p>
     * <p>Message.RecipientType.CC  抄送</p>
     * <p>Message.RecipientType.BCC 密送</p>
     * @param msg 邮件内容
     * @param type 收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public static String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }

        if (addresss == null || addresss.length < 1)
            throw new MessagingException("没有收件人!");
        for (Address address : addresss) {
            InternetAddress internetAddress = (InternetAddress)address;
            receiveAddress.append(internetAddress.toUnicodeString()).append(",");
        }

        receiveAddress.deleteCharAt(receiveAddress.length()-1); //删除最后一个逗号

        return receiveAddress.toString();
    }

    /**
     * 获得邮件发送时间
     * @param msg 邮件内容
     * @return yyyy年mm月dd日 星期X HH:mm
     * @throws MessagingException
     */
    public static String getSentDate(MimeMessage msg, String pattern) throws MessagingException {
        Date receivedDate = msg.getSentDate();
        if (receivedDate == null)
            return "";

        if (pattern == null || "".equals(pattern))
            pattern = "yyyy年MM月dd日 E HH:mm ";

        return new SimpleDateFormat(pattern).format(receivedDate);
    }

    /**
     * 判断邮件中是否包含附件
     * @param msg 邮件内容
     * @return 邮件中存在附件返回true，不存在返回false
     * @throws MessagingException
     * @throws IOException
     */

    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            ByteArrayDataSource src = new ByteArrayDataSource(part.getInputStream(),"multipart/*");
            MimeMultipart multipart = new MimeMultipart(src);
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }

                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }

                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part)part.getContent());
        }
        return flag;
    }

    /**
     * 判断邮件是否已读
     * @param msg 邮件内容
     * @return 如果邮件已读返回true,否则返回false
     * @throws MessagingException
     */
    public static boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }

    /**
     * 判断邮件是否需要阅读回执
     * @param msg 邮件内容
     * @return 需要回执返回true,否则返回false
     * @throws MessagingException
     */
    public static boolean isReplySign(MimeMessage msg) throws MessagingException {
        boolean replySign = false;
        String[] headers = msg.getHeader("Disposition-Notification-To");
        if (headers != null)
            replySign = true;
        return replySign;
    }

    /**
     * 获得邮件的优先级
     * @param msg 邮件内容
     * @return 1(High):紧急  3:普通(Normal)  5:低(Low)
     * @throws MessagingException
     */
    public static String getPriority(MimeMessage msg) throws MessagingException {
        String priority = "普通";
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)
                priority = "紧急";
            else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)
                priority = "低";
            else
                priority = "普通";
        }
        return priority;
    }

    /**
     * 获得邮件文本内容
     * @param part 邮件体
     * @param content 存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     */
    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            InputStream is = part.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String thisline = reader.readLine();
            while(thisline != null){
                //System.out.println(thisline);
                content.append(thisline);
                thisline = reader.readLine();
            }

            //content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part)part.getContent(),content);
        } else if (part.isMimeType("multipart/*")) {
            //Multipart multipart = part.getContent();
           // Object temp = part.getContent();
           // String string = JSON.toJSON(temp).toString();
            ByteArrayDataSource src = new ByteArrayDataSource(part.getInputStream(),"multipart/*");
            Multipart multipart = new MimeMultipart(src);

            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart,content);
            }
        }
    }

    /**
     * 保存附件
     * @param part 邮件中多个组合体中的其中一个组合体
     * @param destDir  附件保存目录
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws FileNotFoundException
     * @throws IOException
     */

    public static void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            //Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            ByteArrayDataSource src = new ByteArrayDataSource(part.getInputStream(),"multipart/*");
            Multipart multipart = new MimeMultipart(src);
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart,destDir);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(),destDir);
        }
    }

    /**
     * 读取输入流中的数据保存至指定目录
     * @param is 输入流
     * @param fileName 文件名
     * @param destDir 文件存储目录
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void saveFile(InputStream is, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir + fileName)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    /**
     * 文本解码
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    public static String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }



}
