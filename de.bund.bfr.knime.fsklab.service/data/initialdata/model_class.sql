INSERT INTO model_class (id, name) VALUES (0, '(Data)');
INSERT INTO model_class (id, name) VALUES (1, 'Consumption model');
INSERT INTO model_class (id, name) VALUES (2, 'Dose-response model');
INSERT INTO model_class (id, name) VALUES (3, 'Exposure model');
INSERT INTO model_class (id, name) VALUES (4, 'Health metrics model');
INSERT INTO model_class (id, name) VALUES (5, 'Other Empirical models');
INSERT INTO model_class (id, name) VALUES (6, 'Predictive model');
INSERT INTO model_class (id, name) VALUES (7, 'Process model');
INSERT INTO model_class (id, name) VALUES (8, 'QRA model');
INSERT INTO model_class (id, name) VALUES (9, 'Risk characterization model');
INSERT INTO model_class (id, name) VALUES (10, 'Toxicological reference value model');

-- Predictive models
INSERT INTO model_subclass (id, name, class_id) VALUES (0, 'Growth', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (1, 'Growth boundary model', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (2, 'Inactivation', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (3, 'Maximum population density (MPD)', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (4, 'Metabolite formation', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (5, 'Other', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (6, 'Primary', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (7, 'Primary-secondary', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (8, 'Secondary', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (9, 'Spoilage', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (10, 'Survival', 6);
INSERT INTO model_subclass (id, name, class_id) VALUES (11, 'Transfer', 6);

-- Other Empirical Models
INSERT INTO model_subclass (id, name, class_id) VALUES (12, 'Epidemiological models', 5);
INSERT INTO model_subclass (id, name, class_id) VALUES (13, 'Fluid dynamic models', 5);
INSERT INTO model_subclass (id, name, class_id) VALUES (14, 'Other', 5);
INSERT INTO model_subclass (id, name, class_id) VALUES (15, 'Pharmacokinetics models', 5);
INSERT INTO model_subclass (id, name, class_id) VALUES (16, 'Temporary models', 5);
INSERT INTO model_subclass (id, name, class_id) VALUES (17, 'Time-temperature models', 5);

-- Process models
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (18, 'ANIMAL FARM', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (19, 'CONSUMER', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (20, 'CROP FARM', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (21, 'FISHERY', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (22, 'FOOD SERVICE', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (23, 'OTHER', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (24, 'PROCESSING CENTER-SLAUGHTER', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (25, 'PROCESSING CENTER-SLAUGHTER: Canning', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (26, 'PROCESSING CENTER-SLAUGHTER: Churning', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (27, 'PROCESSING CENTER-SLAUGHTER: Concentration', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (28, 'PROCESSING CENTER-SLAUGHTER: Cooking', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (29, 'PROCESSING CENTER-SLAUGHTER: Cooking in air (Baking)', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (30, 'PROCESSING CENTER-SLAUGHTER: Cooking in oil (Frying)', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (31, 'PROCESSING CENTER-SLAUGHTER: Cooking in water', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (32, 'PROCESSING CENTER-SLAUGHTER: Crushing', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (33, 'PROCESSING CENTER-SLAUGHTER: Decortication', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (34, 'PROCESSING CENTER-SLAUGHTER: Dehydration', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (35, 'PROCESSING CENTER-SLAUGHTER: Desugaring', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (36, 'PROCESSING CENTER-SLAUGHTER: Extrusion', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (37, 'PROCESSING CENTER-SLAUGHTER: Fermentation', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (38, 'PROCESSING CENTER-SLAUGHTER: Flaking', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (39, 'PROCESSING CENTER-SLAUGHTER: Freezing', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (40, 'PROCESSING CENTER-SLAUGHTER: Heating', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (41, 'PROCESSING CENTER-SLAUGHTER: Hydrogenation', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (42, 'PROCESSING CENTER-SLAUGHTER: Hydrolysis', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (43, 'PROCESSING CENTER-SLAUGHTER: Infusion-extractions', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (44, 'PROCESSING CENTER-SLAUGHTER: Juicing', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (45, 'PROCESSING CENTER-SLAUGHTER: Milk pasteurisation', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (46, 'PROCESSING CENTER-SLAUGHTER: Milling', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (47, 'PROCESSING CENTER-SLAUGHTER: Milling - bran production', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (48, 'PROCESSING CENTER-SLAUGHTER: Milling - refined flour', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (49, 'PROCESSING CENTER-SLAUGHTER: Milling - unprocessed flour', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (50, 'PROCESSING CENTER-SLAUGHTER: Oil production', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (51, 'PROCESSING CENTER-SLAUGHTER: Oil production - Cold press', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (52, 'PROCESSING CENTER-SLAUGHTER: Oil production - refined oils', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (53, 'PROCESSING CENTER-SLAUGHTER: Oil production - Solvent Extraction', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (54, 'PROCESSING CENTER-SLAUGHTER: Oil production - Virgin oil after cold press', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (55, 'PROCESSING CENTER-SLAUGHTER: Oil production - Warm press', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (56, 'PROCESSING CENTER-SLAUGHTER: Other', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (57, 'PROCESSING CENTER-SLAUGHTER: Packaging', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (58, 'PROCESSING CENTER-SLAUGHTER: Pasteurisation', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (59, 'PROCESSING CENTER-SLAUGHTER: Peeling (edible peel)', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (60, 'PROCESSING CENTER-SLAUGHTER: Peeling (inedible peel)', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (61, 'PROCESSING CENTER-SLAUGHTER: Pelleting', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (62, 'PROCESSING CENTER-SLAUGHTER: Pickling', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (63, 'PROCESSING CENTER-SLAUGHTER: Polishing', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (64, 'PROCESSING CENTER-SLAUGHTER: Pregelatinisation', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (65, 'PROCESSING CENTER-SLAUGHTER: Preserving', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (66, 'PROCESSING CENTER-SLAUGHTER: Pressing', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (67, 'PROCESSING CENTER-SLAUGHTER: Processing', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (68, 'PROCESSING CENTER-SLAUGHTER: Production of alcoholic beverages', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (69, 'PROCESSING CENTER-SLAUGHTER: Refining', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (70, 'PROCESSING CENTER-SLAUGHTER: Silage-Fodder production', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (71, 'PROCESSING CENTER-SLAUGHTER: Smoking', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (72, 'PROCESSING CENTER-SLAUGHTER: Sugar production', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (73, 'PROCESSING CENTER-SLAUGHTER: Sugar production - raw', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (74, 'PROCESSING CENTER-SLAUGHTER: Sugar production - refined', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (75, 'PROCESSING CENTER-SLAUGHTER: Unknown', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (76, 'PROCESSING CENTER-SLAUGHTER: Wet-milling', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (77, 'PROCESSING CENTER-SLAUGHTER: Wine production', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (78, 'PROCESSING CENTER-SLAUGHTER: Wine production - red wine cold process', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (79, 'PROCESSING CENTER-SLAUGHTER: Wine production - red wine warm process', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (80, 'PROCESSING CENTER-SLAUGHTER: Wine production - white wine', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (81, 'RETAIL', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (82, 'STORAGE', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (83, 'STORAGE: ambient temperature', 'Stored at the surrounding temperature or normal storage conditions, which means storage in a dry, clean, well ventilated area at room temperatures between 15° to 25°C (59°-77°F) or up to 30°C, depending on climatic conditions.', 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (84, 'STORAGE: at 2°-8°C', 'Stored at 2°-8°C (36°-46°F): for heat sensitive products that must not be frozen.', 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (85, 'STORAGE: cool', 'Stored between 8°-15°C (45°-59°F).', 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (86, 'STORAGE: frozen', 'Stored at -20°C (4°F).', 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (87, 'STORAGE: room temperature', 'Stored at 15°-25°C (59°-77°F).', 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (88, 'TRANSPORT-DISTRIBUTION', NULL, 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (89, 'TRANSPORT-DISTRIBUTION: ambient temperature', 'Transported at the surrounding temperature or normal storage conditions, which means storage in a dry, clean, well ventilated vehicle at room temperatures between 15° to 25°C (59°-77°F) or up to 30°C, depending on climatic conditions.', 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (90, 'TRANSPORT-DISTRIBUTION: at 2°-8°C', 'Transported at 2°-8°C (36°-46°F): for heat sensitive products that must not be frozen.', 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (91, 'TRANSPORT-DISTRIBUTION: cool', 'Transported between 8°-15°C (45°-59°F).', 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (92, 'TRANSPORT-DISTRIBUTION: frozen', 'Transported at -20°C (4°F).', 7);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (93, 'TRANSPORT-DISTRIBUTION: room temperature', 'Transported at 15°-25°C (59°-77°F).', 7);

-- Dose-response models
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (94, 'Children', NULL, 2);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (95, 'Elderly', NULL, 2);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (96, 'Imunocompromised ', NULL, 2);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (97, 'Other', NULL, 2);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (98, 'Pregnant ', NULL, 2);

-- Health-metrics models
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (99, 'Cost per illness', NULL, 4);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (100, 'DALY ', 'Disability-Adjusted Life-Year', 4);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (101, 'QALY', 'Quality-adjusted life year', 4);

-- QRA models
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (102, 'QCRA', 'Quantitative chemical risk assessment model', 8);
INSERT INTO model_subclass (id, name, comment, class_id) VALUES (103, 'QMRA', 'Quantitative microbial risk assessment model', 8);