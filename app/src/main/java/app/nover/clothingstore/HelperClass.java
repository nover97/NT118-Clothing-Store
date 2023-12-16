package app.nover.clothingstore;

import com.google.firebase.database.PropertyName;

public class HelperClass {
    @PropertyName("fullName")
    public String fullName;
    @PropertyName("email")
    public String email;
    @PropertyName("password")
    public String password;


    public HelperClass(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public HelperClass() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
