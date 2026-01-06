package constants;

public enum Type {

	SINGLE("SINGLE"), //
	MULTI("MULTI"), //
	TEXT("TEXT");

	private String type;

	private Type(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static boolean checkType(String input) {
//		if (input.equalsIgnoreCase(Type.SINGLE.getType()) //
//				|| input.equalsIgnoreCase(Type.MULTI.getType()) //
//				|| input.equalsIgnoreCase(Type.TEXT.getType())) {
//			return true;
//		}
		/* values() : 指的是上面 enum 的所有項目 */
		for (Type type : values()) {
			if (input.equalsIgnoreCase(type.getType())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isChosenType(String input) {
		if (input.equalsIgnoreCase(Type.SINGLE.getType()) || input.equalsIgnoreCase(Type.MULTI.getType())) {
			return true;
		}
		return false;
	}
}
