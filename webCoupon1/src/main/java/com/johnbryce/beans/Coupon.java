package com.johnbryce.beans;

import java.util.Date;

/**
 * this is the object representing Coupon data
 * 
 * @author Eivy & Michal
 *
 */
public class Coupon {
	private long couponId;
	private String title;
	private Date start_date;
	private Date end_date;
	private int amount;
	private CouponType type;
	private String message;
	private Double price;
	private String image;

	/**
	 * empty cTor for Company object instantiation 
	 */
	public Coupon() {}

	/**
	 * full cTor for the coupon object
	 * 
	 * @param couponId id of the coupon
	 * @param title coupon's title
	 * @param start_date the coupons start date for usage
	 * @param end_date expiration date of the coupon
	 * @param amount amount of coupons available
	 * @param type the category of the coupon 
	 * @param message the detailed information about the coupon
	 * @param price the pricing for each coupon
	 * @param image a image of the service provided by the coupon
	 */
	public Coupon(long couponId, String title, Date start_date, Date end_date, int amount, CouponType type,
			String message, Double price, String image) {
		setCouponId(couponId);
		setTitle(title);
		setStart_date(start_date);
		setEnd_date(end_date);
		setAmount(amount);
		setType(type);
		setMessage(message);
		setPrice(price);
		setImage(image);
	}
	
	public Coupon(long couponId, String title, Date start_date, Date end_date, int amount, String type, String message, Double price, String image) {
		this(couponId, title, start_date, end_date, amount, resolveType(type), message, price, image);
	}

	public Coupon(String title, Date start_date, Date end_date, int amount, CouponType type,String message,Double price, String image) {
		setTitle(title);
		setStart_date(start_date);
		setEnd_date(end_date);
		setAmount(amount);
		setType(type);
		setMessage(message);
		setPrice(price);
		setImage(image);
	}
	
	public Coupon(String title, Date start_date, Date end_date, int amount, String type,String message,Double price, String image) {
		this(title, start_date, end_date, amount, resolveType(type), message, price, image);
	}

	public static CouponType resolveType(String type) {
		switch (type) {
		case "Food":
			return CouponType.Food;
		case "Resturans":
			return CouponType.Resturans;
		case "Electricity":
			return CouponType.Electricity;
		case "Health":
			return CouponType.Health;
		case "Sports":
			 return CouponType.Sports;
		case "Camping":
			 return CouponType.Camping;
		case "Traveling":
			 return CouponType.Traveling;
		default:
			return null;
		}
	}

	/**
	 * @return the coupon id
	 */
	public long getCouponId() {
		if (couponId<0) {
			System.out.println("id is invalid");
		}else {
			System.out.println("id is valid");
		}
		return couponId;
	}

	/**
	 * @param couponId id to set the coupon 
	 */
	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	/**
	 * @return the title of the coupon
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title set the coupon's title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the date the coupon can be used
	 */
	public Date getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date set the coupon initial usage date
	 */
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return the expiration date of the coupon
	 */
	public Date getEnd_date() {
		return end_date;
	}

	/**
	 * @param end_date set's the expiration date for the coupon
	 */
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	/**
	 * @return the amount of coupon there is currently for sale
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount set the amount of coupons to be soled
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the category of the coupon 
	 */
	public CouponType getType() {
		return type;
	}

	/**
	 * @param type set the category of the coupon 
	 */
	public void setType(CouponType type) {
		this.type = type;
	}

	/**
	 * @return the detailed information of the coupon 
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message set detailed information of the coupon 
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the price of a single coupon
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price set the pricing of a single coupon
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @return the coupon's image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image sets the coupon's image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * prints all the information about the coupon as a String 
	 */
	@Override
	public String toString() {
		return "Coupon [couponId=" + couponId + ", title=" + title + ", start_date=" + start_date + ", end_date="
				+ end_date + ", amount=" + amount + ", type=" + type + ", message=" + message + ", price=" + price
				+ ", image=" + image + "]";
	}

}
