package user.model;
import java.sql.Date;

public class Member {
    private long userId;
    private String name;
    private String residentId;
    private String address;
    private String phone;
    private String email;
    private String password;
    private Date birthDate;

    public Member() {}

    public Member(long userId, String name, String residentId, String address, String phone, String email, String password, Date birthDate) {
        this.userId = userId;
        this.name = name;
        this.residentId = residentId;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
    }

    // getters & setters
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getResidentId() { return residentId; }
    public void setResidentId(String residentId) { this.residentId = residentId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
}
