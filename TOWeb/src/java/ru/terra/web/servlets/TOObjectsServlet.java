/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.terra.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.terra.ejb.entity.EntityBeanRemote;
import ru.terra.ejb.entity.TOObject;
import ru.terra.ejb.entity.TOObjectProperty;

/**
 *
 * @author terranz
 */
public class TOObjectsServlet extends HttpServlet {
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
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TOObjectsServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TOObjectsServlet at " + request.getContextPath () + "</h1>");

	    for (int i = 0;i<=100;i++)
	    {
		//entityBean.createNewObjectByTemplate(1);
	    }
	    List<TOObject> objects = entityBean.findAllObjects();
	    for (TOObject o : objects)
	    {
		out.println(o.getObjectId());
		printObjectProps(o.getObjectId(), out);
		out.println("<br>");
	    }
            out.println("</body>");
            out.println("</html>");
        } finally { 
            out.close();
        }
    } 

    private void printObjectProps(Integer oid,PrintWriter out)
    {
	List<TOObjectProperty> props = entityBean.getObjectProps(oid);
	for (TOObjectProperty p : props)
	{
	    out.print(entityBean.getPropertyValue(oid, p.getPropertyId()));
	}
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
