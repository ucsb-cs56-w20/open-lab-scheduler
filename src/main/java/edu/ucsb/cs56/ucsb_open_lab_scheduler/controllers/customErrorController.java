package edu.ucsb.cs56.ucsb_open_lab_scheduler.controllers;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class customErrorController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

  @GetMapping("/error")
  public String handleError(Model model, HttpServletRequest request, RedirectAttributes redirAttrs) {
      ServletWebRequest servletWebRequest = new ServletWebRequest(request);
      Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
      Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(servletWebRequest, true); 
      if (status != null) {
          Integer statusCode = Integer.valueOf(status.toString());
       
          if(statusCode == HttpStatus.NOT_FOUND.value()) {
            model.addAttribute("timestamp",  errorAttributes.get("timestamp").toString());
            model.addAttribute("status",  HttpStatus.NOT_FOUND.value());
            model.addAttribute("error", "error-404 HttpStatus.NOTFOUND");
            model.addAttribute("message",  errorAttributes.get("message").toString()); 
  
          }
          else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            model.addAttribute("timestamp",  errorAttributes.get("timestamp").toString());
            model.addAttribute("status",  HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addAttribute("error", "error-405 HttpStatus.INTERNAL_SERVER_ERROR");
            model.addAttribute("message",  errorAttributes.get("message").toString());
          }
      }else{
      //   final StringBuilder errorDetails = new StringBuilder();
      model.addAttribute("timestamp",  errorAttributes.get("timestamp").toString());
      model.addAttribute("status",  errorAttributes.get("status").toString()); 
      model.addAttribute("error",  errorAttributes.get("error").toString());
      model.addAttribute("message",  errorAttributes.get("message").toString());
      }
      return "error";
  }
    @Override
    public String getErrorPath(){
        return "/error";
    }
}