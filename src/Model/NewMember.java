package Model;

public class NewMember {
    
    private String accountNo;
    private String name;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NewMember(String accountNo, String name) {
        this.accountNo = accountNo;
        this.name = name;
    }

    public NewMember() {
    }
    
}
