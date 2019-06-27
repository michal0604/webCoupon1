package com.johnbryce.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.johnbryce.exception.CouponException;
import com.johnbryce.facad.CouponClientFacade;
import com.johnbryce.utils.ClientType;
import com.johnbryce.utils.CouponSystem;

public class LoginServlet extends HttpServlet {

	private CouponSystem system;

	@Override
	public void init() {
		try {
			system = CouponSystem.getInstance();
		} catch (CouponException e) {
			System.out.println("Failed to connect to db, Failed to load system");
			//System.out.println(e.getMessage());
			//e.printStackTrace();
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

		// check whether there is a open session
		HttpSession session = request.getSession(false);

		if (session != null) {

			session.invalidate(); // killing the session if exist
		}

		session = request.getSession(true); // create a new session for a new client
		// getting the data from the Login HTML form
		String username = request.getParameter("name");
		String password = request.getParameter("pass");
		String clientType = request.getParameter("type");
		ClientType type = ClientType.valueOf(clientType.toUpperCase()); // convert String to ENUM

		try {
			CouponClientFacade facade = CouponSystem.login(username, password, type);
			if (facade != null) {
				// updating the session with the login facade
				session.setAttribute("facade", facade);
				// dispatcher to the right Page according to the Client Type
				switch (type) {
				case ADMIN:
					request.getRequestDispatcher("/admin.html").forward(request, response);
					break;

				case COMPANY:
					request.getRequestDispatcher("web/company.html").forward(request, response);
					break;

				case CUSTOMER:
					request.getRequestDispatcher("web/customer.html").forward(request, response);
					break;
				default:
					break;
				}
			}
			else {
				response.sendRedirect("login.html");
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
}