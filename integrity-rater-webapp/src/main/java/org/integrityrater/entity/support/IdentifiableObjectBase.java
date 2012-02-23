package org.integrityrater.entity.support;


public abstract class IdentifiableObjectBase implements Identifiable {

	public int hashCode() {
		return 0 != getId() ? Long.valueOf(getId()).hashCode() : "NULL"
				.hashCode();
	}

	public boolean equals(Object obj) {

		if (null == obj)
			return false;

		// TODO: Removed this because it fails to identify two identical objects
		// if one of them is enhanced by cglib
		/*
		 * if( !obj.getClass().getName().equals( getClass().getName())) return
		 * false;
		 */
		
		// Right type of object?
		if (!(obj instanceof IdentifiableObjectBase)) {
		    return false;
		}

		return 0 != getId() ? getId() == ((IdentifiableObjectBase) obj).getId()
				: 0 == ((IdentifiableObjectBase) obj).getId();
	}

	public int compareTo(Object obj) {

		if (null == obj)
			return 1;

		if (obj.getClass().getName().equals(getClass().getName())) {
			Long myId = Long.valueOf(this.getId());
			Long otherId = Long.valueOf(((IdentifiableObjectBase) obj).getId());
			if (null != myId) {
				return myId.compareTo(otherId);
			} else {
				return -1;
			}
		}
		
		return -1;
	}

	public String toString() {
		return "id=" + this.getId() + ": " + super.toString();
	}
}