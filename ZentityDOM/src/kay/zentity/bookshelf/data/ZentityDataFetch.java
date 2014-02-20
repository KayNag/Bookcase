package kay.zentity.bookshelf.data;
public class ZentityDataFetch  {

	private String ID;
	private String TITLE;
	private String imageUrl;
	private String STATUS;
	
	
	public ZentityDataFetch(String ID, String number, String imageUrl,String Status) {
		super ();
		
		this.ID = ID;
		this.TITLE = number;
		this.imageUrl = imageUrl;
		this.STATUS = Status;
		
		
		
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getTicketno() {
		return TITLE;
	}

	public void setTicketno(String number) {
		this.TITLE = number;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTicketstatus() {
		return STATUS;
	}

	public void setTicketstatus(String status) {
		this.STATUS = status;
	}

		
}
