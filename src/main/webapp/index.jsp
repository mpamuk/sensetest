<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%=com.sense360.search.SearchServices.place(request.getParameter("latitude"),request.getParameter("longitude"), request.getParameter("radius"))%>
