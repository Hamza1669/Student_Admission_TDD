package com.student.model;

import java.util.Objects;

public class Student {

		private Long id;
	    private String firstName;
	    private String lastName;
	    private String email;
	    
		public Student(Long id, String firstName, String lastName, String email) {
			super();
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		@Override
		public int hashCode() {
			return Objects.hash(email, firstName, id, lastName);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Student other = (Student) obj;
			return Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
					&& Objects.equals(id, other.id) && Objects.equals(lastName, other.lastName);
		}
		
		
	    
	   

}
