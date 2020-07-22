CREATE TABLE availability (
    id INTEGER not NULL,
    name VARCHAR(255) not NULL,
    comment VARCHAR(255),
    PRIMARY KEY (id));
    
-- Method to collect data
CREATE TABLE collection_tool (
    id INTEGER NOT NULL,
    name VARCHAR(50),
    PRIMARY KEY(id));
    
CREATE TABLE country (
    id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    iso CHAR(2) NOT NULL, -- ISO-3166-1 alpha-2
    PRIMARY KEY(id));
    
CREATE TABLE fish_area (
    id INTEGER NOT NULL,
    name VARCHAR(250) NOT NULL,
    ssd CHAR(10) NOT NULL);

CREATE TABLE format(
    id INTEGER not NULL,
    name VARCHAR(255) not NULL,
    comment VARCHAR(255),
    PRIMARY KEY (id));

CREATE TABLE hazard_type (
    id INTEGER not NULL,
    name VARCHAR(255) not NULL,
    PRIMARY KEY(id));

CREATE TABLE hazard (
    id INTEGER NOT NULL,
    name VARCHAR(300),
    ssd CHAR(15),
    PRIMARY KEY (id));

CREATE TABLE ind_sum (
    id INTEGER NOT NULL,
    name VARCHAR(50),
    ssd CHAR(5), -- TODO: Could not find it in EFSA's SDD
    PRIMARY KEY(id));

CREATE TABLE laboratory_accreditation (
    id INTEGER NOT NULL,
    name VARCHAR(50),
    ssd VARCHAR(5), -- TODO: Is it really from SSD? I cannot find the actual SSD vocabulary.
    PRIMARY KEY(id));

CREATE TABLE language_written_in (
    id INTEGER not NULL,
    name VARCHAR(50) not NULL,
    PRIMARY KEY(id));
    
CREATE TABLE language (
    id INTEGER NOT NULL,
    code CHAR(2) NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(code));

CREATE TABLE model_class (
    id INTEGER NOT NULL,
    name VARCHAR(64) NOT NULL,
    PRIMARY KEY(id));

CREATE TABLE model_subclass (
    id INTEGER NOT NULL,
    name VARCHAR(128) NOT NULL,
    comment VARCHAR(256),
    class_id INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(class_id) REFERENCES model_class(id));
    
CREATE TABLE basic_process (
	id INTEGER NOT NULL,
	name VARCHAR(128) NOT NULL,
	class_id INTEGER NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(class_id) REFERENCES model_class(id));
    
CREATE TABLE model_equation_class (
    id INTEGER NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY(id));
    
CREATE TABLE packaging (
    id INTEGER not NULL,
    name VARCHAR(255) not NULL,
    ssd CHAR(5) not NULL, -- EFSA's SSD prodPackaging (xs:string(5))
    comment VARCHAR(255),
    PRIMARY KEY(id));
    
CREATE TABLE parameter_distribution (
    id INTEGER NOT NULL,
    name VARCHAR(50),
    comment VARCHAR(255),
    PRIMARY KEY(id));

CREATE TABLE parameter_source (
    id INTEGER NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY(id));

CREATE TABLE parameter_subject (
    id INTEGER NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY(id));
    
CREATE TABLE population (
    id INTEGER NOT NULL,
    name VARCHAR(255),
    foodon CHAR(10),
    PRIMARY KEY(id));
    
-- Production method
CREATE TABLE prodmeth (
    id INTEGER not NULL,
    name VARCHAR(255) not NULL,
    ssd CHAR(5) not NULL, -- EFSA's SSD prodProdMeth (xs:string(5))
    comment VARCHAR(400),
    PRIMARY KEY(id));
    
CREATE TABLE prodTreat (
    id INTEGER not NULL,
    name VARCHAR(255) not NULL,
    ssd CHAR(5) not NULL, -- EFSA's SSD prodTreat
    comment VARCHAR(255),
    PRIMARY KEY(id));

CREATE TABLE product_matrix (
    id INTEGER not NULL,
    ssd CHAR(20) not NULL,
    name VARCHAR(250),
    comment VARCHAR(255),
    PRIMARY KEY(id));

CREATE TABLE publication_status(
    id INTEGER not NULL,
    name VARCHAR(50) not NULL,
    comment VARCHAR(255),
    PRIMARY KEY (id));

CREATE TABLE publication_type(
    id INTEGER not NULL,
    shortName VARCHAR(10) not NULL,
    fullName VARCHAR(255) not NULL,
    PRIMARY KEY (id));

CREATE TABLE region (
    id INTEGER not NULL,
    name VARCHAR(255),
    nuts CHAR(5) not NULL, -- NUTS vocabulary
    PRIMARY KEY(id));

CREATE TABLE rights(
    id INTEGER not NULL,
    shortname VARCHAR(50) not NULL,
    fullname VARCHAR(255) not NULL,
    url VARCHAR(255),
    PRIMARY KEY (id));

CREATE TABLE sampling_method (
    id INTEGER NOT NULL,
    name VARCHAR(255),
    sampmd CHAR(5) NOT NULL, -- EFSA's SSD sampMethod (xs:string(5))
    comment VARCHAR(255),
    PRIMARY KEY (id));
    
CREATE TABLE sampling_point (
    id INTEGER NOT NULL,
    name VARCHAR(255),
    sampnt CHAR(10),
    PRIMARY KEY(id));
    
-- Type of sampling program (Programme type in EFSA's SSD)
CREATE TABLE sampling_program (
    id INTEGER NOT NULL,
    name VARCHAR(255),
    progtype CHAR(5),
    PRIMARY KEY(id));
    
CREATE TABLE sampling_strategy (
    id INTEGER NOT NULL,
    name VARCHAR(50),
    comment VARCHAR(700),
    PRIMARY KEY(id));
    
CREATE TABLE software (
    id INTEGER not NULL,
    name VARCHAR(255) not NULL,
    PRIMARY KEY (id));
    
CREATE TABLE sources(
    id INTEGER not NULL,
    name VARCHAR(255) not NULL,
    comment VARCHAR(255),
    PRIMARY KEY (id));
    
CREATE TABLE status (
    id INTEGER NOT NULL,
    name VARCHAR(10) NOT NULL,
    comment VARCHAR(128),
    PRIMARY KEY(id));
    
CREATE TABLE unit_category (
    id INTEGER not NULL,
    name VARCHAR(255) not NULL,
    PRIMARY KEY(id));

CREATE TABLE unit (
    id INTEGER NOT NULL,
    name VARCHAR(64) NOT NULL,
    ssd CHAR(5),
    comment VARCHAR(128),
    category_id INTEGER,
    PRIMARY KEY(id),
    FOREIGN KEY(category_id) REFERENCES unit_category(id));
    
-- Study assay technology type
CREATE TABLE technology_type (
    id INTEGER NOT NULL,
    ssd VARCHAR(6) NOT NULL,
    name VARCHAR(128) NOT NULL,
    comment VARCHAR(256),
    PRIMARY KEY(id));

-- SSD MDSTAT (xs:string(5))
CREATE TABLE accreditation_procedure (
    id INTEGER NOT NULL,
    mdstat VARCHAR(5) NOT NULL, 
    name VARCHAR(50),
    PRIMARY KEY(id));

CREATE TABLE parameter_classification (
	id INTEGER NOT NULL,
	name VARCHAR(10) NOT NULL,
	comment VARCHAR(256),
	PRIMARY KEY(id));

CREATE TABLE parameter_datatype (
	id INTEGER NOT NULL,
	name VARCHAR(16) NOT NULL,
	comment VARCHAR(300),
	represented_as VARCHAR(8));
