package emailClient;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.BorderLayout;
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
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

public class Main {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextPane txtpnPleaseLoginYour;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String username;
		String password;
		String realm="qq";

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 502);
		//设置整个页面的大小

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//设置username和password的位置
		JTextPane txtpnUsername = new JTextPane();
		txtpnUsername.setEditable(false);
		txtpnUsername.setFont(new Font("Consolas", Font.PLAIN, 16));
		txtpnUsername.setText("username:");
		txtpnUsername.setBounds(89, 148, 93, 32);
		frame.getContentPane().add(txtpnUsername);

		JTextPane txtpnPassword = new JTextPane();
		txtpnPassword.setEditable(false);
		txtpnPassword.setText("password:");
		txtpnPassword.setFont(new Font("Consolas", Font.PLAIN, 16));
		txtpnPassword.setBounds(89, 205, 93, 32);
		frame.getContentPane().add(txtpnPassword);

		//设置输入username和password的文本框
		textField = new JTextField();
		textField.setBounds(192, 148, 184, 32);
		frame.getContentPane().add(textField);
		textField.setColumns(10);


		/*
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(192, 205, 184, 32);
		frame.getContentPane().add(textField_1);
		*/
		//修改为密码类别的字段
		textField_1 = new JPasswordField();
		textField_1.setColumns(10);
		textField_1.setBounds(192, 205, 184, 32);
		frame.getContentPane().add(textField_1);

		JButton button = new JButton("log in");
		//JButton button = new JButton("\u767B\u5F55");


		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				String password = textField_1.getText();
				System.out.println(username+ "  " + password);
				/*debug 显示输入的用户名和密码*/

				try {
					Socket client = new Socket("smtp.qq.com",25);
					// smtp使用25号端口监听
					BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String response = br.readLine();
					System.out.println("response 1: "+response);
					if(response.equals("220 smtp.qq.com Esmtp QQ Mail Server")){
						System.out.println("客户端已经连接到腾讯邮件服务器！！");
						//输入HELO指令
						DataOutputStream dos = new DataOutputStream(client.getOutputStream());
						dos.writeBytes("HELO wangyiwen\r\n");
						dos.flush();
						response = br.readLine();
						System.out.println("response 2: "+response);
						if(!response.equals("250 smtp.qq.com")){
							System.out.println("命令错误！！！");
						}
						//输入认证指令，用户名和密码
						dos.writeBytes("AUTH LOGIN\r\n");
						dos.flush();
						response = br.readLine();
						if(!response.equals("334 VXNlcm5hbWU6")){
							System.out.println("命令错误！！！");
						}else{
							System.out.print("请输入用户名：");
							dos.writeBytes(Base64.getEncoder().encodeToString(username.getBytes())+ "\r\n");
							dos.flush();
							response = br.readLine();
							if(!response.equals("334 UGFzc3dvcmQ6")){
								System.out.println("用户名输入错误！！！");
								textField.setText("");
								textField_1.setText("");
							}else{
								System.out.println("用户名输入成功！！！");
								System.out.print("请输入密码：");

								//dos.writeBytes("dm54dmJ2YWJpbXZwaGpjZQ==\r\n");

								dos.writeBytes(Base64.getEncoder().encodeToString(password.getBytes())+ "\r\n");
								dos.flush();
								response = br.readLine();
								if(!response.equals("235 Authentication successful")){
									System.out.println("密码输入错误！！！");
									textField.setText("");
									textField_1.setText("");
								}else{
									System.out.println("登录成功！！！");
									dos.writeBytes("quit\r\n");
									dos.flush();

									//WriteAndSendEmail send = new WriteAndSendEmail(username,password);
									//send.setVisible(true);

									SelectPage selpage = new SelectPage(username,password,realm);
									selpage.setVisible(true);


									frame.dispose();
								}
							}
						}
					}
					else{
						System.out.println("未知错误！！！");
						frame.dispose();
					}
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(157, 303, 93, 37);
		frame.getContentPane().add(button);

		txtpnPleaseLoginYour = new JTextPane();
		txtpnPleaseLoginYour.setFont(new Font("Consolas", Font.BOLD, 18));
		txtpnPleaseLoginYour.setEditable(false);
		txtpnPleaseLoginYour.setText("Please login your QQ email");
		txtpnPleaseLoginYour.setBounds(84, 50, 266, 37);
		frame.getContentPane().add(txtpnPleaseLoginYour);
	}
}
