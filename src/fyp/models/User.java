package fyp.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "Accounts")
@DynamicUpdate
public class User extends IdStatusTimeModel implements Comparable<User> {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@Column(name = "user_id", length = 20, nullable = false, unique = true)
	private String userId;
	
	public String getUserId(){
		return userId;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	@Column(length = 64, nullable = false)
	private String name;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	@Column(length = 64, nullable = false)
	private String password;
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
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
	
	/*
	private Byte departmentId;
	
	public Byte getDepartmentId(){
		return departmentId;
	}
	public void setDepartmentId(Byte departmentId){
		this.departmentId = departmentId;
	}
	*/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;
	
	public Department getDepartment(){
		return department;
	}
	
	public void setDepartment(Department department){
		this.department = department;
	}
	
	@Column
	private Byte category;
	
	public Byte getCategory(){
		return category;
	}
	
	public void setCategory(Byte category){
		this.category = category;
	}
	
	public boolean isAdmin(){
		return category == 0;
	}
	
	public boolean isStudent(){
		return category == 1;
	}
	
	public boolean isTeacher(){
		return category == 2;
	}
	
	public boolean isGuest(){
		return !isAdmin() && !isStudent() && !isTeacher();
	}
	
	
	public int compareTo(User user) {
		int result = department.compareTo(user.department);
		if (0 != result) return result;

		if (null == category && null != user.category) return 1;
		if (null != category && null == user.category) return -1;
		// put undefined user at last
		
		result = category.compareTo(user.category);
		if (0 != result) return result;
		
		return userId.compareTo(user.userId);
	}
}