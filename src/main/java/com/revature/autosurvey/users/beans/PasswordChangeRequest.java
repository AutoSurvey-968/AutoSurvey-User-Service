package com.revature.autosurvey.users.beans;

public class PasswordChangeRequest {
	private int userId;
	private String oldPass;
	private String newPass;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getOldPass() {
		return oldPass;
	}
	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}
	public String getNewPass() {
		return newPass;
	}
	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newPass == null) ? 0 : newPass.hashCode());
		result = prime * result + ((oldPass == null) ? 0 : oldPass.hashCode());
		result = prime * result + userId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PasswordChangeRequest other = (PasswordChangeRequest) obj;
		if (newPass == null) {
			if (other.newPass != null)
				return false;
		} else if (!newPass.equals(other.newPass))
			return false;
		if (oldPass == null) {
			if (other.oldPass != null)
				return false;
		} else if (!oldPass.equals(other.oldPass))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PasswordChangeRequest [userId=" + userId + ", oldPass=" + oldPass + ", newPass=" + newPass + "]";
	}
}
