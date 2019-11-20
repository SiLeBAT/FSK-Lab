const ui = {
    "generalInformation": [{
        "id": "name",
        "label": "Name",
        "type": "text",
        "description": "A name given to the model or data",
        "required": true
    }, {
        "id": "source",
        "label": "Source",
        "type": "text",
        "description": "A source from which the model/data is derived",
        "vocabulary": "Source"
    }, {
        "id": "identifier",
        "label": "Identifier",
        "type": "text",
        "description": "An unambiguous ID given to the model or data. This can also be created automatically by a software tool",
        "required": true
    }, {
        "id": "creationDate",
        "label": "Creation date",
        "type": "date",
        "description": "Creation date/time of the FSK file",
        "required": true
    }, {
        "id": "modificationDate",
        "label": "Modification date",
        "type": "date-array",
        "description": "Date/time of the last version of the FSK file"
    }, {
        "id": "rights",
        "label": "Rights",
        "type": "text",
        "description": "Rights granted for usage, distribution and modification of this FSK file",
        "vocabulary": "Rights",
        "required": true
    }, {
        "id": "availability",
        "label": "Availability",
        "type": "text",
        "description": "Availability of data or model, i.e. if the annotated model code / data is included in this FSK file",
        "vocabulary": "Availability"
    }, {
        "id": "url",
        "label": "URL",
        "type": "url",
        "description": "Web address referencing the resource location (data for example)"
    }, {
        "id": "format",
        "label": "Format",
        "type": "text",
        "description": "File extension of the model or data file (including version number of format if applicable)",
        "vocabulary": "Format"
    }, {
        "id": "language",
        "label": "Language",
        "type": "text",
        "description": "A language of the resource (some data or reports can be available in French language for example)",
        "vocabulary": "Language"
    }, {
        "id": "software",
        "label": "Software",
        "type": "text",
        "description": "The program or software language in which the model has been implemented",
        "vocabulary": "Software"
    }, {
        "id": "languageWrittenIn",
        "label": "Language written in",
        "type": "text",
        "description": "Software language used to write the model, e.g. R or MatLab",
        "vocabulary": "Language_written_in"
    }, {
        "id": "status",
        "label": "Status",
        "type": "text",
        "description": "The curation status of the model",
        "vocabulary": "Status"
    }, {
        "id": "objective",
        "label": "Objective",
        "type": "text",
        "description": "Objective of the model or data"
    }, {
        "id": "description",
        "label": "Description",
        "type": "text",
        "description": "General description of the study, data or model"
    }],
    "modelCategory": [{
        "id": "modelClass",
        "label": "Model class",
        "type": "enum",
        "description": "Type of model according to RAKIP classification",
        "vocabulary": "Model_Class",
        "required": true
    }, {
        "id": "modelClassComment",
        "label": "Comment",
        "type": "text",
        "description": ""
    }, {
        "id": "modelSubClass",
        "label": "Subclass",
        "type": "text-array",
        "description": "Sub-cassification of the model given the Model Class",
        "vocabulary": "Model_Sub_Class"
    }, {
        "id": "basicProcess",
        "label": "Basic process",
        "type": "text-array",
        "description": "Defines the impact of the process on the hazard (so far only valid for process models and predictive models)",
        "vocabulary": "Basic_process"
    }],
    "contact": [{
        "id": "title",
        "label": "Title",
        "type": "text",
        "description": "Specifies the job title, functional position or function of the individual associated."
    }, {
        "id": "familyName",
        "label": "Family name",
        "type": "text",
        "description": "Family name or surname."
    }, {
        "id": "givenName",
        "label": "Given name",
        "type": "text",
        "description": "Given name"
    }, {
        "id": "email",
        "label": "Email",
        "type": "text",
        "description": "The address for eletronic mail communication.",
        "required": true
    }, {
        "id": "telephone",
        "label": "Telephone",
        "type": "text",
        "description": "The canonical number string for a telephone number for telephony communication."
    }, {
        "id": "streetAddress",
        "label": "Address",
        "type": "text",
        "description": "Physical delivery address"
    }, {
        "id": "country",
        "label": "Country",
        "type": "text",
        "description": "Full name of the country in English",
        "vocabulary": "Country"
    }, {
        "id": "zipCode",
        "label": "Zip code",
        "type": "text",
        "description": "Postal code"
    }, {
        "id": "region",
        "label": "Region",
        "type": "text",
        "description": "State or province",
        "vocabulary": "Region"
    }, {
        "id": "timeZone",
        "label": "Timezone",
        "type": "text",
        "description": "Time zone"
    }, {
        "id": "gender",
        "label": "Gender",
        "type": "text",
        "description": "Sex and gender identity. M (male), F (female), O (other), N (none or not applicable) and U (unknown)"
    }, {
        "id": "note",
        "label": "Note",
        "type": "text",
        "description": "Supplemental information or a comment"
    }, {
        "id": "organization",
        "label": "Organization",
        "type": "text",
        "description": "Organization information"
    }],
    "reference": [
        {
            "id": "isReferenceDescription",
            "label": "Is reference",
            "type": "boolean",
            "description": "Indicates whether this specific publication serves as the reference description for the model. There has to be at least one reference where this field is set to 'True'",
            "required": true
        }, {
            "id": "publicationType",
            "label": "Type",
            "type": "text",
            "description": "The type of publication, e.g. Report, Journal article, Book, Online database, ...",
            "vocabulary": "Publication_Type"
        }, {
            "id": "date",
            "label": "Date",
            "type": "date",
            "description": "Temporal information on the publication date"
        }, {
            "id": "pmid",
            "label": "PubMed",
            "type": "text",
            "description": "The PubMed ID related to this publication"
        }, {
            "id": "doi",
            "label": "DOI",
            "type": "text",
            "description": "DOI related to this publication",
            "required": true
        }, {
            "id": "authorList",
            "label": "Author list",
            "type": "text",
            "description": "Name and surname of the authors who contributed to this publication"
        }, {
            "id": "title",
            "label": "Title",
            "type": "text",
            "description": "Title of the publication in which the model or the data has been described",
            "required": true
        }, {
            "id": "abstract",
            "label": "Abstract",
            "type": "text",
            "description": "Abstract of the publication in which the model or the data has been described"
        }, {
            "id": "journal",
            "label": "Journal",
            "type": "text",
            "description": "Data on the details of the journal in which the model or the data has been described"
        }, {
            "id": "volume",
            "label": "Volume",
            "type": "text",
            "description": "Data on the details of the volume in which the model or the data has been described"
        }, {
            "id": "issue",
            "label": "Issue",
            "type": "text",
            "description": "Data on the details of the issue in which the model or the data has been described"
        }, {
            "id": "status",
            "label": "Status",
            "type": "text",
            "description": "The status of this publication, e.g. Published, Submitted, etc.",
            "vocabulary": "Publication_Status"
        }, {
            "id": "website",
            "label": "Website",
            "type": "text",
            "description": "A link to the publication website (different from DOI)"
        }, {
            "id": "comment",
            "label": "Comment",
            "type": "text",
            "description": "Further comments related to the reference description, e.g. which section in there describes the specific model or which figure in there can be reproduced with the visualization script"
        }
    ],
    "scope": [{
        "id": "generalComment",
        "label": "General comment",
        "type": "text",
        "description": "General comments on the scope of the study, data or model",
    }, {
        "id": "spatialInformation",
        "label": "Spatial information",
        "type": "text-array",
        "description": "Spatial information (area) on which the model or data applies"
    }],
    "product": [
        {
            "id": "name",
            "label": "Name",
            "type": "text",
            "description": "The product, matrix or environment (e.g food product, lab media, soil etc.) for which the model or data applies",
            "vocabulary": "Product_matrix_name",
            "required": true
        }, {
            "id": "description",
            "label": "Description",
            "type": "text",
            "description": "Detailed description of the product, matrix or environment for which the model or data applies"
        }, {
            "id": "unit",
            "label": "Unit",
            "type": "text",
            "description": "Unit of the product, matrix or environment for which the model or data applies",
            "vocabulary": "Parameter_unit",
            "required": true
        }, {
            "id": "method",
            "label": "Method",
            "type": "text-array",
            "description": "Type of production for the product/ matrix",
            "vocabulary": "Method_of_production"
        }, {
            "id": "packaging",
            "label": "Packaging",
            "type": "text-array",
            "description": "Describe container or wrapper that holds the product/matrix. Common type of packaging (paper or plastic bags, boxes, tinplate or aluminium cans, plastic trays, plastic bottles, glass bottles or jars)",
            "vocabulary": "Packaging"
        }, {
            "id": "treatment",
            "label": "Treatment",
            "type": "text-array",
            "description": "Used to characterise a product/matrix based on the treatment or processes applied to the product or any indexed ingredient",
            "vocabulary": "Product_treatment"
        }, {
            "id": "originCountry",
            "label": "Origin country",
            "type": "text",
            "description": "Country of origin of the food/product (ISO 3166-1-alpha-2 country code)",
            "vocabulary": "Country"
        }, {
            "id": "originArea",
            "label": "Origin area",
            "type": "text",
            "description": "Area of origin of the food/product (Nomenclature of territorial units for statistics – NUTS – coding system valid only for EEA and Switzerland)",
            "vocabulary": "Area_of_origin"
        }, {
            "id": "fisheriesArea",
            "label": "Fisheries area",
            "type": "text",
            "description": "Fisheries or aquaculture area specifying the origin of the sample (FAO Fisheries areas)",
            "vocabulary": "Fisheries_area"
        }, {
            "id": "productionDate",
            "label": "Production date",
            "type": "date",
            "description": "Date of production of food/product"
        }, {
            "id": "expiryDate",
            "label": "Expiration date",
            "type": "date",
            "description": "Date of expiry of food/product"
        }
    ],
    "hazard": [
        {
            "id": "type",
            "label": "Type",
            "type": "text",
            "description": "General classification of the hazard for which the model or data applies",
            "vocabulary": "Hazard_type"
        }, {
            "id": "name",
            "label": "Name",
            "type": "text",
            "description": "Name of the hazard (agent, contaminant, chemical) for which the data or model applies",
            "vocabulary": "Hazard_name",
            "required": true
        }, {
            "id": "description",
            "label": "Description",
            "type": "text",
            "description": "Description of the hazard for which the model or data applies"
        }, {
            "id": "unit",
            "label": "Unit",
            "type": "text",
            "description": "Unit of the hazard for which the model or data applies",
            "vocabulary": "Parameter_unit"
        }, {
            "id": "adverseEffect",
            "label": "Adverse effect",
            "type": "text",
            "description": "Adverse effect induced by hazard about morbidity, mortality, and etcetera"
        }, {
            "id": "sourceOfContamination",
            "label": "Source of contamination",
            "type": "text",
            "description": "Origin of the contamination, source"
        }, {
            "id": "benchmarkDose",
            "label": "Benchmark dose",
            "type": "text",
            "description": "A dose or concentration that produces a predetermined change in response rate of an adverse effect (called the benchmark response or BMR) compared to background"
        }, {
            "id": "maximumResidueLimit",
            "label": "Maximum residue limit",
            "type": "text",
            "description": "International regulations and permissible maximum residue levels in food and drinking water"
        }, {
            "id": "noObservedAdverseAffectLevel",
            "label": "NOAEL",
            "type": "text",
            "description": "Level of exposure of an organism, found by experiment or observation, at which there is no biologically or statistically significant increase in the frequency or severity of any adverse effects in the exposed population when compared to its appropriate control"
        }, {
            "id": "lowestObservedAdverseAffectLevel",
            "label": "LOAEL",
            "type": "text",
            "description": "Lowest concentration or amount of a substance found by experiment or observation that causes an adverse alteration of morphology, function, capacity, growth, development, or lifespan of a target organism distinguished from normal organisms of the same species under defined conditions of exposure"
        }, {
            "id": "acceptableOperatorsExposureLevel",
            "label": "AOEL",
            "type": "text",
            "description": "Maximum amount of active substance to which the operator may be exposed without any adverse health effects. The AOEL is expressed as milligrams of the chemical per kilogram body weight of the operator"
        }, {
            "id": "acuteReferenceDose",
            "label": "ARD",
            "type": "text",
            "description": "An estimate (with uncertainty spanning perhaps an order of magnitude) of a daily oral exposure for an acute duration (24 hours or less) to the human population (including sensitive subgroups) that is likely to be without an appreciable risk of deleterious effects during a lifetime"
        }, {
            "id": "acceptableDailyIntake",
            "label": "ADI",
            "type": "text",
            "description": "Acceptable daily intake: measure of amount of a specific substance in food or in drinking water tahta can be ingested (orally) on a daily basis over a lifetime without an appreciable health risk"
        }, {
            "id": "indSum",
            "label": "Ind sum",
            "type": "text",
            "description": "Define if the parameter reported is an individual residue/analyte, a summed residue definition or part of a sum a summed residue definition",
            "vocabulary": "Hazard_ind_sum"
        }],
    "populationGroup": [{
        "id": "name",
        "label": "Name",
        "type": "text",
        "description": "Name of the population for which the model or data applies",
        "required": true
    }, {
        "id": "targetPopulation",
        "label": "Target population",
        "type": "text",
        "description": "Population of individual that we are interested in describing and making statistical inferences about"
    }, {
        "id": "populationSpan",
        "label": "Span",
        "type": "text-array",
        "description": "Temporal information on the exposure duration"
    }, {
        "id": "populationDescription",
        "label": "Description",
        "type": "text-array",
        "description": "Description of the population for which the model applies (demographic and socio-economic characteristics for example). Background information that are needed in the data analysis phase, size of household, education level, employment status, professional category, ethnicity, etc."
    }, {
        "id": "populationAge",
        "label": "Age",
        "type": "text-array",
        "description": "Description of the range of age or group of age"
    }, {
        "id": "populationGender",
        "label": "Gender",
        "type": "text",
        "description": "Description of the percentage of gender"
    }, {
        "id": "specialDietGroups",
        "label": "Special diet groups",
        "type": "text-array",
        "description": "Description of sub-population with special diets (vegetarians, diabetics, group following special ethnic diets)"
    }, {
        "id": "patternConsumption",
        "label": "Pattern consumption",
        "type": "text-array",
        "description": "Description of the consumption of different food items, frequency, portion size"
    }, {
        "id": "region",
        "label": "Region",
        "type": "text-array",
        "description": "Spatial information (area) on which the population group of the model or data applies",
        "vocabulary": "Region"
    }, {
        "id": "country",
        "label": "Country",
        "type": "text-array",
        "description": "Country on which the population group of the model or data applies",
        "vocabulary": "Country"
    }, {
        "id": "populationRiskFactor",
        "label": "Risk factor",
        "type": "text-array",
        "description": "Population risk factor that may influence the outcomes of the study, confounder should be included"
    }, {
        "id": "season",
        "label": "Season",
        "type": "text-array",
        "description": "Distribution of surveyed people according to the season (influence consumption pattern)"
    }],
    "study": [{
        "id": "identifier",
        "label": "Identifier",
        "type": "text",
        "description": "A user-defined identifier for the study",
        "required": true
    }, {
        "id": "title",
        "label": "Title",
        "type": "text",
        "description": "A title for the Study"
    }, {
        "id": "description",
        "label": "Description",
        "type": "text",
        "description": "A brief description of the study aims"
    }, {
        "id": "designType",
        "label": "Design type",
        "type": "text",
        "description": "The type of study design being employed"
    }, {
        "id": "assayMeasurementType",
        "label": "Measurement type",
        "type": "text",
        "description": "The measurement being observed in this assay"
    }, {
        "id": "assayTechnologyType",
        "label": "Technology type",
        "type": "text",
        "description": "The technology being employed to observe this measurement",
        "vocabulary": "Study_Assay_Technology_Type"
    }, {
        "id": "assayTechnologyPlatform",
        "label": "Technology platform",
        "type": "text",
        "description": "The technology platform used"
    }, {
        "id": "accreditationProcedureForTheAssayTechnology",
        "label": "Accreditation procedure",
        "type": "text",
        "description": "Accreditation procedure for the analytical method used",
        "vocabulary": "Accreditation_procedure_Ass_Tec"
    }, {
        "id": "protocolName",
        "label": "Protocol name",
        "type": "text",
        "description": "The name of the protocol, e.g.Extraction Protocol"
    }, {
        "id": "protocolDescription",
        "label": "Protocol description",
        "type": "text",
        "description": "A description of the Protocol"
    }, {
        "id": "protocolURI",
        "label": "Protocol URI",
        "type": "text",
        "description": "A URI to link out to a publication, web page, etc. describing the protocol."
    }, {
        "id": "protocolVersion",
        "label": "Protocol version",
        "type": "text",
        "description": "The version of the protocol used, where applicable"
    }, {
        "id": "protocolParametersName",
        "label": "Parameters name",
        "type": "text",
        "description": "The parameters used when executing this protocol"
    }, {
        "id": "protocolComponentsName",
        "label": "Components name",
        "type": "text",
        "description": "The components used when carrying out this protocol"
    }, {
        "id": "protocolComponentsType",
        "label": "Components type",
        "type": "text",
        "description": ""
    }],
    "studySample": [{
        "id": "sampleName",
        "label": "Name",
        "type": "text",
        "description": "An unambiguous ID given to the samples used in the assay",
        "required": true
    }, {
        "id": "protocolOfSampleCollection",
        "label": "Protocol",
        "type": "text",
        "description": "Additional protocol for sample and sample collection. Corresponds to the Protocol REF in ISA",
        "required": true
    }, {
        "id": "samplingStrategy",
        "label": "Sampling strategy",
        "type": "text",
        "description": "Sampling strategy (ref. EUROSTAT - Typology of sampling strategy, version of July 2009)",
        "vocabulary": "Sampling_strategy"
    }, {
        "id": "typeOfSamplingProgram",
        "label": "Sampling program",
        "type": "text",
        "description": "Indicate the type of programm for which the samples have been collected",
        "vocabulary": "Type_of_sampling_program"
    }, {
        "id": "samplingMethod",
        "label": "Sampling method",
        "type": "text",
        "description": "Sampling method used to take the sample",
        "vocabulary": "Sampling_method"
    }, {
        "id": "samplingPlan",
        "label": "Sampling plan",
        "type": "text",
        "description": "Description of data collection technique (stratified or complex sampling (several stages))",
        "required": true
    }, {
        "id": "samplingWeight",
        "label": "Sampling weight",
        "type": "text",
        "description": "Description of the method employed to compute sampling weight (nonresponse-adjusted weight)",
        "required": true
    }, {
        "id": "samplingSize",
        "label": "Sampling size",
        "type": "text",
        "description": "Number of units, full participants, partial participants, eligibles, not eligible, unresolved (eligibility status not resolved)...",
        "required": true
    }, {
        "id": "lotSizeUnit",
        "label": "Size unit",
        "type": "text",
        "description": "Unit in which the lot size is expressed"
    }, {
        "id": "samplingPoint",
        "label": "Sampling point",
        "type": "text",
        "description": "Point in the food chain where the sample was taken. (Doc. ESTAT/F5/ES/155 “Data dictionary of activities of the establishments”)",
        "vocabulary": "Sampling_point"
    }],
    "dietaryAssessmentMethod": [{
        "id": "collectionTool",
        "label": "Collection tool",
        "type": "text",
        "description": "Food diaries, interview, 24-hour recall interview, food propensy questionnaire, portion size measurement aids, eating outside questionnaire",
        "vocabulary": "Method_tool_to_collect_data",
        "required": true
    }, {
        "id": "numberOfNonConsecutiveOneDay",
        "label": "Non-consecutive one day",
        "type": "text",
        "description": "Number of non-consecutive one-day recorded",
        "required": true
    }, {
        "id": "softwareTool",
        "label": "Software tool",
        "type": "text",
        "description": "Name of the software used to collect the data"
    }, {
        "id": "numberOfFoodItems",
        "label": "Food items",
        "type": "text-array",
        "description": "Number of food items",
        "required": true
    }, {
        "id": "recordTypes",
        "label": "Record types",
        "type": "text-array",
        "description": "Consumption occasion, mean of consumption, quantified and described as eaten, recipes for self-made",
        "required": true
    }, {
        "id": "foodDescriptors",
        "label": "Food descriptors",
        "type": "text-array",
        "description": "Description using FoodEx2 facet",
        "required": true
    }],
    "laboratory": [{
        "id": "name",
        "label": "Name",
        "type": "text",
        "description": "Laboratory code (National laboratory code if available) or Laboratory name"
    }, {
        "id": "country",
        "label": "Country",
        "type": "text",
        "description": "Country where the laboratory is placed. (ISO 3166-1-alpha-2)",
        "vocabulary": "Country"
    }, {
        "id": "accreditation",
        "label": "Accreditation",
        "type": "text-array",
        "description": "The laboratory accreditation to ISO/IEC 17025",
        "vocabulary": "Laboratory_accreditation",
        "required": true
    }],
    "assay": [{
        "id": "name",
        "label": "Name",
        "type": "text",
        "description": "A name given to the assay",
        "required": true
    }, {
        "id": "description",
        "label": "Description",
        "type": "text",
        "description": "General description of the assay. Corresponds to the Protocol REF in ISA"
    }, {
        "id": "moisturePercentage",
        "label": "Moisture %",
        "type": "text",
        "description": "Percentage of moisture in the original sample"
    }, {
        "id": "fatPercentage",
        "label": "Fat %",
        "type": "text",
        "description": "Percentage of fat in the original sample"
    }, {
        "id": "detectionLimit",
        "label": "Detection limit",
        "type": "text",
        "description": "Limit of detection reported in the unit specified by the variable 'Hazard unit'"
    }, {
        "id": "quantificationLimit",
        "label": "Quantification limit",
        "type": "text",
        "description": "Limit of quantification reported in the unit specified by the variable 'Hazard unit'"
    }, {
        "id": "leftCensoredData",
        "label": "Censored data",
        "type": "text",
        "description": "Percentage of measures equal to LOQ and/or LOD"
    }, {
        "id": "contaminationRange",
        "label": "Contamination range",
        "type": "text",
        "description": "Contamination range"
    }, {
        "id": "uncertaintyValue",
        "label": "Uncertainty value",
        "type": "text",
        "description": "Indicate the expanded uncertainty (usually 95% confidence interval) value associated with the measurement expressed in the unit reported in the field 'Hazard unit'"
    }],
    "modelMath": [{
        "id": "fittingProcedure",
        "label": "Fitting procedure",
        "type": "text",
        "description": "Procedure used to fit the data to the model equation"
    }, {
        "id": "event",
        "label": "Event",
        "type": "text-array",
        "description": "Definition of time-dependent parameter changes"
    }],
    "parameter": [{
        "id": "id",
        "label": "ID",
        "type": "text",
        "description": "An unambiguous ID given to each of the parameters - preferably autogenerated by a software tool and compatible with SBML ID requirements, only letters from A to Z, numbers and '_'",
        "required": true
    }, {
        "id": "classification",
        "label": "Classification",
        "type": "text",
        "description": "General classification of the parameter (e.g. Input, Constant, Output...)",
        "vocabulary": "Parameter_classification",
        "required": true
    }, {
        "id": "name",
        "label": "Name",
        "type": "text",
        "description": "A name given to the parameter",
        "required": true
    }, {
        "id": "description",
        "label": "Description",
        "type": "text",
        "description": "General description of the parameter"
    }, {
        "id": "unit",
        "label": "Unit",
        "type": "text",
        "description": "Unit of the parameter",
        "vocabulary": "Parameter_unit",
        "required": true
    }, {
        "id": "unitCategory",
        "label": "Unit category",
        "type": "text",
        "description": "",
        "vocabulary": "Parameter_unit_category"
    }, {
        "id": "dataType",
        "label": "Data type",
        "type": "text",
        "description": "Information on the data format of the parameter, e.g. if it the input parameter is a file location or a date or a number. This is important for software tools interpreting the metadata and generate user interfaces for parameter input.",
        "vocabulary": "Parameter_data_type",
        "required": true
    }, {
        "id": "source",
        "label": "Source",
        "type": "text",
        "description": "Information on the type of knowledge used to define the parameter value",
        "vocabulary": "Parameter_source"
    }, {
        "id": "subject",
        "label": "Subject",
        "type": "text",
        "description": "Scope of the parameter, e.g. if it refers to an animal, a batch of animals, a batch of products, a carcass, a carcass skin etc",
        "vocabulary": "Parameter_subject"
    }, {
        "id": "distribution",
        "label": "Distribution",
        "type": "text",
        "description": 'Distribution describing the parameter variabilty. If no distribution selected this means the value provided in "Parameter value" is a point estimate. In case a distribution is selected the value provided in "Parameter value" is a string that the model code can parse in order to sample from the named distribution',
        "vocabulary": "Parameter_distribution"
    }, {
        "id": "value",
        "label": "Value",
        "type": "text",
        "description": "A default value for the parameter. This is mandatory (needs to be provided) for all parameters of type 'Input'"
    },
    // TODO: reference
    {
        "id": "variabilitySubject",
        "label": "Variability subject",
        "type": "text",
        "description": "Information 'per what' the variability is described. It can be variability between broiler in a flock,  variability between all meat packages sold, variability between days, etc."
    }, {
        "id": "minValue",
        "label": "Min value",
        "type": "text",
        "description": "Numerical value of the minimum limit of the parameter that determines the range of applicability for which the model applies"
    }, {
        "id": "maxValue",
        "label": "Max value",
        "type": "text",
        "description": "Numerical value of the maximum limit of the parameter that determines the range of applicability for which the model applies"
    }, {
        "id": "error",
        "label": "Error",
        "type": "text",
        "description": "Error of the parameter value"
    }],
    "qualityMeasures": [{
        "id": "sse",
        "label": "SSE",
        "type": "number",
        "description": "Statistical values calculated to describe the performance of the model fitting procedure"
    }, {
        "id": "mse",
        "label": "MSE",
        "type": "number",
        "description": "Statistical values calculated to describe the performance of the model fitting procedure"
    }, {
        "id": "rmse",
        "label": "RMSE",
        "type": "number",
        "description": "Statistical values calculated to describe the performance of the model fitting procedure"
    }, {
        "id": "rsquared",
        "label": "R squared",
        "type": "number",
        "description": "Statistical values calculated to describe the performance of the model fitting procedure"
    }, {
        "id": "aic",
        "label": "AIC",
        "type": "number",
        "description": "Statistical values calculated to describe the performance of the model fitting procedure"
    }, {
        "id": "bic",
        "label": "BIC",
        "type": "number",
        "description": "Statistical values calculated to describe the performance of the model fitting procedure"
    }, {
        "id": "sensitivityAnalysis",
        "label": "Sensitivity analysis",
        "type": "text",
        "description": "Description of the results of an sensitivity analysis, i.e. how independence assumptions are met or how variables will affect the output of model"
    }],
    "modelEquation": [{
        "id": "name",
        "label": "Name",
        "type": "text",
        "description": "A name given to the model equation",
        "required": true,
    }, {
        "id": "modelEquationClass",
        "label": "Class",
        "type": "text",
        "description": "Information on that helps to categorize model equations"
    },
    // TODO: reference
    {
        "id": "modelEquation",
        "label": "Equation",
        "type": "text",
        "description": "The pointer to the file that holds the software code (e.g. R-script)",
        "required": true
    }, {
        "id": "modelHypothesis",
        "label": "Hypothesis",
        "type": "text-array",
        "description": "Description of the hypothesis of the model"
    }],
    "exposure": [{
        "id": "treatment",
        "label": "Treatment",
        "type": "text-array",
        "description": "Description of the mathematical method to replace left-censored data (recommandation of WHO (2013), distribution or others)"
    }, {
        "id": "contamination",
        "label": "Contamination",
        "type": "text-array",
        "description": "Description of the range of of the level of contamination after left censored data treatment"
    }, {
        "id": "type",
        "label": "Type",
        "type": "text",
        "description": "Description of the type of exposure",
        "required": true
    }, {
        "id": "scenario",
        "label": "Scenario",
        "type": "text-array",
        "description": "Description of the different scenarii used in exposure assessment"
    }, {
        "id": "uncertaintyEstimation",
        "label": "Uncertainty estimation",
        "type": "text",
        "description": "Analysis to estimate uncertainty"
    }]
}

const _genericModelScope = {
    scope: [
        {
            id: "generalComment",
            label: "General comment",
            type: "text",
            description: "General comments on the scope of the study, data or model"
        },
        {
            id: "temporalInformation",
            label: "Temporal information",
            type: "text",
            description: "Spatial information (area) on which the model or data applies"
        },
        {
            id: "spatialInformation",
            label: "Spatial information",
            type: "text-array",
            description: "" // TODO: spatial information description
        }
    ],
    product: ui.product,
    hazard: ui.hazard,
    populationGroup: ui.populationGroup
};

const _exposureModelScope = {
    // scope
    scope: [
        {
            id: "generalComment",
            label: "General comment",
            type: "text",
            description: "General comments on the scope of the study, data or model"
        },
        {
            id: "temporalInformation",
            label: "Temporal information",
            type: "text",
            description: "Spatial information (area) on which the model or data applies"
        },
        {
            id: "spatialInformation",
            label: "Spatial information",
            type: "text-array",
            description: "" // TODO: spatial information description
        }
    ],
    product: ui.product,
    hazard: ui.hazard,
    populationGroup: ui.populationGroup
};

const _genericModelDataBackground = {
    study: ui.study,
    studySample: ui.studySample,
    dietaryAssessmentMethod: ui.dietaryAssessmentMethod,
    laboratory: ui.laboratory,
    assay: ui.assay
};

const _predictiveModelDataBackground = {
    study: ui.study,
    studySample: ui.studySample,
    laboratory: ui.laboratory,
    assay: ui.assay
};

const _genericModelModelMath = {
    modelMath: [
        {
            id: "fittingProcedure",
            label: "Fitting procedure",
            type: "text",
            description: "Procedure used to fit the data to the model equation"
        }, {
            id: "event",
            label: "Event",
            type: "text-array",
            description: "Definition of time-dependent parameter changes"
        }
    ],
    parameter: ui.parameter,
    qualityMeasures: ui.qualityMeasures,
    modelEquation: ui.modelEquation,
    exposure: ui.exposure
};

const _predictiveModelModelMath = {
    modelMath: [
        {
            id: "fittingProcedure",
            label: "Fitting procedure",
            type: "text",
            description: "Procedure used to fit the data to the model equation"
        }, {
            id: "event",
            label: "Event",
            type: "text-array",
            description: "Definition of time-dependent parameter changes"
        }
    ],
    parameter: ui.parameter,
    qualityMeasures: ui.qualityMeasures,
    modelEquation: ui.modelEquation
};

const _predictiveModelGeneralInformation = {
    generalInformation: [
        {
            "id": "name",
            "label": "Model name",
            "type": "text",
            "description": "A name given to the model or data",
            "required": true
        },
        {
            "id": "source",
            "label": "Source",
            "type": "text",
            "description": "A source from which the model/data is derived",
            "vocabulary": "Source"
        },
        {
            "id": "identifier",
            "label": "Identifier",
            "type": "text",
            "description": "An unambiguous ID given to the model or data. This can also be created automatically by a software tool",
            "required": true
        },
        {
            "id": "creationDate",
            "label": "Creation date",
            "type": "date",
            "description": "Creation date/time of the FSK file",
            "required": true
        }, {
            "id": "modificationDate",
            "label": "Modification date",
            "type": "date-array",
            "description": "Date/time of the last version of the FSK file"
        },
        {
            "id": "rights",
            "label": "Rights",
            "type": "text",
            "description": "Rights granted for usage, distribution and modification of this FSK file",
            "vocabulary": "Rights",
            "required": true
        }, {
            "id": "availability",
            "label": "Availability",
            "type": "text",
            "description": "Availability of data or model, i.e. if the annotated model code / data is included in this FSK file",
            "vocabulary": "Availability"
        },
        {
            "id": "url",
            "label": "URL",
            "type": "url",
            "description": "Web address referencing the resource location (data for example)"
        }, {
            "id": "format",
            "label": "Format",
            "type": "text",
            "description": "File extension of the model or data file (including version number of format if applicable)",
            "vocabulary": "Format"
        }, {
            "id": "language",
            "label": "Language",
            "type": "text",
            "description": "A language of the resource (some data or reports can be available in French language for example)",
            "vocabulary": "Language"
        },
        {
            "id": "software",
            "label": "Software",
            "type": "text",
            "description": "The program or software language in which the model has been implemented",
            "vocabulary": "Software"
        }, {
            "id": "languageWrittenIn",
            "label": "Language written in",
            "type": "text",
            "description": "Software language used to write the model, e.g. R or MatLab",
            "vocabulary": "Language_written_in"
        },
        {
            "id": "status",
            "label": "Status",
            "type": "text",
            "description": "The curation status of the model",
            "vocabulary": "Status"
        },
        {
            "id": "objective",
            "label": "Objective",
            "type": "text",
            "description": "Objective of the model or data"
        },
        {
            "id": "description",
            "label": "Description",
            "type": "text",
            "description": "General description of the study, data or model"
        }
    ],
    modelCategory: ui.modelCategory,
    contact: ui.contact,
    reference: ui.reference
};

const schemas = {

    genericModel: {
        // general information
        generalInformation: [
            {
                "id": "name",
                "label": "Name",
                "type": "text",
                "description": "A name given to the model or data",
                "required": true
            }, {
                "id": "source",
                "label": "Source",
                "type": "text",
                "description": "A source from which the model/data is derived",
                "vocabulary": "Source"
            }, {
                "id": "identifier",
                "label": "Identifier",
                "type": "text",
                "description": "An unambiguous ID given to the model or data. This can also be created automatically by a software tool",
                "required": true
            }, {
                "id": "creationDate",
                "label": "Creation date",
                "type": "date",
                "description": "Creation date/time of the FSK file",
                "required": true
            }, {
                "id": "modificationDate",
                "label": "Modification date",
                "type": "date-array",
                "description": "Date/time of the last version of the FSK file"
            }, {
                "id": "rights",
                "label": "Rights",
                "type": "text",
                "description": "Rights granted for usage, distribution and modification of this FSK file",
                "vocabulary": "Rights",
                "required": true
            }, {
                "id": "availability",
                "label": "Availability",
                "type": "text",
                "description": "Availability of data or model, i.e. if the annotated model code / data is included in this FSK file",
                "vocabulary": "Availability"
            }, {
                "id": "url",
                "label": "URL",
                "type": "url",
                "description": "Web address referencing the resource location (data for example)"
            }, {
                "id": "format",
                "label": "Format",
                "type": "text",
                "description": "File extension of the model or data file (including version number of format if applicable)",
                "vocabulary": "Format"
            }, {
                "id": "language",
                "label": "Language",
                "type": "text",
                "description": "A language of the resource (some data or reports can be available in French language for example)",
                "vocabulary": "Language"
            }, {
                "id": "software",
                "label": "Software",
                "type": "text",
                "description": "The program or software language in which the model has been implemented",
                "vocabulary": "Software"
            }, {
                "id": "languageWrittenIn",
                "label": "Language written in",
                "type": "text",
                "description": "Software language used to write the model, e.g. R or MatLab",
                "vocabulary": "Language_written_in"
            }, {
                "id": "status",
                "label": "Status",
                "type": "text",
                "description": "The curation status of the model",
                "vocabulary": "Status"
            }, {
                "id": "objective",
                "label": "Objective",
                "type": "text",
                "description": "Objective of the model or data"
            }, {
                "id": "description",
                "label": "Description",
                "type": "text",
                "description": "General description of the study, data or model"
            }
        ],
        modelCategory: ui.modelCategory,
        contact: ui.contact,
        reference: ui.reference
    },

    dataModel: {
        generalInformation: [
            {
                "id": "name",
                "label": "Data name",
                "type": "text",
                "description": "A name given to the data",
                "required": true
            },
            {
                "id": "source",
                "label": "Source",
                "type": "text",
                "description": "A related resource from which the described resource is derived",
                "vocabulary": "Source"
            },
            {
                "id": "identifier",
                "label": "Identifier",
                "type": "text",
                "description": "An unambiguous ID given to the model or data. This can also be created automatically by a software tool",
                "required": true
            },
            {
                "id": "creationDate",
                "label": "Creation date",
                "type": "date",
                "description": "Temporal information on the model creation date",
                "required": true
            },
            {
                "id": "modificationDate",
                "label": "Modification date",
                "type": "date-array",
                "description": "Temporal information on the last modification of the model"
            },
            {
                "id": "rights",
                "label": "Rights",
                "type": "text",
                "description": "Information on rights held in an over the resource",
                "vocabulary": "Rights",
                "required": true
            },
            {
                "id": "availability",
                "label": "Availability",
                "type": "text",
                "description": "Availability of data",
                "vocabulary": "Availability"
            },
            {
                "id": "url",
                "label": "URL",
                "type": "url",
                "description": "Web address referencing the resource location"
            },
            {
                "id": "format",
                "label": "Format",
                "type": "text",
                "description": "Form of data (file extension)",
                "vocabulary": "Format"
            },
            {
                "id": "language",
                "label": "Language",
                "type": "text",
                "description": "A language of the resource (some data or reports can be available in French language for example)",
                "vocabulary": "Language"
            },
            {
                "id": "status",
                "label": "Status",
                "type": "text",
                "description": "The curation status of the model",
                "vocabulary": "Status"
            },
            {
                "id": "objective",
                "label": "Objective",
                "type": "text",
                "description": "Objective of the model or data"
            },
            {
                "id": "description",
                "label": "Description",
                "type": "text",
                "description": "General description of the study, data or model"
            }
        ],
        contact: ui.contact,
        reference: ui.reference,

        // model math
        parameter: ui.parameter,
    },

    predictiveModel: {

        // scope
        scope: [
            {
                id: "generalComment",
                label: "General comment",
                type: "text",
                description: "General comments on the scope of the study, data or model"
            },
            {
                id: "temporalInformation",
                label: "Temporal information",
                type: "text",
                description: "Spatial information (area) on which the model or data applies"
            },
            {
                id: "spatialInformation",
                label: "Spatial information",
                type: "text-array",
                description: "" // TODO: spatial information description
            }
        ],
        product: ui.product,
        hazard: ui.hazard
    },

    otherModel: {

        // general information
        generalInformation: [
            {
                "id": "name",
                "label": "Model name",
                "type": "text",
                "description": "A name given to the model",
                "required": true
            },
            {
                "id": "source",
                "label": "Source",
                "type": "text",
                "description": "A related resource from which the described resource is derived",
                "vocabulary": "Source"
            },
            {
                "id": "identifier",
                "label": "Identifier",
                "type": "text",
                "description": "An unambiguous ID given to the model or data. This can also be created automatically by a software tool",
                "required": true
            },
            {
                "id": "creationDate",
                "label": "Creation date",
                "type": "date",
                "description": "Temporal information on the model creation date",
                "required": true
            },
            {
                "id": "modificationDate",
                "label": "Modification date",
                "type": "date-array",
                "description": "Temporal information on the last modification of the model"
            },
            {
                "id": "rights",
                "label": "Rights",
                "type": "text",
                "description": "Information on rights held in an over the resource",
                "vocabulary": "Rights",
                "required": true
            },
            {
                "id": "availability",
                "label": "Availability",
                "type": "text",
                "description": "Availability of data",
                "vocabulary": "Availability"
            },
            {
                "id": "url",
                "label": "URL",
                "type": "url",
                "description": "Web address referencing the resource location"
            },
            {
                "id": "format",
                "label": "Format",
                "type": "text",
                "description": "Form of data (file extension)",
                "vocabulary": "Format"
            },
            {
                "id": "language",
                "label": "Language",
                "type": "text",
                "description": "A language of the resource (some data or reports can be available in French language for example)",
                "vocabulary": "Language"
            },
            {
                id: "software",
                label: "Software",
                type: "text",
                description: "The program in which the model has been implemented",
                vocabulary: "Software"
            },
            {
                "id": "languageWrittenIn",
                "label": "Language written in",
                "type": "text",
                "description": "Software language used to write the model, e.g. R or MatLab",
                "vocabulary": "Language_written_in"
            },
            {
                "id": "status",
                "label": "Status",
                "type": "text",
                "description": "The curation status of the model",
                "vocabulary": "Status"
            },
            {
                "id": "objective",
                "label": "Objective",
                "type": "text",
                "description": "Objective of the model or data"
            },
            {
                "id": "description",
                "label": "Description",
                "type": "text",
                "description": "General description of the study, data or model"
            }
        ],
        contact: ui.contact,
        reference: ui.reference,
        modelCategory: ui.modelCategory,

        // scope
        scope: [
            {
                id: "generalComment",
                label: "General comment",
                type: "text",
                description: "General comments on the scope of the study, data or model"
            },
            {
                id: "temporalInformation",
                label: "Temporal information",
                type: "text",
                description: "Spatial information (area) on which the model or data applies"
            },
            {
                id: "spatialInformation",
                label: "Spatial information",
                type: "text-array",
                description: "" // TODO: spatial information description
            }
        ],
        product: ui.product,
        hazard: ui.hazard,
        populationGroup: ui.populationGroup,

        // data background
        study: ui.study,
        studySample: ui.studySample,
        laboratory: ui.laboratory,
        assay: ui.assay,

        // model math
        modelMath: [
            {
                id: "fittingProcedure",
                label: "Fitting procedure",
                type: "text",
                description: "Procedure used to fit the data to the model equation"
            }, {
                id: "event",
                label: "Event",
                type: "text-array",
                description: "Definition of time-dependent parameter changes"
            }
        ],
        parameter: ui.parameter,
        qualityMeasures: ui.qualityMeasures,
        modelEquation: ui.modelEquation
    },

    doseResponseModel: {

        // General information
        generalInformation: [
            {
                id: "modelName",
                label: "Model name",
                type: "text",
                description: "A name given to the model",
                required: true
            },
            {
                id: "source",
                label: "Source",
                type: "text",
                description: "A related resource from which the described resource is derived",
                vocabulary: "Source"
            },
            {
                id: "identifier",
                label: "Identifier",
                type: "text",
                description: "An unambiguous ID given to the model",
                required: true
            },
            {
                id: "creationDate",
                label: "Creation date",
                type: "date",
                description: "Temporal information on the model creation date",
                required: true
            },
            {
                id: "modificationDate",
                label: "Modification date",
                type: "date-array",
                description: "Temporal information on the last modification of the model"
            },
            {
                id: "rights",
                label: "Rights",
                type: "text",
                description: "Information on rights held in an over the resource",
                vocabulary: "Rights",
                required: true
            },
            {
                id: "availability",
                label: "Availability",
                type: "text",
                description: "Availability of model",
                vocabulary: "Availability"
            },
            {
                id: "url",
                label: "URL",
                type: "url",
                description: "Web adress referencing the resource location"
            },
            {
                id: "format",
                label: "Format",
                type: "text",
                description: "form of the model (file extension)",
                vocabulary: "Format"
            },
            {
                id: "language",
                label: "Language",
                type: "text",
                description: "A language of the resource (some data or reports can be available in French language for example)",
                vocabulary: "Language"
            },
            {
                id: "software",
                label: "Software",
                type: "text",
                description: "The program in which the model has been implemented",
                vocabulary: "Software"
            },
            {
                id: "languageWrittenIn",
                label: "Language written in",
                type: "text",
                description: "Language used to write the model, e.g. R or MatLab",
                vocabulary: "Language_written_in"
            },
            {
                id: "status",
                label: "Status",
                type: "text",
                description: "The curation status of the model",
                vocabulary: "Status"
            },
            {
                id: "objective",
                label: "Objective",
                type: "text",
                description: "Objective of the model"
            },
            {
                id: "description",
                label: "Description",
                type: "text",
                description: "General description of the model"
            }
        ],
        modelCategory: ui.modelCategory,
        contact: ui.contact,
        reference: ui.reference,

        // Scope
        scope: [
            {
                id: "generalComment",
                label: "General comment",
                type: "text",
                description: "General comments on the scope of the model"
            },
            {
                id: "temporalInformation",
                label: "Temporal information",
                type: "text",
                description: "Temporal information on which the model applies / An interval of time that is named or defined by its start and end dates (period of study)"
            },
            {
                id: "spatialInformation",
                label: "Spatial information",
                type: "text",
                description: "Spatial information (area) on which the model applies"
            }
        ],
        hazard: ui.hazard,
        populationGroup: ui.populationGroup,

        // Model math
        modelMath: [
            {
                id: "fittingProcedure",
                label: "Fitting procedure",
                type: "text",
                description: "Procedure used to fit the data to the model equation"
            },
            {
                id: "event",
                label: "Event",
                type: "text-array",
                description: "Definition of time-dependent parameter changes"
            }
        ],
        parameter: ui.parameter,
        qualityMeasures: ui.qualityMeasures,
        modelEquation: ui.modelEquation,
        exposure: ui.exposure
    },

    toxicologicalModel: {

        // scope
        scope: [
            {
                id: "generalComment",
                label: "General comment",
                type: "text",
                description: "General comments on the scope of the study, data or model"
            },
            {
                id: "temporalInformation",
                label: "Temporal information",
                type: "text",
                description: "Spatial information (area) on which the model or data applies"
            },
            {
                id: "spatialInformation",
                label: "Spatial information",
                type: "text-array",
                description: "" // TODO: spatial information description
            }
        ],
        hazard: ui.hazard,
        populationGroup: ui.populationGroup
    },

    exposureModel: {},

    processModel: {

        // scope
        scope: [
            {
                id: "generalComment",
                label: "General comment",
                type: "text",
                description: "General comments on the scope of the study, data or model"
            },
            {
                id: "temporalInformation",
                label: "Temporal information",
                type: "text",
                description: "Spatial information (area) on which the model or data applies"
            },
            {
                id: "spatialInformation",
                label: "Spatial information",
                type: "text-array",
                description: "" // TODO: spatial information description
            }
        ],
        product: ui.product,
        hazard: ui.hazard
    },

    consumptionModel: {

        // scope
        scope: [
            {
                id: "generalComment",
                label: "General comment",
                type: "text",
                description: "General comments on the scope of the study, data or model"
            },
            {
                id: "temporalInformation",
                label: "Temporal information",
                type: "text",
                description: "Spatial information (area) on which the model or data applies"
            },
            {
                id: "spatialInformation",
                label: "Spatial information",
                type: "text-array",
                description: "" // TODO: spatial information description
            }
        ],
        product: ui.product,
        populationGroup: ui.populationGroup,

        // data background
        study: ui.study,
        studySample: ui.studySample,
        dietaryAssessmentMethod: ui.dietaryAssessmentMethod,
        laboratory: ui.laboratory,
        assay: ui.assay
    },

    healthModel: {

        // scope
        scope: [
            {
                id: "generalComment",
                label: "General comment",
                type: "text",
                description: "General comments on the scope of the study, data or model"
            },
            {
                id: "temporalInformation",
                label: "Temporal information",
                type: "text",
                description: "Spatial information (area) on which the model or data applies"
            },
            {
                id: "spatialInformation",
                label: "Spatial information",
                type: "text-array",
                description: "" // TODO: spatial information description
            }
        ],
        hazard: ui.hazard,
        populationGroup: ui.populationGroup
    },

    riskModel: {},
    qraModel: {}
}

schemas.genericModel = Object.assign(schemas.genericModel, _genericModelScope);
schemas.genericModel = Object.assign(schemas.genericModel, _genericModelDataBackground);
schemas.genericModel = Object.assign(schemas.genericModel, _genericModelModelMath);

schemas.dataModel = Object.assign(schemas.dataModel, _genericModelScope);
schemas.dataModel = Object.assign(schemas.dataModel, _genericModelDataBackground);

schemas.doseResponseModel = Object.assign(schemas.doseResponseModel, _predictiveModelDataBackground);

schemas.toxicologicalModel = Object.assign(schemas.toxicologicalModel, _predictiveModelGeneralInformation);
schemas.toxicologicalModel = Object.assign(schemas.toxicologicalModel, _predictiveModelDataBackground);
schemas.toxicologicalModel = Object.assign(schemas.toxicologicalModel, _genericModelModelMath);

schemas.predictiveModel = Object.assign(schemas.predictiveModel, _predictiveModelGeneralInformation);
schemas.predictiveModel = Object.assign(schemas.predictiveModel, _predictiveModelDataBackground);
schemas.predictiveModel = Object.assign(schemas.predictiveModel, _predictiveModelModelMath);

schemas.exposureModel = Object.assign(schemas.exposureModel, _predictiveModelGeneralInformation);
schemas.exposureModel = Object.assign(schemas.exposureModel, _exposureModelScope);
schemas.exposureModel = Object.assign(schemas.exposureModel, _genericModelDataBackground);
schemas.exposureModel = Object.assign(schemas.exposureModel, _genericModelModelMath);

schemas.processModel = Object.assign(schemas.processModel, _predictiveModelGeneralInformation);
schemas.processModel = Object.assign(schemas.processModel, _predictiveModelDataBackground);
schemas.processModel = Object.assign(schemas.processModel, _predictiveModelModelMath);

schemas.consumptionModel = Object.assign(schemas.consumptionModel, _predictiveModelGeneralInformation);
schemas.consumptionModel = Object.assign(schemas.consumptionModel, _predictiveModelModelMath);

schemas.healthModel = Object.assign(schemas.healthModel, _predictiveModelGeneralInformation);
schemas.healthModel = Object.assign(schemas.healthModel, _predictiveModelDataBackground);
schemas.healthModel = Object.assign(schemas.healthModel, _genericModelModelMath);

schemas.riskModel = Object.assign(schemas.riskModel, _predictiveModelGeneralInformation);
schemas.riskModel = Object.assign(schemas.riskModel, _exposureModelScope);
schemas.riskModel = Object.assign(schemas.riskModel, _genericModelDataBackground);
schemas.riskModel = Object.assign(schemas.riskModel, _genericModelModelMath);

schemas.qraModel = Object.assign(schemas.qraModel, _predictiveModelGeneralInformation);
schemas.qraModel = Object.assign(schemas.qraModel, _exposureModelScope);
schemas.qraModel = Object.assign(schemas.qraModel, _genericModelDataBackground);
schemas.qraModel = Object.assign(schemas.qraModel, _genericModelModelMath);