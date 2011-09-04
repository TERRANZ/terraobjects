/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.terra.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.terra.ejb.entity.EntityBeanRemote;

/**
 *
 * @author terranz
 */
public class StorageServlet extends HttpServlet {
    @EJB
    private EntityBeanRemote entityBean;
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
	
        try {
            if (request.getParameter("do") != null)
	    {
		storeHash(request.getParameter("name"), request.getParameter("hash"));
	    }
        } finally { 
            out.close();
        }
    } 

    private void storeObject()
    {

    }

    private void storeHash(String name,String hash)
    {
	Integer newobjId = entityBean.createNewObjectByTemplate(2);
	entityBean.setPropertyValue(newobjId, 1, name);
	entityBean.setPropertyValue(newobjId, 3, hash);
    }

    private void loadObject()
    {

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
