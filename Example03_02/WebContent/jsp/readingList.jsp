<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="javax.ejb.EJB"%>
<%@ page import="javax.naming.InitialContext"%>
<%@page import="com.iquestgroup.ejb.ReadingListManagerBeanLocal"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>HCP Book :: Chapter 3 :: My HCP Reading List</title>
<link href="css/style.css" rel="stylesheet">
</head>
<body>
	<h1>My HCP Reading List</h1>

	<form method="post" action="ReadingListServlet">
		<table class="readingList">
			<tr>
				<th>Book Title</th>
				<th>Action</th>
			</tr>

			<c:forEach var="book" items="${readingList}">
				<tr>
					<td>${book}</td>
					<td><a
						href="ReadingListServlet?action=Delete&title=${book}">Remove</a>
					</td>
				</tr>
			</c:forEach>
		</table>

		<section>
			<div>
				<label for="inpBook">Book: <input id="addBook" name="title" /></label>&nbsp;<input
					type="submit" name="action" value="Add" />
			</div>
		</section>
	</form>
</body>
</html>