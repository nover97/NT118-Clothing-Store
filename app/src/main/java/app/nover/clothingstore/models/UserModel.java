package app.nover.clothingstore.models;

public class UserModel {
    String fullName, email, password, urlImage, role;


    public UserModel() {
    }

    public UserModel(String fullName, String email, String password, String urlImage, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.urlImage = urlImage;
        this.role = role;

    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
