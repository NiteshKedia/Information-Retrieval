<%@ page import="java.util.*,com.ir.process.*;" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript">
function myFunction( test )
{
	document.getElementById('textquery').value = test;
	document.getElementById('tfnewsearch').submit();
    
}
</script>
<title>Search ASU</title>
<style type="text/css">
#tfheader {
	background-color: #EFEFEF;
	float: left;
	width: 100%;
	margin: 0px;
	margin-bottom: 2px;
	margin-left: 0px;
	border: 0px;
	paddin-left: 0px;
} 

#tfnewsearch {
	float: left;
	padding: 10px;
}

.tftextinput {
	margin: 0;
	margin-left: 50px;
	padding: 5px 15px;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 14px;
	border: 1px solid #0076a3;
}

.tfbutton {
	margin: 0;
	padding: 5px 15px;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 14px;
	outline: none;
	cursor: pointer;
	text-align: center;
	text-decoration: none;
	color: #ffffff;
	border: solid 1px #0076a3;
	background: #0095cd;
	background: -webkit-gradient(linear, left top, left bottom, from(#00adee),
		to(#0078a5));
	background: -moz-linear-gradient(top, #00adee, #0078a5);
}

.tfbutton:hover {
	text-decoration: none;
	background: #007ead;
	background: -webkit-gradient(linear, left top, left bottom, from(#0095cc),
		to(#00678e));
	background: -moz-linear-gradient(top, #0095cc, #00678e);
}

.resulttextarea {
	resize: none;
	padding: 0px;
}
/* Fixes submit button height problem in Firefox */
.tfbutton::-moz-focus-inner {
	border: 0;
}

.tfclear {
	clear: both;
}



tr.row1 td {
    font-size: 20px;
    color:blue;
}

tr.row2 td {
   
    font-size: 14px;
    color:green;
}

tr.row3 td {
    padding-bottom: 20px;
    font-size: 16px;
}

</style>
</head>
<body>

	<div id="tfheader" style="width:100%;">
		<form name="submitForm" id="tfnewsearch" method="post" action="searchASU">
		<div style="float:left;width:10%"> 
		<img border="0" src="searchnew.png">
		</div>
		<div style="float:right; width:90%; "> 
			<input id="textquery" type="text" class="tftextinput" name="query" size="91" maxlength="120" value="${param.query}"> 
			<input type="submit" value="search" class="tfbutton">
		
		<div id="options" style="height: 80px; width: 100%; float: left; margin-bottom: 20px; margin-left: 50px;margin-top: 10px; "> 
		 
			<br> <input type="radio" name="option" value="VS" onclick="this.form.submit()" ${param.option == 'VS' ? 'checked' : ''} checked>Vector
			Space
			 <br><input type="radio" name="option" value="AH" onclick="this.form.submit()" ${param.option == 'AH' ? 'checked' : ''}>Authority/Hub
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Size of Root Set :
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="text" name="rootset" style="width: 55px" value=10> 
			<br><input type="radio" name="option" value="PR" onclick="this.form.submit()" ${param.option == 'PR' ? 'checked' : ''}>Page
			Rank
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Random Surfer Probability : &nbsp;&nbsp;&nbsp;<input type="text"
				name="rsprob" style="width: 55px" value=0.8>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Page Rank Weight : &nbsp;&nbsp;<input
				type="text" name="prweight" style="width: 55px" value=0.4>
		</div>
		</div>
		</form>
	</div>
	

	

	<div id="searchresult" style="height: 800px; width: 100%; float: left; margin-left: 80px;border-top: 1px solid #EFEFEF;">
	<div style="float:left; width: 50% ;">
		<br><div style="font-size: 20px;">Search Results</div><br><br>
		<table width="900" border="0" cellspacing="0" cellpadding="0"> 
		<%
		if("VS".equals(request.getParameter("option"))){
			ArrayList<DisplayObject> tfidf = null;
			tfidf = (ArrayList<DisplayObject>)request.getAttribute("TFIDF");
			if(tfidf!=null){
				for (DisplayObject d: tfidf){
					
					%> 
					<tr  class="row1"><td><div style="width: 600px" onclick ="location.href='<%=d.link%>'">
					<a href = "<%=d.link%>"><%= d.linkTitle  %></a> </div>
					</td></tr>
					<tr class="row2"><td><div style="width: 600px" >
					<%= d.url  %></div>
					</td></tr>
					<tr class="row3"><td><div style="width: 500px" >
					<%= d.snippet %></div>
					</td></tr>
					
					<%  }
				}
				}
		else if("PR".equals(request.getParameter("option"))){
			Map<Integer,Double> pagerank = null;
			pagerank = (Map<Integer,Double>)request.getAttribute("PAGERANK");
			if(pagerank!=null){
				for (Map.Entry<Integer,Double> e : pagerank.entrySet()){
					
					%>
					<tr><td>
					<%= e.getKey()  %>
					</td><td>
					<%= e.getValue() %>
					</td></tr>
					<%  }
				}
		}
		else if("AH".equals(request.getParameter("option"))){
			int[][] vectAuthHub = null;%>
			<tr><td>
			Authority
			</td><td>
			Hubs
			</td></tr><%
			vectAuthHub = (int[][])request.getAttribute("AUTHHUB");
			for(int j=0;j<vectAuthHub.length;j++){
					
					%>
					<tr><td>
					<%= vectAuthHub[j][0]  %>
					</td><td>
					<%= vectAuthHub[j][1]%>
					</td></tr>
					<%  }
				
		}
				
				
		%>
		</table>
	</div> 
	
	<div style="float:left; height: 1200px ; border-left: 1px solid #EFEFEF; " > 
	<div style="height: 300px; width: 100% ;">
	<br><div style="font-size: 20px;">Top Clusters</div><br>
		<table width=100% border="0" cellspacing="2" cellpadding="0">
		<%
			if("VS".equals(request.getParameter("option"))){
			String[][] topClusters = (String[][])request.getAttribute("clusters");
			ArrayList<Set<String>> clusDesc = (ArrayList<Set<String>>)request.getAttribute("clusterDescription");%>
				<tr><td style="color:blue; padding-top:5px">
				<%if(topClusters[0][0] !=null){%>
					<%= topClusters[0][0]%><%} %>
				</td></tr>
				<tr><td style="color:blue;">
				<%if(topClusters[0][1] !=null){%>
					<%= topClusters[0][1]%><%} %>
				</td></tr>
				<tr><td style="color:blue;">
				<%if(topClusters[0][2] !=null){%>
					<%= topClusters[0][2]%><%} %>
				</td></tr>
				<tr><td style="color:brown;">
				<%if(!clusDesc.isEmpty()){%>
					<%= clusDesc.get(0)%><%} %>
				</td></tr>
				
				<tr><td style="color:green;padding-top:5px">
				<%if(topClusters[1][0] !=null){%>
					<%= topClusters[1][0]%><%} %>
				</td></tr>
				<tr><td style="color:green;">
				<%if(topClusters[1][1] !=null){%>
					<%= topClusters[1][1]%><%} %>
				</td></tr>
				<tr><td style="color:green;">
				<%if(topClusters[1][2] !=null){%>
					<%= topClusters[1][2]%><%} %>
				</td></tr>
				<tr><td style="color:brown;">
				<%if(!clusDesc.isEmpty()){%>
					<%= clusDesc.get(1)%><%} %>
				</td></tr>
				<tr><td style="color:red;padding-top:5px">
				<%if(topClusters[2][0] !=null){%>
					<%= topClusters[2][0]%><%} %>
				</td></tr>
				<tr><td style="color:red;">
				<%if(topClusters[2][1] !=null){%>
					<%= topClusters[2][1]%><%} %>
				</td></tr>
				<tr><td style="color:red;">
				<%if(topClusters[2][2] !=null){%>
					<%= topClusters[2][2]%><%} %>
				</td></tr>
				<tr><td style="color:brown;">
				<%if(!clusDesc.isEmpty()){%>
					<%= clusDesc.get(2)%><%} %>
				</td></tr>
			
			<%
			}
		%>
		</table>
	</div>
	<div style="height: 280px;">
	<br><br><div style="font-size: 20px;">Related Searches</div><br>
	<table width="300" border="0" cellspacing="2" cellpadding="0">
	<%
	if("VS".equals(request.getParameter("option"))){
		ArrayList<String> elabQuery = (ArrayList<String>)request.getAttribute("elaboratedQuery");
		for(int j=0;j<elabQuery.size();j++){%>
		<tr><td style="color:brown; font-size: 18px;">
		
		<a href = "#" onclick="myFunction('<%=elabQuery.get(j)%>')" ><%= elabQuery.get(j)%></a>
		</td></tr><%
		}
	}%>
	</table>
	</div>
	</div>
		
	
	
	</div>
	
</body>
</html>