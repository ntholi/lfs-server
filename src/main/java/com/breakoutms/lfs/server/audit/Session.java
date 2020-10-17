package com.breakoutms.lfs.server.audit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.breakoutms.lfs.server.branch.model.Branch;
import com.breakoutms.lfs.server.user.model.User;

//@Entity
public class Session {

//	@Id 
//	@GenericGenerator(name = "custom_id", strategy = "lfs.desktop.util.IdGenerator")
//	@GeneratedValue(generator = "custom_id")
//	private Integer id;
//	private String machineName;
//
//	private String ipAddress;
//	private String localUser;
//	@Column(nullable = false, updatable = false, insertable = false, 
//			columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//	private LocalDateTime date;
//	@ManyToOne
//	private User user;
//	@ManyToOne
//	private Branch branch;
//
//	private Session(){}
//
//	public static Session get() {
//		if(session == null) {
//			session = new Session();
//			try {
//				InetAddress localMachine = InetAddress.getLocalHost();
//				session.setMachineName(localMachine.getHostName());
//				session.ipAddress = localMachine.getHostAddress();
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			}
//			session.setLocalUser(System.getProperty("user.name"));
//			session.setUser(CurrentUser.get());
//			session.branch = Branch.getCurrentBranch();
//		}
//
//		return session;
//	}
}
