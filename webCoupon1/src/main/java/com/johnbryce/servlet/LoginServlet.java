package com.johnbryce.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.johnbryce.exception.CouponException;
import com.johnbryce.facad.CouponClientFacade;
import com.johnbryce.utils.ClientType;
import com.johnbryce.utils.CouponSystem;


//@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	
	private CouponSystem system;



	@Override
	public void init() {
			try {
				system = CouponSystem.getInstance();
			} catch (CouponException e) {
				System.out.println("Failed to connect to db, Failed to load system");
				System.exit(1);
			}
			System.out.println("Loaded...");

	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("abcd im here~~~~~");

		// check whether there is a open session
		HttpSession session = request.getSession(false);

		if (session != null) {

			session.invalidate(); // killing the session if exist
		}

		session = request.getSession(true); // create a new session for a new
											// client
		System.out.println(session.getId() +" * "+ session.getMaxInactiveInterval());
		// getting the data from the Login HTML form
		System.out.println("I'm here");
		String username = request.getParameter("name");
		System.out.println(username);
		String password = request.getParameter("pass");
		System.out.println(password);
		String clientType = request.getParameter("type");
		System.out.println(clientType);
		ClientType type = ClientType.valueOf(clientType.toUpperCase()); // convert String to
															// ENUM

		try {
System.out.println("I'm here2");
			CouponClientFacade facade = system.login(username, password, type);
			System.out.println("loginServlet: request = " + request); // for
																		// debug
			System.out.println("loginServlet: response = " + response); // for
																		// debug

			if (facade != null) {
				// updating the session with the login facade
				session.setAttribute("facade", facade);
				// dispatcher to the right Page according to the Client Type
				switch (type) {
				case ADMIN:
					request.getRequestDispatcher("/admin.html").forward(request, response);
					break;

				case COMPANY:
					// updating the session with the logged in company
					// Company company =
					// ((CompanyFacade)facade).getLoginCompany();
					// session.setAttribute("company", company);
					request.getRequestDispatcher("web/company.html").forward(request, response);
					break;

				case CUSTOMER:
					// updating the session with the logged in customer
					// Customer customer =
					// ((CustomerFacade)facade).getLoginCustomer();
					// session.setAttribute("customer", customer);
					request.getRequestDispatcher("web/customer.html").forward(request, response);
					break;

				default:
					break;
				}
			}

			else {
				// return to the Login HTML form if the user name or password
				// are incorrect
				// response.getWriter().print("The UserName or the Password are
				// incorrect! please try again");
				response.sendRedirect("login.html");
			}

		}

		// LoginException
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}