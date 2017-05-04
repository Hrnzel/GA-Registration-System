package Model;

public class TerminalCounter {
    
    private String counterId;
    private String userId;
    private String counter;

    public String getCounterId() {
        return counterId;
    }

    public void setCounterId(String counterId) {
        this.counterId = counterId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public TerminalCounter(String counterId, String userId, String counter) {
        this.counterId = counterId;
        this.userId = userId;
        this.counter = counter;
    }
    
}
