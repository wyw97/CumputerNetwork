package emailClient;

import com.sun.mail.util.MailSSLSocketFactory;

import json.JSONArray;
import json.JSONObject;

import com.sun.mail.util.QPDecoderStream;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Desktop;
import java.net.URI;
import javax.mail.*;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.util.SharedByteArrayInputStream;
import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

class EmailData{
    public String date;
    public String from;
    public String subject;
    public String content;
    public boolean hasAddict;
    EmailData(){}
    EmailData(String d, String f, String s, String c, boolean b){
        date = d;
        from = f;
        subject = s;
        content = c;
        hasAddict = b;
    }
    boolean JudgeOK(){
        return hasAddict;
    }

}
public class ReceiveAndReadEmail extends JFrame{

    private Socket socket = null;
    public int tot_email = 1;
    private int num_index = 0;
    //private JFrame f2;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField2;
    private JTextField textField3;
    private EmailData ED;
    private JWebBrowser webBrowser;
    private String url;



    public ReceiveAndReadEmail(String username, String password, String realm){
        String DocuPath = "/Users/wangyiwen/Desktop/ComputerNetworking/wangyiwenemail/";
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setBounds(100, 100, 450, 498);
        setBounds(100, 100, 1050, 898);

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


        textField = new JTextField();
        textField.setBounds(105, 51, 919, 31);
        contentPane.add(textField);

        textField.setColumns(10);

        JTextPane txtpnReceiver = new JTextPane();
        txtpnReceiver.setEditable(false);
        txtpnReceiver.setBackground(new Color(173, 216, 230));
        txtpnReceiver.setFont(new Font("Consolas", Font.PLAIN, 15));
        txtpnReceiver.setText("Date:");
        txtpnReceiver.setBounds(10, 51, 85, 31);
        //frame2.getContentPane().add(txtpnReceiver);
        contentPane.add(txtpnReceiver);

        JTextPane textPane2 = new JTextPane();
        textPane2.setEditable(false);
        textPane2.setText("From: ");
        textPane2.setFont(new Font("Consolas", Font.PLAIN, 15));
        textPane2.setBackground(new Color(173, 216, 230));
        textPane2.setBounds(10, 91, 85, 31);
        //frame2.getContentPane().add(textPane);
        contentPane.add(textPane2);

        textField2 = new JTextField();
        textField2.setBounds(105, 91, 919, 31);
        contentPane.add(textField2);

        textField2.setColumns(10);
        //parseMessage();

        JTextPane textPane3 = new JTextPane();
        textPane3.setEditable(false);
        textPane3.setText("Subject: ");
        textPane3.setFont(new Font("Consolas", Font.PLAIN, 15));
        textPane3.setBackground(new Color(173, 216, 230));
        textPane3.setBounds(10, 131, 85, 31);
        //frame2.getContentPane().add(textPane);
        contentPane.add(textPane3);

        textField3 = new JTextField();
        textField3.setBounds(105, 131, 919, 31);
        contentPane.add(textField3);

        textField3.setColumns(10);


        JTextPane txtpnContent = new JTextPane();
        txtpnContent.setEditable(false);
        txtpnContent.setText("Content:");
        txtpnContent.setFont(new Font("Consolas", Font.PLAIN, 15));
        txtpnContent.setBackground(new Color(173, 216, 230));
        txtpnContent.setBounds(10, 172, 62, 31);
        //frame2.getContentPane().add(txtpnContent);
        contentPane.add(txtpnContent);


        /*
        JTextArea textArea = new JTextArea();
        textArea.setBounds(10, 212, 414, 186);
        //frame2.getContentPane().add(textArea);
        contentPane.add(textArea);
        //textArea.setColumns(10);
        */

        JEditorPane editorPane = new JEditorPane();
        //String pathloc = "http://127.0.0.1:"+DocuPath + num_index + "/mail.html";
        //String pathloc = "http://www.baidu.com";
        String pathloc = "file:///Users/wangyiwen/Desktop/ComputerNetworking/wangyiwenemail/"+num_index+"/mail.html";
        editorPane.setEditable(false);

        try{
            editorPane.setPage(pathloc);
            editorPane.setBounds(10, 212, 819, 500);
        }catch(Exception ee){
            ee.printStackTrace();
        }
        contentPane.add(editorPane);




        JButton btnCansel = new JButton("Back");
        btnCansel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                System.out.println("Close");
                dispose();
                //contentPane.dispatchEvent(contentPane,WindowEvent.WINDOW_CLOSING);
                //contentPane.disPose();
                //SelectPage selpage = new SelectPage(username,password,realm);
                //selpage.setVisible(true);


            }
        });
        btnCansel.setFont(new Font("Consolas", Font.BOLD, 16));
        btnCansel.setBounds(808, 750, 116, 38);
        //frame2.getContentPane().add(btnCansel);
        contentPane.add(btnCansel);


        JButton btnCansel2 = new JButton("Attchment");
        btnCansel2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(ED.JudgeOK()){
                    //btnCansel2.setBackground(Color.red);
                    Desktop desk=Desktop.getDesktop();
                    try {
                        // URI path = new URI("http://www.pku.edu.cn");
                        File path = new File(DocuPath + num_index + "/");
                        desk.open(path);
                    }catch(Exception ee){
                        ee.printStackTrace();
                    }

                }
                else{
                    btnCansel2.setForeground(Color.CYAN);
                }

            }
        });
        btnCansel2.setFont(new Font("Consolas", Font.BOLD, 16));
        btnCansel2.setBounds(38, 750, 146, 38);
        //frame2.getContentPane().add(btnCansel);
        contentPane.add(btnCansel2);

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

                        System.out.println("Parsing messages...");
                        ED = parseMessage(messages, 0, 1, DocuPath);

                        textField.setText(ED.date);
                        textField2.setText(ED.from);
                        textField3.setText(ED.subject);
                        //textArea.setText(ED.content);

                        System.out.println("End of parsing!");

                        JComboBox selPage = new JComboBox(Number);
                        selPage.setEditable(true);
                        selPage.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e){




                                num_index = ((JComboBox)e.getSource()).getSelectedIndex();
                                System.out.println("Num index: "+num_index);
                                try{
                                    ED = parseMessage(messages, num_index, num_index+ 1, DocuPath);
                                    textField.setText(ED.date);
                                    textField2.setText(ED.from);
                                    textField3.setText(ED.subject);
                                    //textArea.setText(ED.content);

                                    String pathloc = "file:///Users/wangyiwen/Desktop/ComputerNetworking/wangyiwenemail/"+num_index+"/mail.html";
                                    editorPane.setEditable(false);
                                    try{
                                        editorPane.setPage(pathloc);
                                        //editorPane.setBounds(10, 212, 414, 186);
                                    }catch(Exception ee){
                                        ee.printStackTrace();
                                    }
                                }catch(Exception ex){
                                    ex.printStackTrace();
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


    public static EmailData parseMessage(Message[] messages,int lb, int ub, String DocuPath) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");
        String tempContent = "";
        File file = new File(DocuPath);
        if(!file.exists()){
            file.mkdir();
        }
        // 解析所有邮件
        int cnt_mess = messages.length;
        for(int i = lb; i < ub; i++){
            String EmailDocuPath = DocuPath + i + "/";
            File file2 = new File(EmailDocuPath);
            if(!file2.exists()){
                file2.mkdir();
            }
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
            System.out.println("邮件大小：" + msg.getSize() / 1024 + "kb");
            String WriteTxt =EmailDocuPath + "email.txt";
            //String WriteTxt = DocuPath + i + "/email.txt";
            File txtfw = new File(WriteTxt);
            //msg.writeTo(System.out);
            //System.out.println("MessageTry:" + SharedByteArrayInputStream(msg.getContent()));
            boolean isContainerAttachment = isContainAttachment(msg);


            System.out.println("是否包含附件：" + isContainerAttachment);
            if (isContainerAttachment) {
                saveAttachment(msg, EmailDocuPath+msg.getSubject() + "_"+i+"_"); //保存附件
            }

            StringBuffer content = new StringBuffer(30);
          //  System.out.println("Text: "+MimeUtility.getEncoding(msg.getContent()));
            getMailTextContent(msg, content);
            tempContent = content.toString();
            int len_c = tempContent.length();
            int pos = 0;
            for(int i0 = 0; i0 < len_c; ++i0){
                if(tempContent.charAt(i0) == '<'){
                    pos = i0;
                    break;
                }
            }
            tempContent = tempContent.substring(pos);
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(txtfw));
                bw.write("Subject: ");
                bw.write(getSubject(msg).toString());
                bw.write("\n");
                bw.write("From: ");
                bw.write(getFrom(msg).toString());
                bw.write("\n");
                bw.write("Time: ");
                bw.write(getSentDate(msg, null).toString());
                bw.write("\n");
                bw.write("Content: ");
                bw.write(tempContent);
                //bw.write(content.toString());
                bw.flush();
                bw.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }


            String recordPath = EmailDocuPath+"mail.html";
            File fw = new File(recordPath);
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(fw));
                bw.write(tempContent);
                //bw.write(content.toString());
                bw.flush();
                bw.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println("邮件正文：" + (content.length() > 100 ? content.substring(0,100) + "..." : content));
            System.out.println("------------------第" + i + "封邮件解析结束-------------------- ");
            System.out.println();

            EmailData ED = new EmailData(getSentDate(msg, null).toString(),getFrom(msg), getSubject(msg),tempContent,isContainerAttachment);
            return ED;
        }
        //return cnt_mess;
        return new EmailData();
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
            System.out.println("Look");
            System.out.println(thisline);
            while(thisline != null){
                //System.out.println(thisline);
                content.append(thisline);
                thisline = reader.readLine();
                System.out.println("Look2");
                System.out.println(thisline);
            }

            //content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            System.out.println("message");
            getMailTextContent((Part)part.getContent(),content);
        } else if (part.isMimeType("multipart/*")) {
            //Multipart multipart = part.getContent();
           // Object temp = part.getContent();
           // String string = JSON.toJSON(temp).toString();
            ByteArrayDataSource src = new ByteArrayDataSource(part.getInputStream(),"multipart/*");
            Multipart multipart = new MimeMultipart(src);

            int partCount = multipart.getCount();
            System.out.println("Multi Content" + partCount);
            for (int i = 0; i < partCount; i++) {//我也不知道为什么从1开始quq
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
