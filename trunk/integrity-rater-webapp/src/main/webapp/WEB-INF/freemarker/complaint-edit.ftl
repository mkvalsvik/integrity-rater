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
		<form action="complaint-edit" method="post">
		    <fieldset>
		        <legend>Target of complaint</legend>
		        <#if complaintForm.complaint.person.name.first??>
					<div>
			        	<label>Name</label>
			        	<span>${complaintForm.complaint.person.name}</span>
					</div>
				<#else>
					<div>
						<@spring.bind "complaintForm.complaintTargetName" />
						<label for="${spring.status.expression}">* Name</label>
						<input name="${spring.status.expression}" size="24" type="text" id="autocomplete" />
					</div>
				</#if>
					
				<#if (complaintForm.complaint.person.id)?? && complaintForm.complaint.person.id != 0>
					<input type="hidden" name="personId" value="${complaintForm.complaint.person.id?c}" />
				</#if>
			</fieldset>
			<fieldset>
		        <legend>Details of complaint</legend>
		        <#--
				<div>
					<@spring.bind "complaintForm.complaint.type" />
			        <label for="${spring.status.expression}">* Type</label>
			        
					<select id="${spring.status.expression}" name="${spring.status.expression}">
						<option value=""<@spring.checkSelected ""/>>-- SELECT COMPLAINT TYPE --</option>
					    <#list types as option>
				    		<option value="${option}"<@spring.checkSelected option/>>${option.summary}</option>
				        </#list>
					</select>
				</div>
				-->	
				<div>
					<@spring.bind "complaintForm.complaint.title" />
			        <label for="${spring.status.expression}">* Title</label>
			        <input size="48" type="text" name="${spring.status.expression}" value="${spring.status.value!}" />
					<span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
				</div>
				<fieldset>
			        <legend>Original position</legend>
					<div>
						<@spring.bind "complaintForm.complaint.date1" />
				        <label for="${spring.status.expression}">* Date</label>
				        <input type="text" name="${spring.status.expression}" value="${spring.status.value!}" />
				        <span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
					</div>
					<div>
						<@spring.bind "complaintForm.complaint.quote1" />
				        <label for="${spring.status.expression}">* Position summary or quote</label>
				        <textarea rows="10" cols="90" name="${spring.status.expression}">${spring.status.value!}</textarea>
						<span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
					</div>
					<div>
						<@spring.bind "complaintForm.complaint.source1" />
				        <label for="${spring.status.expression}">* Source link</label>
				        <input size="60" type="text" name="${spring.status.expression}" value="${spring.status.value!}" />
						<span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
					</div>
				</fieldset>	
				<fieldset>
			        <legend>Conflicting position</legend>
					<div>
						<@spring.bind "complaintForm.complaint.date2" />
				        <label for="${spring.status.expression}">* Date</label>
				        <input type="text" name="${spring.status.expression}" value="${spring.status.value!}" />
				        <span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
					</div>
					<div>
						<@spring.bind "complaintForm.complaint.quote2" />
				        <label for="${spring.status.expression}">* Position summary or quote</label>
				        <textarea rows="10" cols="90" name="${spring.status.expression}">${spring.status.value!}</textarea>
						<span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
					</div>
					<div>
						<@spring.bind "complaintForm.complaint.source2" />
				        <label for="${spring.status.expression}">* Source link</label>
				        <input size="60" type="text" name="${spring.status.expression}" value="${spring.status.value!}" />
						<span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
					</div>
				</fieldset>			
				<fieldset>
			        <legend>Hat Tip (additional sources who deserve credit)</legend>
					<div>
						<@spring.bind "complaintForm.complaint.hatTipA" />
				        <label for="${spring.status.expression}">Hat Tip</label>
				        <input type="text" name="${spring.status.expression}" value="${spring.status.value!}" />
				        <span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
					</div>
					<div>
						<@spring.bind "complaintForm.complaint.hatTipB" />
				        <label for="${spring.status.expression}">Hat Tip</label>
				        <input type="text" name="${spring.status.expression}" value="${spring.status.value!}" />
				        <span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
					</div>
					<div>
						<@spring.bind "complaintForm.complaint.hatTipB" />
				        <label for="${spring.status.expression}">Hat Tip</label>
				        <input type="text" name="${spring.status.expression}" value="${spring.status.value!}" />
				        <span class="fieldErrors"><#list spring.status.errorMessages as error>${error}</b><#if error_has_next>,</#if></#list></span>
					</div>
				</fieldset>
			</fieldset>	
						
			<#if complaintForm.complaint.id?? && complaintForm.complaint.id != 0>
				<input type="hidden" name="complaintId" value="${complaintForm.complaint.id?c}" />
			</#if>
			<input type="submit" value="Save" />	
			
		</form>

	</div> <!-- bodyContent -->
</body>
</html>