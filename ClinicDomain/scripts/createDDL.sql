CREATE TABLE PATIENT (ID BIGINT NOT NULL, BIRTHDATE DATE, NAME VARCHAR(255), PATIENTID BIGINT, PRIMARY KEY (ID))
CREATE TABLE TREATMENT (ID BIGINT NOT NULL, TType VARCHAR(31), DIAGNOSIS VARCHAR(255), patient_fk BIGINT, provider_fk BIGINT, PRIMARY KEY (ID))
CREATE TABLE DRUGTREATMENT (ID BIGINT NOT NULL, DOSAGE FLOAT, DRUG VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE PROVIDER (ID BIGINT NOT NULL, NPI BIGINT, NAME VARCHAR(255), SPECIALIZATION VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE SURGERY (ID BIGINT NOT NULL, SURGERYDATE DATE, PRIMARY KEY (ID))
CREATE TABLE RADIOLOGYTREATMENT (ID BIGINT NOT NULL, PRIMARY KEY (ID))
CREATE TABLE RADIOLOGYDATES (radiologyDate DATE NOT NULL, radiologyTreatment_fk BIGINT)
ALTER TABLE TREATMENT ADD CONSTRAINT FK_TREATMENT_provider_fk FOREIGN KEY (provider_fk) REFERENCES PROVIDER (ID)
ALTER TABLE TREATMENT ADD CONSTRAINT FK_TREATMENT_patient_fk FOREIGN KEY (patient_fk) REFERENCES PATIENT (ID)
ALTER TABLE DRUGTREATMENT ADD CONSTRAINT FK_DRUGTREATMENT_ID FOREIGN KEY (ID) REFERENCES TREATMENT (ID)
ALTER TABLE SURGERY ADD CONSTRAINT FK_SURGERY_ID FOREIGN KEY (ID) REFERENCES TREATMENT (ID)
ALTER TABLE RADIOLOGYTREATMENT ADD CONSTRAINT FK_RADIOLOGYTREATMENT_ID FOREIGN KEY (ID) REFERENCES TREATMENT (ID)
ALTER TABLE RADIOLOGYDATES ADD CONSTRAINT FK_RADIOLOGYDATES_radiologyTreatment_fk FOREIGN KEY (radiologyTreatment_fk) REFERENCES RADIOLOGYTREATMENT (ID)
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT DECIMAL(38), PRIMARY KEY (SEQ_NAME))
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0)