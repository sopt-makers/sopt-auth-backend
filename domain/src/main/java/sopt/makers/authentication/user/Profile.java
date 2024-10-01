package sopt.makers.authentication.user;

import java.time.LocalDate;
import java.util.Objects;

public class Profile {

	private final String name;

	private final String email;

	private final String phone;

	private final LocalDate birthday;

	public Profile(String name, String email, String phone, LocalDate birthday) {
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.birthday = birthday;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Profile profile = (Profile) o;
		return Objects.equals(name, profile.name)
				&& Objects.equals(email, profile.email)
				&& Objects.equals(phone, profile.phone)
				&& Objects.equals(birthday, profile.birthday);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, email, phone, birthday);
	}

	public Profile updateName(String name) {
		return new Profile(name, email, phone, birthday);
	}

	public Profile updateEmail(String email) {
		return new Profile(name, email, phone, birthday);
	}

	public Profile updatePhone(String phone) {
		return new Profile(name, email, phone, birthday);
	}

	public Profile updateBirthday(LocalDate birthday) {
		return new Profile(name, email, phone, birthday);
	}
}
