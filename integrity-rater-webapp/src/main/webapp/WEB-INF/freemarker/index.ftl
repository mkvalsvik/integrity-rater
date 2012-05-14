<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">

<head>
	<#include "includes/head.ftl" />
</head>
<body>
	<#include "includes/top-nav.ftl" />
	
	<div id="bodyContent">
	
		<div id="mainColumn">
			<div id="searchBox">
				<form>
					<input size="24" type="text" id="autocomplete" value="Enter a name.." />&nbsp;
					<input class="button" type="submit" value="Search"/>
				</form>
				<p>
					Showing <span id="filterName">${complaints?size} complaint<#if (complaints?size > 1)>s</#if></span> against 
						<#if person??>
							${person.name} <a href="index">(show everyone)</a><br/>
							<span class="entryMenu"><a href="complaint-edit?personId=${person.id?c}">File a new complaint</a><#if !person.user> | <a href="signup?subject=${person.id}">Claim ${person.name}</a></#if></span>
						<#else>${complaintPersonCount} <#if (complaintPersonCount > 1)>people<#else>person</#if>
						</#if>
				</p>
			</div>	
			
			<#if message??>
				<div class="errorMessage">
				<#if message.success>
					<p class="successMessage">${message.text}</p>
				<#else>
					<p class="failureMessage">${message.text}</p>
				</#if>
				</div>
			</#if>
			
			<!-- TODO: Move this div to an include -->
			<div id="complaintBox">
				<#if complaints?has_content>
					<ul>	
					<#list complaints as complaint>
					<li class="complaintEntry">
						<h2><a href="complaint?complaintId=${complaint.id?c}">${complaint.title}</a></h2>
						<h3>A complaint about <a href="person?personId=${complaint.person.id?c}">${complaint.person.name}</a></h3>
						<h4>- By <a href="person?personId=${complaint.createdBy.id?c}">${complaint.createdBy.name}</a> <#if complaint.votes?has_content>with support from 
							<#list complaint.votes as vote>
								<a href="person?personId=${vote.createdBy.id?c}">${vote.createdBy.name}</a><#if vote_has_next>, </#if>
							</#list></#if>
							| Filed ${complaint.lastModifiedTime?date}
						</h4>
						<p><span>${complaint.date1?date?string.long}:</span><br/>
							${complaint.quote1}<br/>
							<span class="grey">${complaint.source1}</span>
						</p>
						<p><span>${complaint.date2?date?string.long}:</span><br/>
							${complaint.quote2}<br/>
							<span class="grey">${complaint.source2}</span>
						</p>
						<div class="challengeBox">
							<#if complaint.challenges?has_content>
							<h4>Challenges:</h4>
							<ul>
							<#list complaint.challenges as challenge>
							<li>
								<h3><a href="challenge?challengeId=${challenge.id?c}">${challenge.title}</a> ${challenge.lastModifiedTime}</h3>
							</li>
							</#list>
							</ul>
							</#if>
						</div>
						
						<h4><#if ( !loggedInUser?? ) || ( !complaint.isVoted() )><a id="complaintVoteLink_${complaint.id?c}" class="voteLink" href="complaint-vote.json?complaintId=${complaint.id?c}">Add your support</a> | </#if><a href="challenge-edit?complaintId=${complaint.id?c}">Add a challenge</a><#if ( loggedInUser?? ) && (loggedInUser.id == complaint.createdBy.id)> | <a href="complaint-edit?complaintId=${complaint.id?c}">Edit</a></#if></h4>
						
					</li>
					</#list>
					</ul>	
				<#else>			
					<p>No complaints found <#if person??> for ${person.name} | <a href="complaint-edit?personId=${person.id?c}">Add one</a></#if></p>
				</#if> 		
			</div>
		</div> <!-- mainColumn -->
		
		<div id="leftColumn">					
			<div id="leftBox1">
				Worst ratings:
				<ol>
				<#list persons as person>
					<li><a href="index?personId=${person.id?c}">${person.name}</a> (${person.integrityScore})</li>
				</#list>
				</ol>
			</div>
		</div>
		
		
		
	</div> <!-- bodyContent -->	
</body>
</html>