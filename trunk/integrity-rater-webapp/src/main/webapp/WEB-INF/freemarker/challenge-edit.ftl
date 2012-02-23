<#import "/spring.ftl" as spring />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">

<head>
	<#include "includes/head.ftl" />
</head>
<body>
<#include "includes/top-nav.ftl" />

	<div id="bodyContent">
		<form action="challenge-edit" method="post">
		    <fieldset>
		        <legend>Person</legend>
				<div>					
			        <label>Name</label>
			        ${challengeForm.challenge.complaint.person.name}
				</div>
			</fieldset>
			<fieldset>
		        <legend>Complaint</legend>
				<div>					
			        <label>Title</label>
			        ${challengeForm.challenge.complaint.title}
				</div>
				<div>
			        <label>Body</label>
			        ${challengeForm.challenge.complaint.body}
				</div>
			</fieldset>	
			
			<fieldset>
		        <legend>Challenge</legend>
				<div>
					<@spring.bind "challengeForm.challenge.title" />
			        <label for="${spring.status.expression}">* Title</label>
			        <input type="text" name="${spring.status.expression}" value="${spring.status.value!}" />
					<span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
				</div>
				<div>
					<@spring.bind "challengeForm.challenge.body" />
			        <label for="${spring.status.expression}">* Body</label>
			        <textarea rows="10" cols="90" name="${spring.status.expression}" value="${spring.status.value!}"></textarea>
					<span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
				</div>
			</fieldset>	
			
			<#if challengeForm.challenge.id?? && challengeForm.challenge.id != 0>
				<input type="hidden" name="challengeId" value="${challengeForm.challenge.id?c}" />
			</#if>
			<input type="hidden" name="complaintId" value="${challengeForm.challenge.complaint.id?c}" />
			<input type="submit" value="Save" />	
			
		</form>

	</div> <!-- bodyContent -->
</body>
</html>