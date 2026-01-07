package request;



import java.util.List;

import constants.ValidMsg;
import jakarta.validation.constraints.NotBlank;
import vo.fee_description;

public class StoresReq {

	
	@NotBlank(message = ValidMsg.STORE_NAME_ENPTY)
	private String storesname;
	
	@NotBlank(message = ValidMsg.PHONE_ENPTY)
	private int phone;
	
	@NotBlank(message = ValidMsg.CATEGORY_ENPTY)
	private String category;
	
	@NotBlank(message = ValidMsg.TYPE_ENPTY)
	private String type;
	
	private String memo;
	
	private String image;
	
	private List <fee_description> fee_description;
	
	private boolean publish;

	public String getStoresname() {
		return storesname;
	}

	public void setStoresname(String storesname) {
		this.storesname = storesname;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<fee_description> getFee_description() {
		return fee_description;
	}

	public void setFee_description(List<fee_description> fee_description) {
		this.fee_description = fee_description;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}
	
	
}
