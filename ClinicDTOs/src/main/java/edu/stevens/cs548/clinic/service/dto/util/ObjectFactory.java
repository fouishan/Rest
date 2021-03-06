//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.10.16 at 03:45:40 AM EDT 
//


package edu.stevens.cs548.clinic.service.dto.util;

import javax.xml.bind.annotation.XmlRegistry;

import edu.stevens.cs548.clinic.service.dto.util.PatientDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDto;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.stevens.cs548.clinic.service.dto.util package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.stevens.cs548.clinic.service.dto.util
     * 
     */
    public ObjectFactory() {
    }
    
    /**
     * Create an instance of {@link PatientDto }
     * 
     */
    public PatientDto createPatientDto() {
        return new PatientDto();
    }
    
    /**
     * Create an instance of {@link ProviderDto }
     * 
     */
    public ProviderDto createProviderDto() {
        return new ProviderDto();
    }

    /**
     * Create an instance of {@link ProviderDto }
     * 
     */
    /*public ProviderDto createProviderDto() {
        return new ProviderDto();
    }*/

    /**
     * Create an instance of {@link TreatmentDto }
     * 
     */
    public TreatmentDto createTreatmentDto() {
        return new TreatmentDto();
    }

    /**
     * Create an instance of {@link SurgeryType }
     * 
     */
    public SurgeryType createSurgeryType() {
        return new SurgeryType();
    }

    /**
     * Create an instance of {@link RadiologyType }
     * 
     */
    public RadiologyType createRadiologyType() {
        return new RadiologyType();
    }

    /**
     * Create an instance of {@link DrugTreatmentType }
     * 
     */
    public DrugTreatmentType createDrugTreatmentType() {
        return new DrugTreatmentType();
    }

}
