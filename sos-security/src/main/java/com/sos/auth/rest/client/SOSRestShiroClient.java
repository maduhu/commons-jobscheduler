package com.sos.auth.rest.client;


import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.MediaType;





import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.auth.rest.SOSWebserviceAuthenticationRecord;
import com.sos.auth.rest.permission.model.SOSPermissionShiro;
import com.sos.jobscheduler.tools.webservices.globals.SOSCommandSecurityWebserviceAnswer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

  
public class SOSRestShiroClient {
    
public SOSRestShiroClient() {
    super();
 }

private SOSPermissionShiro getSOSPermissionShiro(URL resource) {
    Client client = Client.create();
    WebResource webResource = client.resource(resource.toExternalForm());
    ClientResponse response = webResource.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);  
    if (response.getStatus() != 200) {
         throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }   
    SOSPermissionShiro shiro = response.getEntity(SOSPermissionShiro.class); 
    return shiro;
     
}

private SOSCommandSecurityWebserviceAnswer getSOSCommandSecurityPlugin(URL resource) {
    Client client = Client.create();
    WebResource webResource = client.resource(resource.toExternalForm());
    ClientResponse response = webResource.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);  
    if (response.getStatus() != 200) {
         throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }   
    SOSCommandSecurityWebserviceAnswer answer = response.getEntity(SOSCommandSecurityWebserviceAnswer.class); 
    return answer;
     
}

public SOSShiroCurrentUserAnswer getSOSShiroCurrentUserAnswer(URL resource) {
    Client client = Client.create();
    WebResource webResource = client.resource(resource.toExternalForm());
    ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);  
    if (response.getStatus() != 200) {
         throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }   
    SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = response.getEntity(SOSShiroCurrentUserAnswer.class); 
    return sosShiroCurrentUserAnswer;
     
}

public SOSPermissionShiro getPermissions(SOSWebserviceAuthenticationRecord sosWebserviceAuthenticationRecord) throws MalformedURLException {
    return getSOSPermissionShiro(new URL(String.format(sosWebserviceAuthenticationRecord.getResource(),sosWebserviceAuthenticationRecord.getUser(),sosWebserviceAuthenticationRecord.getPassword(),sosWebserviceAuthenticationRecord.getPermission(),sosWebserviceAuthenticationRecord.getSessionId())));
}

public boolean isEnabled(URL url) throws Exception {
    try{
        SOSCommandSecurityWebserviceAnswer m = getSOSCommandSecurityPlugin(url);
        return m.getIsEnabled();
    }catch (Exception e) {
       throw new Exception(String.format("Could not connect to security server at %s --> %s",url,e.getMessage()));
    }
}

public SOSShiroCurrentUserAnswer getSOSShiroCurrentUserAnswer(SOSWebserviceAuthenticationRecord sosWebserviceAuthenticationRecord) {
    String url = String.format(sosWebserviceAuthenticationRecord.getResource(),sosWebserviceAuthenticationRecord.getUser(),sosWebserviceAuthenticationRecord.getPassword(),sosWebserviceAuthenticationRecord.getPermission(),sosWebserviceAuthenticationRecord.getSessionId());
    try {
        return getSOSShiroCurrentUserAnswer(new URL(url));
    }catch (MalformedURLException e) {
        SOSShiroCurrentUserAnswer a = new SOSShiroCurrentUserAnswer();
        a.setSessionId(String.format("could not establish a session: MalformedUrlException %s",url));
        return a;
    }
}

public static void main(String[] args) throws MalformedURLException {
    
    SOSRestShiroClient sosRestShiroClient = new SOSRestShiroClient();
    SOSWebserviceAuthenticationRecord sosWebserviceAuthenticationRecord = new SOSWebserviceAuthenticationRecord();
    sosWebserviceAuthenticationRecord.setUser("SOS01");
    sosWebserviceAuthenticationRecord.setPassword("sos01");
    sosWebserviceAuthenticationRecord.setResource("http://localhost:40040/jobscheduler/rest/sosPermission/permissions?user=%s&pwd=%s");
 
    SOSPermissionShiro shiro = sosRestShiroClient.getPermissions(sosWebserviceAuthenticationRecord);
    System.out.println("Output xml client .... \n");
    System.out.println("Roles "+shiro.getSOSPermissionRoles().getSOSPermissionRole());
    System.out.println("Joe "+shiro.getSOSPermissions().getSOSPermissionJid().getSOSPermissionJoe().getSOSPermission());
    System.out.println("Joc "+shiro.getSOSPermissions().getSOSPermissionJid().getSOSPermissionJoc().getSOSPermission());
    System.out.println("Jid "+shiro.getSOSPermissions().getSOSPermissionJid().getSOSPermission());
    System.out.println("Jobnet"+shiro.getSOSPermissions().getSOSPermissionJid().getSOSPermissionJobnet().getSOSPermission());
    System.out.println("Events "+shiro.getSOSPermissions().getSOSPermissionJid().getSOSPermissionEvents().getSOSPermission());
    System.out.println("Dashboard "+shiro.getSOSPermissions().getSOSPermissionJid().getSOSPermissionDashboard().getSOSPermission());
    System.out.println("Workingplan "+shiro.getSOSPermissions().getSOSPermissionJid().getSOSPermissionWorkingplan().getSOSPermission());
    
    SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = sosRestShiroClient.getSOSShiroCurrentUserAnswer(sosWebserviceAuthenticationRecord);  
    System.out.println("SOS01:" + sosShiroCurrentUserAnswer.getIsAuthenticated());

    sosShiroCurrentUserAnswer = sosRestShiroClient.getSOSShiroCurrentUserAnswer(sosWebserviceAuthenticationRecord);  
    System.out.println("SOS01:" + sosShiroCurrentUserAnswer.getIsAuthenticated());
    System.out.println("SOS01:" + sosShiroCurrentUserAnswer.getHasRole());
 
    sosShiroCurrentUserAnswer = sosRestShiroClient.getSOSShiroCurrentUserAnswer(sosWebserviceAuthenticationRecord);  
    System.out.println("SOS01:" + sosShiroCurrentUserAnswer.getIsAuthenticated());
    System.out.println("SOS01:" + sosShiroCurrentUserAnswer.getIsPermitted());

    
    
 
    
    }   
}