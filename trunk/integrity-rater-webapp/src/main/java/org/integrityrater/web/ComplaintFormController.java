package org.integrityrater.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.catamarancode.spring.mvc.DisplayMessage;
import org.integrityrater.entity.AuditEntry;
import org.integrityrater.entity.Complaint;
import org.integrityrater.entity.Person;
import org.integrityrater.entity.support.Factory;
import org.integrityrater.entity.support.Repository;
import org.integrityrater.entity.type.ComplaintType;
import org.integrityrater.web.support.ControllerUtils;
import org.integrityrater.web.support.HttpSessionUtils;
import org.integrityrater.web.support.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;


/**
 * See http://blog.jteam.nl/2009/05/14/simple-forms-with-spring-mvc-2-5/
 * @author mkvalsvik
 *
 */
@Controller
@RequestMapping("/complaint-edit")
public class ComplaintFormController {
    
    @Resource
    private Repository repository;
    
    @ModelAttribute("complaintForm")
    public ComplaintForm formBackingObject(Long complaintId, Long personId) {
        Complaint complaint = null;
        if (complaintId != null && complaintId != 0) {
            complaint = repository.loadComplaint(complaintId);
        } else {
            if (personId != null && personId != 0) {
                Person person = repository.loadPerson(personId);
                complaint = Factory.createComplaint(person);
            } else {
                complaint = Factory.createComplaint();   
            }            
        }
        ComplaintForm form = new ComplaintForm();
        form.setComplaint(complaint);
        return form;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, @ModelAttribute("complaintForm") ComplaintForm complaintForm) {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request, repository);        
        HttpSessionUtils.addLoggedInUserToModel(request, repository, mv);
        mv.addObject("types", ComplaintType.values());
        return mv;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView submit(HttpServletRequest request,
            @ModelAttribute("complaintForm") ComplaintForm complaintForm,
            BindingResult result, SessionStatus sessionStatus, ModelMap model) {
        
        Person user = HttpSessionUtils.retrieveLoggedInUser(request, repository);
        Complaint complaint = complaintForm.getComplaint();
        
        // Did user enter a name in autocomplete?
        Person person = null;
        if (StringUtils.hasText(complaintForm.getComplaintTargetName())) {
            List<Person> persons = repository.findPersonByPartialName(complaintForm.getComplaintTargetName());
            if (persons.size() > 2) {
                throw new RuntimeException("TODO: Show a modal that lets user pick from multiple persons");
            } else if (!persons.isEmpty()) {
                person = persons.iterator().next();
            }
        }

        // Save person if new
        if (person != null) {
            complaint.setPerson(person);
        } else {
            person = complaint.getPerson();
            
            // Override name with one-field value?
            if (StringUtils.hasText(complaintForm.getComplaintTargetName())) {
                String[] parts = complaintForm.getComplaintTargetName().split(" ");
                if (parts.length < 2) {
                    ModelAndView mv = new ModelAndView();
                    DisplayMessage.addToThisPage(mv, new DisplayMessage("Invalid target name: " + complaintForm.getComplaintTargetName(), false));
                    return mv;
                } else if (parts.length == 2) {
                    person.getName().setFirst(parts[0]);
                    person.getName().setLast(parts[1]);
                } else {
                    // TODO: Handle names like "George Herbert Walker Bush Sr"
                    person.getName().setFirst(parts[0]);
                    person.getName().setFirst(parts[1]);
                    person.getName().setLast(parts[2]);
                }
            }
            
            person.save();    
        }
        
        complaint.setCreatedBy(user);
        complaint.save();
        
        AuditEntry.insert(user.getId(), complaint.getPerson().getId(), complaint.getId(), AuditEntry.TransactionType.COMPLAINT_CREATE);
        Message.addPending(request, "Complaint saved", true);

        return new ModelAndView("redirect:index");
    }

}
