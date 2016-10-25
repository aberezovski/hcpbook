package com.iquestgroup.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.iquestgroup.ejb.ReadingListManagerBeanLocal;

/**
 * Servlet implementation class ReadingListServlet
 */
@WebServlet(ReadingListServlet.READING_LIST_SERVLET)
public class ReadingListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String READING_LIST_JSP = "jsp/readingList.jsp";
	public static final String READING_LIST_SERVLET = "/ReadingListServlet";

	private static final String READING_LIST_PARAM = "readingList";
	private static final String READING_LIST_MANAGER = "readingListManager";
	private static final String EJB_REF_NAME_READING_LIST_MANAGER = "ejb/ReadingListManager";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReadingListServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Fetch the EJB list manager:
		ReadingListManagerBeanLocal readingListManager = getListManager(request.getSession());

		// Retrieve the target action from the request parameters:
		String action = request.getParameter("action");
		if ("Delete".equals(action)) {
			String title = request.getParameter("title");
			readingListManager.removeTitle(title);
		
			// Re-route the user back to the main reading list form:
			response.sendRedirect(request.getContextPath() + READING_LIST_SERVLET);
		
		} else {
			// Store the reading list in request context so we can bind it to an HTML
			// table in the JSP:
			request.setAttribute(READING_LIST_PARAM, loadReadingList(request));
			
			forward(READING_LIST_JSP, request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Fetch the EJB list manager:
		ReadingListManagerBeanLocal readingListManager = getListManager(request.getSession());

		// Add the selected title to the list:
		String title = request.getParameter("title");
		if (title != null && !"".equals(title)) {
			readingListManager.addTitle(title);
		}

		// Re-route the user back to the main reading list form:
		response.sendRedirect(request.getContextPath() + READING_LIST_SERVLET);
	}

	private ReadingListManagerBeanLocal getListManager(HttpSession session) {
		// Access/create the reading list manager EJB from the session context:
		ReadingListManagerBeanLocal readingListManager = null;
		readingListManager = (ReadingListManagerBeanLocal) session.getAttribute(READING_LIST_MANAGER);
		if (readingListManager == null) {
			try {
				// If the EJB hasn't been created yet, go ahead and create it:
				InitialContext ctx = new InitialContext();
				readingListManager = (ReadingListManagerBeanLocal) ctx
						.lookup(String.format("java:comp/env/%s", EJB_REF_NAME_READING_LIST_MANAGER));

				readingListManager.addTitle("OData and SAP NetWeaver Gateway");
				readingListManager.addTitle("SAP HANA: An Introduction");
				readingListManager.addTitle("SuccessFactors with SAP ERP HCM");

				session.setAttribute(READING_LIST_MANAGER, readingListManager);
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}
		return readingListManager;
	}

	private void forward(String jspPage, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Reroute the user back to the main reading list form:
		RequestDispatcher rd = request.getRequestDispatcher(jspPage);
		rd.forward(request, response);
	}
	
	private List<String> loadReadingList(HttpServletRequest request) {
		try {
			// Fetch the reading list session EJB from session context:
			ReadingListManagerBeanLocal readingListManager = (ReadingListManagerBeanLocal) request.getSession()
					.getAttribute(READING_LIST_MANAGER);

			// Use the EJB handle to lookup the user's reading list:
			if (readingListManager != null) {
				return readingListManager.getReadingList();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return new ArrayList<String>();
	}

}
