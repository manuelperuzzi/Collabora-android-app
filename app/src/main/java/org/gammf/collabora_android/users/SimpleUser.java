package org.gammf.collabora_android.users;

import org.gammf.collabora_android.utils.MandatoryFieldMissingException;
import org.joda.time.DateTime;

/**
 * @author Manuel Peruzzi
 * Simple class representing a standard user.
 */
public class SimpleUser implements User {

    private final String username;
    private final String email;
    private final String name;
    private final String surname;
    private final DateTime birthday;

    private SimpleUser(final String username, final String email, final String name,
                         final String surname, final DateTime birthday) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public DateTime getBirthday() {
        return birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleUser that = (SimpleUser) o;

        return username.equals(that.username) &&
                (email != null ? email.equals(that.email) : that.email == null &&
                (name != null ? name.equals(that.name) : that.name == null &&
                (surname != null ? surname.equals(that.surname) : that.surname == null &&
                (birthday != null ? birthday.equals(that.birthday) : that.birthday == null))));

    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        return result;
    }

    /**
     * @author Manuel Peruzzi
     * This nested class is used as a builder to create an object of type SimpleUser.
     */
    public static class Builder {

        private String username;
        private String email = "";
        private String name = "";
        private String surname = "";
        private DateTime birthday = new DateTime();

        /**
         * @param username the username of the user.
         * @return the builder object.
         */
        public Builder username(final String username) {
            this.username = username;
            return this;
        }

        /**
         * @param email the email of the user.
         * @return the builder object.
         */
        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        /**
         * @param name the name of the user.
         * @return the builder object.
         */
        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * @param surname the surname of the user.
         * @return the builder object.
         */
        public Builder surname(final String surname) {
            this.surname = surname;
            return this;
        }

        /**
         * @param birthday the date of birth of the user.
         * @return the builder object.
         */
        public Builder birthday(final DateTime birthday) {
            this.birthday = birthday;
            return this;
        }

        /**
         * Builds an object of type SimpleUser with the supplied fields.
         * @return an object of type SimpleUser.
         * @throws MandatoryFieldMissingException if one or more mandatory fields have not been inserted.
         */
        public SimpleUser build() throws MandatoryFieldMissingException {
            if (username == null) {
                throw new MandatoryFieldMissingException("username", "SimpleUser");
            }
            return new SimpleUser(username, email, name, surname, birthday);
        }

    }

}

