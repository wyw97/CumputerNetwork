package emailClient;

import java.awt.BorderLayout;
import java.awt.EventQueue;

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
import java.util.Base64;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class SelectPage extends JFrame{
    private JPanel contentPane;

    public SelectPage(String username,String password,String realm){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 498);


        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.activeCaption);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);



        JButton btnSnd = new JButton("send");
        btnSnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                WriteAndSendEmail send = new WriteAndSendEmail(username,password,realm);
                send.setVisible(true);

            }
        });
        btnSnd.setFont(new Font("Consolas", Font.BOLD, 16));
        //btnSnd.setBounds(308, 399, 116, 38);
        btnSnd.setBounds(157, 303, 93, 37);
        contentPane.add(btnSnd);


        JButton btnRead = new JButton("view");
        btnRead.addActionListener(new ActionListener(){
           // @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ReceiveAndReadEmail read = new ReceiveAndReadEmail(username, password, realm);
                    read.setVisible(true);
                    //dispose();
                }catch (Exception e2){
                    e2.printStackTrace();
                }
             }
        });
        btnRead.setFont(new Font("Consolas", Font.BOLD, 16));
        btnRead.setBounds(157, 103, 93, 37);
        contentPane.add(btnRead);





    }
}
