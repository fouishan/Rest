--<ScriptOptions statementTerminator=";"/>

CREATE TABLE radiologydates (
		radiologydate DATE NOT NULL,
		radiologytreatment_fk INT8
	);

ALTER TABLE radiologydates ADD CONSTRAINT fk_radiologydates_radiologytreatment_fk FOREIGN KEY (radiologytreatment_fk)
	REFERENCES radiologytreatment (id);

