package sopt.makers.authentication.user;

import java.time.LocalDate;

public record Profile(String name, String email, String phone, LocalDate birthday) {

	public Profile updateName(String name) {
		return new Profile(name, this.email, this.phone, this.birthday);
	}

	public Profile updateEmail(String email) {
		return new Profile(this.name, email, this.phone, this.birthday);
	}

	public Profile updatePhone(String phone) {
		return new Profile(this.name, this.email, phone, this.birthday);
	}

	public Profile updateBirthday(LocalDate birthday) {
		return new Profile(this.name, this.email, this.phone, birthday);
	}
}
