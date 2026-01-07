package constants;

public enum ResMessage {
	
	SUCCESS(200, "成功"), //
	ERROR(500, "失敗"), //
	LOGIN_ERROR(400, "帳號密碼輸入失敗"), //
	USER_NOT_FOUND(400, "找不到此用戶"), //
	DATE_ERROR(400, "開始時間設定失敗"), //
	TYPE_ERROR(400, "選擇種類設定失敗"), //
	OPTIONS_SIZE_ERROR(400, "選項數量設定失敗"), // 
	OPTIONS_NAME_MISMATCH(400,"OPTIONS_NAME_MISMATCH"),	
	ANSWER_REQUIRED(400,"ANSWER_REQUIRED"),	
	NOT_FOUND(404,"NOT_FOUND"),
	PLEASE_LOGIN_FIRST(400, "請重新登入");
	

	private int code;
	
	private String message;
	
	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
