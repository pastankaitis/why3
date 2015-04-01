/**
 * com.why3.ws
 * ssh
 * 
 * @author: David Ebo Adjepon-Yamoah - d.e.adjepon-yamoah@ncl.ac.uk
 *
 */

package com.why3.ws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.jcraft.jsch.*;

/**
 * Performs SSH connection operations and execute commands on remote hosts 
 */
public class why3ssh {
	private static Logger logger = Logger.getLogger(why3ssh.class);
	private String identity;
	private String username;
	private String ipAddress;
	private int port;
	private Session session;

	/**
	 * Constructor
	 * @param identity
	 * @param username
	 * @param ipAddress
	 * @param port
	 */
	public why3ssh(String identity, String username, String ipAddress, int port)
	{
		setIdentity(identity);
		setUsername(username);
		setIpAddress(ipAddress);
		setPort(port);
		
		init();		
	}
	
	
	/**
	 * initiating the SSH session
	 */
	public void init()
	{
		JSch jsch=new JSch();
		
		try 
		{
			jsch.addIdentity(identity);
			//JSch.setConfig("StrictHostKeyChecking", "no");
			logger.info(" ==========> STARTING SSH <==========");
			logger.info("connecting to ip: " + ipAddress + " with key: " + identity);
			session=jsch.getSession(username, ipAddress, port);
			session.connect();
		} 
		catch (JSchException e) 
		{
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * disconnecting the SSH session
	 */
	public void disconnectSession()
	{
		session.disconnect();
		logger.info(" ==========> DISCONNECTING SSH <==========");
	}
	
	/**
	 * creates a channel to the SSH session and executes the input commands
	 * @param commands
	 * @return TODO
	 */
	public boolean execCommand(String command)
	{
		boolean outcome = false;
		try
		{
			ChannelExec channel;

			channel = (ChannelExec) session.openChannel("exec");
			BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));
			String msg=null;
			
			
			channel.setCommand(command);
			logger.info("EXECUTING -- " + command);
			channel.connect();
			//System.out.println(command + " executed!");
		    while((msg = in.readLine())!= null)
		    {
			   logger.info(msg);
		    }
		   
		    channel.disconnect();
		    outcome = true;
		}
		catch (JSchException e)
		{
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		return outcome;
	}
	
	/**
	 * uploads file to a host using SSH
	 * @param file
	 * @param SFTPWORKINGDIR
	 */
	public void uploadFile(File file, String SFTPWORKINGDIR)
	{
		Channel     channel     = null;
		ChannelSftp channelSftp = null;
		try {
			channel = session.openChannel("sftp");		
			channel.connect();
			channelSftp = (ChannelSftp)channel;
			channelSftp.cd(SFTPWORKINGDIR);
			channelSftp.put(new FileInputStream(file), file.getName());
			logger.info("FILE UPLOAD ==> " + file.getName() + " TO " + SFTPWORKINGDIR);
		} 
		catch (JSchException e) 
		{
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
	}
	

	/**
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * @param identity the identity to set
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	
	public static void main(String args[])
	{
		ArrayList<String> input = new ArrayList<String>();
		input.add("whoami;hostname");
		input.add("echo \"test passed\"");
		input.add("mkdir /home/ubuntu/models");
		
		why3ssh ssh = new why3ssh("H:\\PhD\\amazon\\aws-US.pem","ec2-user","54.91.184.5" , 22);
		for(String command: input)
			ssh.execCommand(command);
		File f = new File("D:\\promela\\peterson.pml");
		ssh.uploadFile(f, "/home/ubuntu/models");
		ssh.disconnectSession();
		
	}
	
}






	
	
	
	

