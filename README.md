

After creating a REST interface of the domain driven design,
I’ve used the Postmaster plugin in chrome instead of curl. This chrome extension is offered by 
www.getpostman.com . This extension is much easier to use with better user interface. The commands used for testing the REST Web Service Client are as follow.  
Patient Operations: 
In the video, adding the patient, the added patient is retrieved using the patient identifier (byPatientId) 
1. Adding a patient to the clinic.  
POST ec2-35-162-92-61.us-west-2.compute.amazonaws.com:8080/clinic-rest/resources/patient 
 
Headers:  
Content-Type: application/xml 
 
Body: (raw, XML(application/xml)) 
<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
<patientRepresentation xmlns:ns2="http://cs548.stevens.edu/clinic/service/web/rest/data"> 
<ns2:patient-id>150</ns2:patient-id> 
<ns2:name>Ishan</ns2:name> 
<ns2:dob>1992-04-04</ns2:dob> 
<ns2:age>24</ns2:age> 
</patientRepresentation> 
 
2.	Obtaining a single patient representation, given a patient resource URI.  
GET ec2-35-162-92-61.us-west-2.compute.amazonaws.com:8080/clinic-rest/resources/patient/660 
 
3.	Obtaining a single patient representation, given a patient identifier. GET ec2-35-162-92-61.us-west-2.compute.amazonaws.com:8080/clinicrest/resources/patient/byPatientId?id=151 
 
4.	Obtaining a single treatment representation, from a treatment sub-resource of a patient, given a treatment resource URI.  
GET ec2-35-162-92-61.us-west-2.compute.amazonaws.com:8080/clinicrest/resources/patient/660/treatments/665 
 
Provider operations:  
In the video, adding the provider, the added provider is retrieved using the patient identifier (byNPI) 
1. Adding a provider to the clinic.  
POST ec2-35-162-92-61.us-west-2.compute.amazonaws.com:8080/clinic-rest/resources/provider 
 
Headers:  
Content-Type: application/xml 
 
Body: (raw, XML(application/xml)) 
<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
<providerRepresentation xmlns:ns2="http://cs548.stevens.edu/clinic/service/web/rest/data"> 
    <ns2:provider-id>196</ns2:provider-id> 
    <ns2:specialization>Physician</ns2:specialization> 
    <ns2:name>Dr. Strange</ns2:name> 
    <providerDtofactory/> 
</providerRepresentation> 
 
2.	Obtaining a single provider representation, given a provider NPI. GET ec2-35-162-92-61.us-west-2.compute.amazonaws.com:8080/clinicrest/resources/provider/byNPI?id=196 
 
3.	Obtaining a single provider representation, given a provider key. 
GET ec2-35-162-92-61.us-west-2.compute.amazonaws.com:8080/clinic-rest/resources/provider/663 
 
4.	Adding a treatment for a patient treated by this provider.  
POST ec2-35-162-92-61.us-west-2.compute.amazonaws.com:8080/clinicrest/resources/provider/513/treatments 
 
Headers:  
Content-Type: application/xml 
X-Patient: ec2-35-162-216-228.us-west-2.compute.amazonaws.com:8080/clinicrest/resources/patient/466 
 
Body: (raw, XML(application/xml)) 
<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
<treatmentRepresentation xmlns:ns2="http://cs548.stevens.edu/clinic/service/web/rest/data">     <ns2:diagnosis>Cold</ns2:diagnosis> 
    <ns2:drugTreatment> 
        <ns2:drugname>Advil</ns2:drugname> 
        <ns2:dosage>2.0</ns2:dosage> 
    </ns2:drugTreatment> 
    <repFactory/> 
    <treatmentDtoFactory/> 
</treatmentRepresentation> 
 
5. Getting a treatment administered by this provider: 
GET ec2-35-162-92-61.us-west-2.compute.amazonaws.com:8080/clinicrest/resources/provider/513/treatments/515 
