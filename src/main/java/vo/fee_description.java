package vo;

public class fee_description {

	private int km;
	
	private int fee;

	public fee_description() {
		super();
	}

	public fee_description(int km, int fee) {
		super();
		this.km = km;
		this.fee = fee;
	}

	public int getKm() {
		return km;
	}

	public void setKm(int km) {
		this.km = km;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}
	
	
	
}
