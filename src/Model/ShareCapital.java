package Model;

public class ShareCapital {
    private String accountNo;
    private String comShare;
    private String prefShare;
    private String fixDep;
    private String total;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getComShare() {
        return comShare;
    }

    public void setComShare(String comShare) {
        this.comShare = comShare;
    }

    public String getPrefShare() {
        return prefShare;
    }

    public void setPrefShare(String prefShare) {
        this.prefShare = prefShare;
    }

    public String getFixDep() {
        return fixDep;
    }

    public void setFixDep(String fixDep) {
        this.fixDep = fixDep;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ShareCapital(String accountNo, String comShare, String prefShare, String fixDep, String total, String status) {
        this.accountNo = accountNo;
        this.comShare = comShare;
        this.prefShare = prefShare;
        this.fixDep = fixDep;
        this.total = total;
        this.status = status;
    }

    public ShareCapital() {}
}
