package edu.hkpolyu.epre.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import edu.hkpolyu.common.model.StatusModel;

@Entity
@Table(name = "t_user_password")
@DynamicUpdate
public class UserPassword extends StatusModel {
	@Transient
	private static final long serialVersionUID = 1L;
	
    @Id
	@Column(name = "user_id")
	protected Integer userId;
	
	public Integer getUserId(){
		return userId;
	}
	
	public void setUserId(Integer userId){
        this.user = null;
		this.userId = userId;
	}
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
        this.setUserId(user.getId());
		this.user = user;
	}

	@Column(length = 64, nullable = false)
	private String password;
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = sha256(password);
	}
	
    public boolean isPasswordCorrect(String password) {
        return sha256(password) == this.password;
    }

	public static final String HEX_DIGITS = "0123456789abcdef";
	
	public static String sha256(String password){
		MessageDigest md = null;
		StringBuffer str = new StringBuffer();
		try {
			md = MessageDigest.getInstance("SHA-256");
			byte[] result = md.digest(password.getBytes()); 
			for(byte b : result){
				str.append(HEX_DIGITS.charAt((b >> 4) & 0xF));
				str.append(HEX_DIGITS.charAt(b & 0xF));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return str.toString();
	}
	
	public static String decryptAES128(String code, String key){
		String result = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] output = cipher.doFinal(code.getBytes("UTF-8"));
			result = new String(output, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
