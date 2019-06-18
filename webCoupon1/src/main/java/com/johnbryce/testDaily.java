package com.johnbryce;

import java.util.concurrent.TimeUnit;

import com.johnbryce.beans.Company;
import com.johnbryce.exception.CouponException;
import com.johnbryce.facad.AdminFacad;
import com.johnbryce.facad.CouponClientFacade;
import com.johnbryce.utils.ClientType;
import com.johnbryce.utils.CouponSystem;
import com.johnbryce.utils.DataBase;



public class testDaily {
	private static final TimeUnit TIMEUNIT = TimeUnit.MILLISECONDS;
	private static final int COUPON_NUMBER = 3;
	private static final int TEST_ITERATIONS = 4;

	public static void main(String[] args) {
		try {
			//CouponSystem couponSystem = CouponSystem.getInstance();
			//couponSystem.setDebugMode(true);
			CouponClientFacade facade = CouponSystem.login("admin", "1234", ClientType.ADMIN);
			if (facade instanceof AdminFacad) {
				System.out.println("Prepering Test\ndroping and recreating DB tables\n");
				DataBase.dropTableifNeeded();
				DataBase.createTables();
				//Company company01 = new Company("PIZZAHUT", "111", "pizzahut@gmail.com");
				//Company company02 = new Company("PIZZADomino", "222", "pizzadomino@gmail.com");
				
				AdminFacad adminFacad = (AdminFacad) facade;
				//adminFacad.createCompany(company01);	
				//adminFacad.createCompany(company02);
				System.out.println("\nAdding a company\n" + adminFacad.getAllCompanies().toString());
				
				/*facade = CouponSystem.login("PIZZAHUT", "111", ClientType.COMPANY);
				if (facade instanceof CompanyFacade) {
					System.out.println("\nLogin in to the Company\n");
					Coupon coupon;
					for(int i = 0 ; i < COUPON_NUMBER ; i++ ) {
						coupon = new Coupon(i, "Coupon#"+i, Utile.getCurrentDate(), Utile.getDateAfter(i), i, CouponType.Resturans," this coupon should disapear after "+(i+1)+" Cycles",1+i*2.0,"");
						((CompanyFacade) facade).createCoupon(coupon);
					}
					for(int i = 0 ; i < TEST_ITERATIONS ; i++ ) {
						TIMEUNIT.sleep(15000);
						Set<Coupon> coupons = ((CompanyFacade) facade).getCoupons();
						System.out.println("there are total of "+coupons.size()+" coupons in the system at the moment");
						System.out.println("Show all company coupons\n"+ ((CompanyFacade) facade).getCoupons().toString());
						
					}
					//couponSystem.shutdown();
				}*/
			}
			
		} catch (CouponException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
