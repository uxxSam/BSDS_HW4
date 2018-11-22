package jayray.net.model;

public class User {
	
	private int userID;
	private int historyID;
	
	public User(){

	}
	
	public User(int userID, int historyID) {		
		this.userID = userID;
		this.historyID = historyID;
	}

	public int getuserID() {
		return userID;
	}

	public void setId(int userID) {
		this.userID = userID;
	}

	public int getHistoryID() {
		return historyID;
	}

	public void setNameeng(int historyID) {
		this.historyID = historyID;
	}	
}
