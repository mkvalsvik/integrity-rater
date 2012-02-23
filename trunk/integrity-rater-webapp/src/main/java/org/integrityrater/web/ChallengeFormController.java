package org.integrityrater.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.integrityrater.entity.AuditEntry;
import org.integrityrater.entity.Challenge;
import org.integrityrater.entity.Complaint;
import org.integrityrater.entity.Person;
import org.integrityrater.entity.support.Factory;
import org.integrityrater.entity.support.Repository;
import org.integrityrater.web.support.ControllerUtils;
import org.integrityrater.web.support.HttpSessionUtils;
import org.integrityrater.web.support.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
@RequestMapping("/challenge-edit")
public class ChallengeFormController {
    
    @Resource
    private Repository repository;
    
    @ModelAttribute("challengeForm")
    public ChallengeForm formBackingObject(Long complaintId, Long challengeId) {
        Challenge challenge = null;
        if (challengeId != null &&challengeId != 0) {
            challenge = repository.getChallenge(challengeId);
        } else if (complaintId != null && complaintId != 0) {
            Complaint complaint = repository.loadComplaint(complaintId);
            challenge = Factory.createChallenge(complaint);
        } else {
            throw new UnsupportedOperationException("ChallengeId or complaintId required");
        }
        ChallengeForm form = new ChallengeForm();
        form.setChallenge(challenge);
        return form;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, @ModelAttribute("challengeForm") ChallengeForm challengeForm) {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request, repository);        
        //mv.addObject(ChallengeForm.KEY, challengeForm);
        HttpSessionUtils.addLoggedInUserToModel(request, repository, mv);
        return mv;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String submit(HttpServletRequest request,
            @ModelAttribute("challengeForm") ChallengeForm challengeForm,
            BindingResult result, SessionStatus sessionStatus, ModelMap model) {
        
        Person user = HttpSessionUtils.retrieveLoggedInUser(request, repository);
        Challenge challenge = challengeForm.getChallenge();
        challenge.setCreatedBy(user);
        challenge.save();
        
        AuditEntry.insert(user.getId(), challenge.getComplaint().getPerson().getId(), challenge.getId(), AuditEntry.TransactionType.CHALLENGE_CREATE);
        Message.addPending(request, "Challenge saved", true);
        
        return "redirect:index";
    }

}
