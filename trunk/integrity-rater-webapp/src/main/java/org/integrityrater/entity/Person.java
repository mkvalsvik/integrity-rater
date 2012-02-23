package org.integrityrater.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.catamarancode.type.Name;
import org.catamarancode.util.Timestamped;
import org.integrityrater.entity.support.PersistentEntityBase;
import org.springframework.util.StringUtils;


/**
 * Trick to avoid line break problems: Add a pipe at the end of the last cell and import with
 * load data infile '/temp/persons.csv' into table Person fields terminated by ',' lines terminated by '|';
 * @author mkvalsvik
 *
 */
@Entity
public class Person extends PersistentEntityBase implements Timestamped {

	public enum Status {
		ACTIVE, DELETED, INACTIVE;
	}
	
	private static final String PASSWORD_ENCODING_SECRET = "d849ba754cc5cbd2bcb12c52788271fe";

	private String passwordEncodingUniqueId;
	private Date createdTime;
	private double integrityScore;
	
	private Date lastModifiedTime;
	private Name name;
	private String email;
	
	private byte[] photo = new byte[0];	
	
	@Transient
	private String oldEmail;

	private Person.Status status = Person.Status.INACTIVE;

	@Transient
	private String emailConfirm;
	
	private String passwordResetToken;
	
	private String encodedPassword;
	
	private List<Complaint> complaints = new ArrayList<Complaint>();
	
    @OneToMany(mappedBy = "person")
    public List<Complaint> getComplaints() {
        return complaints;
    }

	/**
	 * Encodes password. Will lazily create a "salt" code if none found which is
	 * unique to this user id.
	 * 
	 * @param password
	 *            the cleartext password
	 * @return the encoded password as a String
	 */
	public String encode(String password) {
		if (!StringUtils.hasText(this.passwordEncodingUniqueId)) {
			passwordEncodingUniqueId = RandomStringUtils.randomAlphanumeric(16);
		}
		StringBuffer sb = new StringBuffer();
		sb.append(passwordEncodingUniqueId);
		sb.append(PASSWORD_ENCODING_SECRET);
		sb.append(password);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(
					"Password encoding algorithm SHA-1 not found, please check you Java version",
					e);
		}
		try {
			md.update(sb.toString().getBytes("UTF-8"));
			byte[] hash = md.digest();
			Base64 base64Encoder = new Base64();
			byte[] base64EncodedHash = base64Encoder.encode(hash);
			return new String(base64EncodedHash, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"UTF-8 not supported, please check you Java version", e);
		}
	}

	/**
	 * Masked password
	 * @return
	 */
	@Transient
	public String getCleartextPassword() {
	    if (StringUtils.hasText(this.encodedPassword)) {
	        return "********";
	    }
	    return null;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	@Column(length = 100)
	public String getEmail() {
		return email;
	}
	
	@Transient
	public String getEmailConfirm() {
		return emailConfirm;
	}

	/**
	 * Gets the encoded password. Normally not used for anything except keeping
	 * it around for when user data is re-saved.
	 * 
	 * @return encoded password as a String
	 */
	@Column(length = 28)
	public String getEncodedPassword() {
		return this.encodedPassword;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	@Embedded
	public Name getName() {
		return name;
	}

	@Transient
	public String getOldEmail() {
		return oldEmail;
	}

	/**
	 * Used only for the purpose of providing a unique "salt" for the password
	 * encoding mechanism
	 * 
	 * @return
	 */
	@Column(length = 16)
	public String getPasswordEncodingUniqueId() {
		return passwordEncodingUniqueId;
	}

	@Column(length = 32)
	public String getPasswordResetToken() {
		return this.passwordResetToken;
	}

	@Column(length=131072)
    public byte[] getPhoto() {
        return photo;
    }

	/**
	 * Encodes cleartext password and compares it with the encoded one
	 * 
	 * @param cleartextPassword
	 * @return true if match, false otherwise
	 */
	public boolean passwordMatches(String cleartextPassword) {
		if (!StringUtils.hasText(this.encodedPassword)) {
			return false;
		}
		String encodedCleartextPassword = this.encode(cleartextPassword);
		return encodedPassword.equals(encodedCleartextPassword);
	}

	public void readPhotoFrom(InputStream in) throws IOException {
        setPhoto(IOUtils.toByteArray(in));
    }

	/**
	 * Sets the encodedPassword by encoding the given cleartextPassword
	 * 
	 * @param cleartextPassword
	 */
	public void setCleartextPassword(String cleartextPassword) {
		this.encodedPassword = encode(cleartextPassword);
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setEmailConfirm(String emailConfirm) {
		this.emailConfirm = emailConfirm;
	}

	/**
	 * Sets the encoded password (used hibernate populates this object from a
	 * data store)
	 * 
	 * @param encodedPassword
	 *            the encoded password
	 */
	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public void setOldEmail(String email) {
		this.oldEmail = email;
	}
	
    public void setPasswordEncodingUniqueId(String passwordEncodingUniqueId) {
		this.passwordEncodingUniqueId = passwordEncodingUniqueId;
	}

    public void setPasswordResetToken(String token) {
		this.passwordResetToken = token;
	}
    
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void writePhotoTo(OutputStream out) throws IOException {
        IOUtils.write(getPhoto(), out);
    }
    
    @Transient
    public boolean isUser() {
        if (StringUtils.hasText(this.email)) {
            return true;
        }
        return false;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public double getIntegrityScore() {
        return integrityScore;
    }

    public void setIntegrityScore(double integrityScore) {
        this.integrityScore = integrityScore;
    }

	
}
