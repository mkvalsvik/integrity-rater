package org.integrityrater.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.catamarancode.type.Name;
import org.catamarancode.type.State;
import org.integrityrater.entity.AuditEntry;
import org.integrityrater.entity.Challenge;
import org.integrityrater.entity.ChallengeVote;
import org.integrityrater.entity.Complaint;
import org.integrityrater.entity.ComplaintVote;
import org.integrityrater.entity.Person;
import org.integrityrater.entity.support.Repository;
import org.integrityrater.web.support.ControllerUtils;
import org.integrityrater.web.support.HttpSessionUtils;
import org.integrityrater.web.support.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Resource
    private Repository repository;

    @RequestMapping("/")
    public ModelAndView home(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return index(request, response);
    }

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository, "index");

        String personId = request.getParameter("personId");
        String personName = request.getParameter("personName");
        if (StringUtils.hasText(personId)) {
            Person person = repository.loadPerson(Long.parseLong(personId));
            mv.addObject("complaints", person.getComplaints());
            mv.addObject("person", person);
        } else if (StringUtils.hasText(personName)) {
            String[] nameParts = personName.split(" ");
            List<Person> persons = null;
            if (nameParts.length > 1) {
                persons = repository.findPersonByPartialName(nameParts[0], nameParts[1]);    
            } else {
                persons = repository.findPersonByPartialLastName(nameParts[0]);
                if (persons.isEmpty()) {
                    persons = repository.findPersonByPartialFirstName(nameParts[0]);
                }
            }            
                    
            if (persons.size() > 1) {
                logger.warn("\n\n\n*******\nMultiple matches for " + personName);
            }
            Person person = persons.iterator().next();
            mv.addObject("complaints", person.getComplaints());
            mv.addObject("person", person);
        } else {
            List<Complaint> complaints = repository.listAllComplaints();
            mv.addObject("complaints", complaints);
            
            // How many people do we have complaints for in results?
            int personCount = 0;
            long lastPersonId = 0;
            for (Complaint complaint : complaints) {
                if (complaint.getPerson().getId().longValue() != lastPersonId) {
                    personCount++;
                    lastPersonId = complaint.getPerson().getId();
                }
            }
            mv.addObject("complaintPersonCount", personCount);
        }

        // TODO: Improve speed, this is dog slow!!!
        List<Person> persons = repository.listPersons();
        List<Person> personsWithComplaints = new ArrayList<Person>();
        for (Person person : persons) {
            if (!person.getComplaints().isEmpty()) {
                personsWithComplaints.add(person);
            }
        }

        // TODO: Move this offline to a scheduled process
        // Compute ratings
        for (Person person : personsWithComplaints) {
            double score = 0;
            for (Complaint complaint : person.getComplaints()) {
                score = score + 2;
                for (ComplaintVote vote : complaint.getVotes()) {
                    score = score + 1;
                }
                for (Challenge challenge : complaint.getChallenges()) {
                    score = score - 1;
                    for (ChallengeVote vote : challenge.getVotes()) {
                        score = score - 0.5;
                    }
                }
            }
            person.setIntegrityScore(-1 * score);
            person.save();
        }

        // Sort by scores
        Collections.sort(personsWithComplaints, new Comparator() {
            public int compare(Object a, Object b) {
                Person pA = (Person) a;
                Person pB = (Person) b;
                if (pA.getIntegrityScore() > pB.getIntegrityScore()) {
                    return 1;
                }
                return -1;
            }
        });

        mv.addObject("persons", personsWithComplaints);

        return mv;
    }

    @RequestMapping("/complaints")
    public ModelAndView complaints(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository);
        mv.addObject("complaints", repository.listAllComplaints());
        return mv;
    }

    @RequestMapping("/complaint-vote.json")
    public ModelAndView complaintVote(
            HttpServletRequest request,
            @RequestParam(value = "complaintId", required = true) Long complaintId)
            throws Exception {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository, "json-string");
        Complaint complaint = repository.loadComplaint(complaintId);

        ComplaintVote vote = new ComplaintVote();
        vote.setComplaint(complaint);
        vote.setCreatedBy(HttpSessionUtils.retrieveLoggedInUser(request,
                repository));
        vote.setCreatedTime(new Date());
        vote.save();

        List<ComplaintVote> totalVotes = complaint.getVotes();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("totalVotes", totalVotes.size());
        jsonObj.put("responseCode", 1);
        jsonObj.put("complaintId", complaint.getId());
        mv.addObject("json", jsonObj);
        return mv;
    }

    @RequestMapping("/challenge-vote.json")
    public ModelAndView challengeVote(HttpServletRequest request,
            @RequestParam(required = true) Long challengeId) throws Exception {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository, "json-string");
        Challenge challenge = repository.loadChallenge(challengeId);

        ChallengeVote vote = new ChallengeVote();
        vote.setChallenge(challenge);
        vote.setCreatedBy(HttpSessionUtils.retrieveLoggedInUser(request,
                repository));
        vote.setCreatedTime(new Date());
        vote.save();

        List<ChallengeVote> totalVotes = challenge.getVotes();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("totalVotes", totalVotes.size());
        jsonObj.put("responseCode", 1);
        mv.addObject("json", jsonObj);
        return mv;
    }

    @RequestMapping("/person-lookup.json")
    public ModelAndView personLookup(HttpServletRequest request,
            @RequestParam(required = true) String term) throws Exception {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository, "json-string");

        // Extract first and last from search term
        boolean firstLastDelimited = false;
        String firstName = null;
        String lastName = null;
        int commaPos = term.indexOf(',');
        int spacePos = term.indexOf(' ');
        if (commaPos > 0) {
            firstLastDelimited = true;
            lastName = term.substring(0, commaPos);
            if (term.length() > commaPos) {
                firstName = term.substring(commaPos + 1);
            }
        } else if (spacePos > 0) {
            firstLastDelimited = true;
            firstName = term.substring(0, spacePos);
            if (term.length() > spacePos) {
                lastName = term.substring(spacePos);
            }
        }

        // Perform DB search
        List<String> results = new ArrayList<String>();
        if (firstLastDelimited) {
            List<Person> persons = Collections.EMPTY_LIST;
            if (firstName != null && lastName != null) {
                persons = repository.findPersonByPartialName(firstName,
                        lastName);
            } else if (firstName != null) {
                persons = repository.findPersonByPartialFirstName(firstName);
            } else if (lastName != null) {
                persons = repository.findPersonByPartialLastName(lastName);
            }
            for (Person person : persons) {
                results.add(person.getName().asFullName());
            }
        } else {

            // We have no way of knowing whether user typed firstname or
            // lastname. So we include both in our search.
            List<Person> personsByFirst = repository
                    .findPersonByPartialFirstName(term);
            for (Person person : personsByFirst) {
                results.add(person.getName().asFullName());
            }
            List<Person> personsByLast = repository
                    .findPersonByPartialLastName(term);
            for (Person person : personsByLast) {
                results.add(person.getName().getLast() + ", "
                        + person.getName().getFirst());
            }
        }

        // Sort the list
        Collections.sort(results);

        // Build results
        JSONArray json = new JSONArray();
        for (String name : results) {
            json.add(name);
        }
        mv.addObject("json", json);
        return mv;
    }

    @RequestMapping("/about")
    public ModelAndView about(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository);
        return mv;
    }

    @RequestMapping("/person")
    public ModelAndView person(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository, "person");

        // Lookup in db
        String personId = request.getParameter("personId");
        Person person = repository.loadPerson(Long.parseLong(personId));
        mv.addObject("person", person);

        // Complaints
        List<Complaint> complaints = repository.listComplaints(person);
        mv.addObject("complaints", complaints);

        return mv;
    }

    @RequestMapping("/people")
    public ModelAndView people(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository);

        // Lookup in db
        List<Person> persons = repository.listPersons();
        mv.addObject("persons", persons);

        return mv;
    }

    private String[] extractNameFromServletPath(HttpServletRequest request) {
        String[] parts = request.getServletPath().split("/");
        String name = parts[2].trim();
        name = name.replaceAll("\\+", " ");
        name = name.replaceAll("_", " ");
        name = name.replaceAll("\\.", " ");
        String[] nameParts = name.split(" ");
        return nameParts;
    }

    @RequestMapping("/person/*")
    public ModelAndView personByName(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // Setting view name explicitly
        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository, "person");

        // Name
        String[] nameParts = extractNameFromServletPath(request);
        String firstName = nameParts[0];
        String lastName = nameParts[1];

        // Lookup in db
        Person person = null;
        List<Person> personList = repository.findPersonByName(firstName,
                lastName);
        if (!personList.isEmpty()) {
            person = personList.iterator().next();
            mv.addObject("person", person);
        }

        // Complaints
        if (person != null) {
            List<Complaint> complaints = repository.listComplaints(person);
            mv.addObject("complaints", complaints);
        }

        return mv;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public void signup(HttpServletRequest request, ModelMap model) {

        if (HttpSessionUtils.isUserLoggedIn(request)) {
            Message.addToModel(model, String.format(
                    "You are already logged in as %s", HttpSessionUtils
                            .retrieveLoggedInUser(request, repository)
                            .getName()), false);
            return;
        }

        // Claim?
        String subjectId = (String) request.getParameter("subject");
        if (StringUtils.hasText(subjectId)) {

            // Add person id to model so that we can validate the claim on form
            // submit
            populateSignupForm(model, Long.valueOf(subjectId));
            ;
        } else {
            populateSignupForm(model, null);
            ;
        }

        Message.addPendingToView(request, model);
    }

    private void populateSignupForm(ModelMap model, Long subjectId) {

        SignupForm form = (SignupForm) model.get(SignupForm.KEY);
        if (form == null) {
            form = new SignupForm();
            model.addAttribute(SignupForm.KEY, form);
        }

        if (subjectId != null) {
            form.setSubjectId(subjectId);
            Person subject = repository.loadPerson(subjectId);
            form.setFirstName(subject.getName().getFirst());
            form.setLastName(subject.getName().getLast());
        }

        // Drop-down values: States
        model.addAttribute("states", State.values());
    }

    @RequestMapping(value = "/signup-submit", method = RequestMethod.POST)
    public String signupSubmit(HttpServletRequest request,
            @ModelAttribute(SignupForm.KEY) SignupForm signupForm,
            BindingResult result, SessionStatus sessionStatus, ModelMap model) {

        // Duplicate email check
        Person person = repository.findPersonByEmail(signupForm.getEmail());
        if (person != null) {
            populateSignupForm(model, null);
            // TODO: Direct user to forgot password
            Message.addToModel(model, "Email already registered: "
                    + signupForm.getEmail(), false);
            return "signup";
        }

        Name name = new Name();
        name.setFirst(signupForm.getFirstName());
        name.setLast(signupForm.getLastName());

        // Claiming a name?
        if (signupForm.getSubjectId() != null) {

            // Yes. TODO: Perform CC check
            person = repository.loadPerson(signupForm.getSubjectId());
            person.setEmail(signupForm.getEmail());
            person.setCleartextPassword(signupForm.getPassword());
            person.save();
            AuditEntry.insert(person.getId(), person.getId(), -1,
                    AuditEntry.TransactionType.PERSON_CLAIM);

        } else {

            // TODO: Perform duplicate name check (generate a unique display
            // name)
            person = new Person();
            person.setCleartextPassword(signupForm.getPassword());
            person.setEmail(signupForm.getEmail());
            person.setName(name);
            person.save();
            AuditEntry.insert(person.getId(), person.getId(), -1,
                    AuditEntry.TransactionType.PERSON_SIGNUP);
            AuditEntry.insert(person.getId(), person.getId(), -1,
                    AuditEntry.TransactionType.PERSON_CREATE);

        }

        // Put user in session
        HttpSessionUtils.storeLoggedInUser(request, person);

        // Return
        sessionStatus.setComplete();
        model.clear();
        return String.format("redirect:/index");
    }

    @RequestMapping("/complaint")
    public ModelAndView complaint(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository);
        Complaint complaint = repository.loadComplaint(Long.parseLong(request
                .getParameter("complaintId")));
        mv.addObject("complaint", complaint);
        return mv;
    }

    /*
     * @RequestMapping(value = "/complaint-submit", method = RequestMethod.POST)
     * public String complaintSubmit(HttpServletRequest request,
     * 
     * @ModelAttribute(ComplaintForm.KEY) ComplaintForm complaintForm,
     * BindingResult result, SessionStatus sessionStatus, ModelMap model) {
     * 
     * Person user = HttpSessionUtils.retrieveLoggedInUser(request, repository);
     * Complaint complaint = complaintForm.getComplaint(); Person person =
     * complaint.getPerson(); person.save(); complaint.save();
     * 
     * AuditEntry.insert(user.getId(), person.getId(), complaint.getId(),
     * AuditEntry.TransactionType.COMPLAINT_CREATE); Message.addPending(request,
     * "Complaint saved", true);
     * 
     * return "redirect:index"; }
     * 
     * @RequestMapping("/complaint-edit") public ModelAndView
     * complaintEdit(HttpServletRequest request, HttpServletResponse response)
     * throws Exception {
     * 
     * Person user = HttpSessionUtils.retrieveLoggedInUser(request, repository);
     * Complaint complaint = null; String complaintId =
     * request.getParameter("complaint"); if (StringUtils.hasText(complaintId))
     * { complaint = repository.loadComplaint(Long.parseLong(complaintId)); }
     * else { complaint = Factory.createComplaint(); }
     * 
     * ComplaintForm complaintForm = new ComplaintForm();
     * complaintForm.setComplaint(complaint);
     * 
     * ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
     * repository); mv.addObject(ComplaintForm.KEY, complaintForm); return mv; }
     */

    @RequestMapping("/challenge")
    public ModelAndView challenge(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ModelAndView mv = ControllerUtils.createDefaultModelAndView(request,
                repository);
        Challenge challenge = repository.loadChallenge(Long.parseLong(request
                .getParameter("challengeId")));
        mv.addObject("challenge", challenge);
        return mv;
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return signout(request, response);
    }

    @RequestMapping("/signout")
    public ModelAndView signout(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSessionUtils.removeLoggedInUser(request);
        ModelAndView mv = new ModelAndView("redirect:index");
        return mv;
    }

    @RequestMapping("/signin")
    public ModelAndView signin(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (request.getMethod().equalsIgnoreCase("GET")) {
            ModelAndView mv = new ModelAndView();
            return mv;
        }

        // Begin POST
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Person user = repository.findPersonByEmail(email);
        if (user == null) {
            ModelAndView mv = new ModelAndView();
            Message.addToModel(mv.getModelMap(), "Invalid email or password",
                    false);
            return mv;
        }
        if (!user.passwordMatches(password)) {
            ModelAndView mv = new ModelAndView();
            Message.addToModel(mv.getModelMap(), "Invalid email or password",
                    false);
            return mv;
        }

        HttpSessionUtils.storeLoggedInUser(request, user);
        ModelAndView mv = new ModelAndView("redirect:index");
        return mv;
    }

}
