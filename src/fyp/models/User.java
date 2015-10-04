package fyp.models;

import java.sql.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class User{
	
	private Integer id;
	
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	
	private Date createTime;
	
	public Date getCreateTime(){
		return createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	
	private Date updateTime;
	
	public Date getUpdateTime(){
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	
	
	private String userId;
	
	public String getUserId(){
		return userId;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	
	private String name;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	
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
	
	
	private Byte departmentId;
	
	public Byte getDepartmentId(){
		return departmentId;
	}
	public void setDepartmentId(Byte departmentId){
		this.departmentId = departmentId;
	}
	
	
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
		return category == 3;
	}
}